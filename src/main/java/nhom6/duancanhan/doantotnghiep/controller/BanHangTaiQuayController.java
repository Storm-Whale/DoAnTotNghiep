package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
//    hoaDon = hoaDonRepository.findById(idHoaDon).get();
//    KhachHang khachHang = new KhachHang();
//        khachHang.setTen("Le Nam");
//    NhanVien nhanVien = new NhanVien();
//        nhanVien.setTen("Le Ha");
//        hoaDon.setNguoiTao(nhanVien);
//    @PostMapping("taohoadon")
//    public String taoHoaDon(
//            Model model
//    ) {
//        HoaDon hoaDon = new HoaDon();
//        model.addAttribute("hoaDon", hoaDon);
//        hoaDon.setTrangThai(0);
//        hoaDonRepository.save(hoaDon);
//        return "redirect:/admin/taiquay";
//    }
    @PostMapping("/taohoadon")
    @ResponseBody
    public HoaDon taoHoaDon(Model model) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(1); // Set trạng thái mới tạo
        hoaDonRepository.save(hoaDon); // Lưu vào cơ sở dữ liệu
        return hoaDon;
    }


    //    @GetMapping("/detail/{idHD}")
//    public String detailHoaDon(@PathVariable("idHD") Integer idHD, Model model) {
//        idHoaDon = idHD;
//        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
//        model.addAttribute("hoaDon", hoaDon);
//
//        model.addAttribute("listCTSP", sanPhamChiTietRepository.findAll());
//        hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
//        model.addAttribute("listHDCT", hoaDonChiTiet);
//        model.addAttribute("listHD", hoaDonRepository.findAll());
////        model.addAttribute("tongTien", tongTien);
////        tongTien = 0.0;
////        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiet) {
////            tongTien += hoaDonChiTiet.getTongTien();
////        }
//        return "/admin/BanhangTaiQuay/index";
//    }
//    @GetMapping("/detail/{idHD}")
//    public String detailHoaDon(@PathVariable("idHD") Integer idHD, Model model) {
//        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
//        model.addAttribute("hoaDon", hoaDon);
//
//        model.addAttribute("listCTSP", sanPhamChiTietRepository.findAll());
//        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
//        model.addAttribute("listHDCT", hoaDonChiTiet);
//
//        return "/admin/BanhangTaiQuay/index";
//    }
    @GetMapping("/detail/{idHD}")
    public String detailHoaDon(@PathVariable("idHD") Integer idHD, Model model) {
        idHoaDon = idHD;
        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
        model.addAttribute("hoaDon", hoaDon);

        // Hiển thị danh sách sản phẩm chi tiết và hóa đơn chi tiết
        model.addAttribute("listCTSP", sanPhamChiTietRepository.findAll());
        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
        model.addAttribute("listHDCT", hoaDonChiTiet);

        // Hiển thị hóa đơn đầu tiên
        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
//        if (firstHoaDon != null) {
//            model.addAttribute("firstHoaDon", firstHoaDon);
//        } else {
//            model.addAttribute("firstHoaDon", new HoaDon()); // Tạo đối tượng rỗng để tránh lỗi
//        }
        // Tạo danh sách hóa đơn chờ và thêm hóa đơn đầu tiên vào danh sách nếu cần
        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
        // Kiểm tra xem hóa đơn đầu tiên đã có trong danh sách chưa
        if (firstHoaDon != null && !listHD.contains(firstHoaDon)) {
            listHD.add(0, firstHoaDon); // Thêm hóa đơn đầu tiên vào đầu danh sách
        }
        model.addAttribute("listHD", listHD.stream().limit(5).collect(Collectors.toList())); // Giới hạn danh sách về 5 hóa đơn
        return "/admin/BanhangTaiQuay/index"; // Trả về view của bạn
    }

//    @GetMapping("/detail/{idHD}")
//    public String detailHoaDon(@PathVariable("idHD") Integer idHD, Model model) {
//        idHoaDon = idHD;
//        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
//        model.addAttribute("hoaDon", hoaDon);
//
//        // Hiển thị danh sách sản phẩm chi tiết và hóa đơn chi tiết
//        model.addAttribute("listCTSP", sanPhamChiTietRepository.findAll());
//        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
//        model.addAttribute("listHDCT", hoaDonChiTiet);
//
//        // Hiển thị hóa đơn đầu tiên
////        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc(); // Lấy hóa đơn đầu tiên
////        model.addAttribute("firstHoaDon", firstHoaDon);
//        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
//        model.addAttribute("firstHoaDon", firstHoaDon != null ? firstHoaDon : new HoaDon());
//        // Hiển thị tối đa 4 hóa đơn mới tạo
////        List<HoaDon> listHD = hoaDonRepository.findTop4ByOrderByIdDesc(); // Lấy 4 hóa đơn mới nhất
////        HoaDon listHD = hoaDonRepository.save(new HoaDon());
//        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
//        model.addAttribute("listHD", listHD.stream().limit(5).collect(Collectors.toList()));
//
//        return "/admin/BanhangTaiQuay/index";
//    }

//    @GetMapping("/detail/{idHD}")
//    public String detailHoaDon(@PathVariable("idHD") Integer idHD, Model model) {
//        idHoaDon = idHD;
//        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
//        model.addAttribute("hoaDon", hoaDon);
//        // Lấy danh sách sản phẩm chi tiết và hóa đơn chi tiết
//        model.addAttribute("listCTSP", sanPhamChiTietRepository.findAll());
//        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
//        model.addAttribute("listHDCT", hoaDonChiTiet);
//        // Lấy danh sách hóa đơn và thêm vào model
//        List<HoaDon> listHD = hoaDonRepository.findAll();;
//        model.addAttribute("listHD", listHD);
//
//        return "/admin/BanhangTaiQuay/index";
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


