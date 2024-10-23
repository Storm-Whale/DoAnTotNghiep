package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.GioHangService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamGioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/taiquay")
public class BanHangTaiQuayController {

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    SanPhamGioHangService gioHangService;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    SanPhamGioHangRepository sanPhamGioHangRepository;

    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;
    List<SanPhamGioHang> sanPhamGioHang = new ArrayList<>();
    List<HoaDonChiTiet> hoaDonChiTiet = new ArrayList<>();
    Integer idGioHang = 1;
    Integer idHoaDon = 1;

//    @PostMapping("taohoadon")
//    public String taoHoaDon(
//                            @RequestParam("idHoaDon") Integer idHoaDon, Model model
//    ) {
//        long soLuongHoaDon = hoaDonRepository.demSoLuongHoaDon();
//
//        // Chỉ cho phép tạo tối đa 5 hóa đơn
//        if (soLuongHoaDon >= 5) {
//            model.addAttribute("errorMessage", "Không thể tạo quá 5 hóa đơn.");
//            return "redirect:/admin/taiquay"; // Trả về trang lỗi hoặc trang hiển thị thông báo
//        }
//
//        // Tạo mới hóa đơn nếu số lượng dưới 5
//        HoaDon hoaDon = new HoaDon();
//        hoaDon = hoaDonRepository.findById(idHoaDon).orElse(null);
//        hoaDon.setTrangThai(1);  // 1 cho trạng thái hóa đơn chưa thanh toán
//        hoaDonRepository.save(hoaDon);
//        if (hoaDon != null) {
//            hoaDon.setTrangThai(0);  // 0 cho trạng thái hóa đơn đã thanh toán
//            hoaDonRepository.save(hoaDon);
//        }
//
//
//        // Thêm ID vào model để truyền về phía view
//
//        // Redirect hoặc trả về trang kèm ID hóa đơn
//        return "redirect:/admin/taiquay";
//    }


    @GetMapping("/add-sanpham-giohang/{id}")
    public String addSanPhamGioHang(@PathVariable("id") Integer id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).get();
        sanPhamGioHang = sanPhamGioHangRepository.findAllByGioHangId(idGioHang);
        boolean checkCongDon = false;
        for (SanPhamGioHang ghct : sanPhamGioHang) {
            if (ghct.getSanPhamChiTiet().getId().equals(id)) {
                ghct.setSoLuong(ghct.getSoLuong() + 1);
                // tru so luong trong database
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - 1);
                sanPhamChiTietRepository.save(sanPhamChiTiet);
                BigDecimal tongTien = sanPhamGioHang.stream()
                        .map(spgh -> spgh.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(spgh.getSoLuong())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                sanPhamGioHangRepository.save(ghct);
                model.addAttribute("tongTien", tongTien);
                checkCongDon = true;
                break;
            }
        }
        if (!checkCongDon) {
            GioHang gioHang = gioHangRepository.findById(1)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng"));
            SanPhamGioHang sanPhamGioHang = SanPhamGioHang.builder()
                    .gioHang(gioHang)
                    .sanPhamChiTiet(sanPhamChiTiet)
                    .soLuong(1)
                    .build();
            sanPhamGioHangRepository.save(sanPhamGioHang);
            List<SanPhamGioHang> sanPhamGioHangs = sanPhamGioHangRepository.findAll();
            BigDecimal tongTien = sanPhamGioHangs.stream()
                    .map(spgh -> spgh.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(spgh.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("tongTien", tongTien);
            model.addAttribute("list", sanPhamGioHangs);
        }

        return "redirect:/admin/taiquay";
    }


    @GetMapping("/add-sanpham-hdct/{id}")
    public String addSanPhamHDCT(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Sản phẩm không tồn tại"));
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
        boolean checkCongDon = false;

        for (HoaDonChiTiet hdct : hoaDonChiTietList) {
            if (hdct.getSanPhamChiTiet().getId().equals(id)) {
                if (sanPhamChiTiet.getSoLuong() >= 1) {
                    System.out.println("Tìm thấy sản phẩm trong hóa đơn, cộng dồn số lượng");
                    hdct.setSoLuong(hdct.getSoLuong() + 1);
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - 1);
                    sanPhamChiTietRepository.save(sanPhamChiTiet);

                    BigDecimal tongTien = hoaDonChiTietList.stream()
                            .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hoaDonChiTietRepository.save(hdct);
                    model.addAttribute("tongTien", tongTien);
                } else {
                    return model.addAttribute("errorSoLuong", "Sản Phẩm Đã Bán Hết").toString();

                }
                checkCongDon = true;
                break;
            }
        }

        if (!checkCongDon) {
            HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn"));

            HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                    .hoaDon(hoaDon)
                    .sanPhamChiTiet(sanPhamChiTiet)
                    .soLuong(1)
                    .build();
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("tongTien", tongTien);
            model.addAttribute("listHDCT", hoaDonChiTietList);
        }

        return "redirect:/admin/taiquay";
    }

}


