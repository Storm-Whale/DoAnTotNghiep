package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.entity.PhuongThucThanhToan;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.service.serviceimpl.InvoidPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/taiquay")
public class TaiQuayController {

    final
    SanPhamChiTietService sanPhamChiTietService;
    final
    SanPhamService sanPhamService;
    final
    MauSacService mauSacService;
    final
    KichCoService kichCoService;
    final HoaDonService hoaDonService;
    final
    KhachHangService khachHangService;
    final
    PhieuGiamGiaService phieuGiamGiaService;
    private final ThuongHieuService thuongHieuService;
    private final ChatLieuService chatLieuService;
    private final KieuCoAoService kieuCoAoService;
    private final KieuTayAoService kieuTayAoService;
    private final InvoidPdfService invoidPdfService;

    @Autowired
    SanPhamGioHangRepository sanPhamGioHangRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    PhieuGiamGiaRepository phieuGiamGiaRepository;

    private Integer idHoaDon = 1;

    private String maPhieuGiamGia = "";

    public TaiQuayController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, MauSacService mauSacService, KichCoService kichCoService, HoaDonService hoaDonService, KhachHangService khachHangService, PhieuGiamGiaService phieuGiamGiaService, ThuongHieuService thuongHieuService, ChatLieuService chatLieuService, KieuCoAoService kieuCoAoService, KieuTayAoService kieuTayAoService, InvoidPdfService invoidPdfService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.sanPhamService = sanPhamService;
        this.mauSacService = mauSacService;
        this.kichCoService = kichCoService;
        this.hoaDonService = hoaDonService;
        this.khachHangService = khachHangService;
        this.phieuGiamGiaService = phieuGiamGiaService;
        this.thuongHieuService = thuongHieuService;
        this.chatLieuService = chatLieuService;
        this.kieuCoAoService = kieuCoAoService;
        this.kieuTayAoService = kieuTayAoService;
        this.invoidPdfService = invoidPdfService;
    }
// TODO: SHOWINDEX
    @GetMapping("")
    public String showIndex(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "ngayBatDau", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
                            @RequestParam(value = "ngayKetThuc", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc,
                            @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                            @RequestParam(value = "chatLieuId", required = false) Integer chatLieuId,
                            @RequestParam(value = "tayAoId", required = false) Integer tayAoId,
                            @RequestParam(value = "coAoId", required = false) Integer coAoId,
                            @RequestParam(value = "kichCoId", required = false) Integer kichCoId,
                            @RequestParam(value = "mauSacId", required = false) Integer mauSacId,
                            @RequestParam(value = "kieuGiamGia", required = false) Integer kieuGiamGia,
                            @RequestParam(value = "trangThai", required = false) Integer trangThai,
                            @RequestParam(name = "soDienThoai", required = false) String soDienThoai
            , RedirectAttributes redirectAttributes,
                            Model model) {
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("sanPhamChiTiet", sanPhamChiTietService.getAllSanPhamChiTiet());
        model.addAttribute("sanPham", sanPhamService.getAllSanPham());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("thuongHieu", thuongHieuService.getAll());
        model.addAttribute("chatLieu", chatLieuService.getAll());
        model.addAttribute("tayAo", kieuTayAoService.getAll());
        model.addAttribute("coAo", kieuCoAoService.getAll());
//        model.addAttribute("listKH",khachHangService.getAll());
//        model.addAttribute("listPGG",phieuGiamGiaService.getAll());
        Page<PhieuGiamGia> pageFind = phieuGiamGiaService.findByCriteria(keyword, ngayBatDau,
                ngayKetThuc, kieuGiamGia, trangThai, page, size);
        List<PhieuGiamGia> listPGG = pageFind.getContent();
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());

        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("ngayBatDau", ngayBatDau);
        model.addAttribute("ngayKetThuc", ngayKetThuc);
        model.addAttribute("kieuGiamGia", kieuGiamGia);
        model.addAttribute("trangThai", trangThai);
        //
        Page<SanPhamChiTiet> pageFind2 = sanPhamChiTietService.timKiemSanPham(keyword, thuongHieuId, chatLieuId, tayAoId,
                coAoId, kichCoId, mauSacId, trangThai, page, size);
        List<SanPhamChiTiet> ListSPCT = pageFind2.getContent();
        model.addAttribute("ListSPCT", ListSPCT);
        //
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("kichCoId", kichCoId);
        model.addAttribute("mauSacId", mauSacId);

        List<SanPhamGioHang> sanPhamGioHangs = sanPhamGioHangRepository.findAll();
        model.addAttribute("list", sanPhamGioHangs);
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAll();
//        model.addAttribute("listHDCT", hoaDonChiTietList);
        //
//        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
//        model.addAttribute("firstHoaDon", firstHoaDon != null ? firstHoaDon : new HoaDon());
        // Tạo danh sách hóa đơn chờ và thêm hóa đơn đầu tiên vào danh sách nếu cần
        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
        // Kiểm tra xem hóa đơn đầu tiên đã có trong danh sách chưa
//        if (firstHoaDon != null && !listHD.contains(firstHoaDon)) {
//            listHD.add(0, firstHoaDon); // Thêm hóa đơn đầu tiên vào đầu danh sách
//        }
        model.addAttribute("listHD", listHD.stream().limit(5).collect(Collectors.toList())); // Giới hạn danh sách về 5 hóa đơn


        BigDecimal tongTienGH = sanPhamGioHangs.stream()
                .map(spgh -> spgh.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(spgh.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("tongTienGH", tongTienGH);

        BigDecimal tongTien = hoaDonChiTietList.stream()
                .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        model.addAttribute("tongTien", tongTien);
        model.addAttribute("idHoaDon", idHoaDon);

        KhachHang khachHang = khachHangService.findBySoDienThoaiKhachHang(soDienThoai);
        redirectAttributes.addFlashAttribute("khachHang", khachHang);
        model.addAttribute("khachHangThemNhanh",new KhachHang());
//        BigDecimal tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGia, tongTien);
//        model.addAttribute("tongTienSauGiam", tongTienSauGiam);
        // Thêm vào model
        return "/admin/BanhangTaiQuay/index";
    }

//    @GetMapping("khachhang")
//    public String khachhang(
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "trangThai", required = false) Integer trangThai,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "5") int size,
//            Model model) {
//        if (keyword != null) {
//            keyword = keyword.trim();
//            if (keyword.isEmpty()) {
//                keyword = null; // Đặt thành null để khớp với logic xử lý trong service
//            }
//        }
//        if (page < 0) {
//            page = 0;
//        }
//        Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
//        int totalPages = listKH.getTotalPages();
//        if (page >= totalPages) {
//            page = totalPages > 0 ? totalPages - 1 : 0; // Go to the last page if out of bounds or reset to 0 if no pages exist
//            listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size); // Fetch the last page data
//        }
//        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
//        model.addAttribute("khachHang", new KhachHang());
//        model.addAttribute("listKH", listKH);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("trangThai", trangThai);
//        model.addAttribute("pageNumbers", pageNumbers);
//        model.addAttribute("idHoaDon",idHoaDon);
//
//        return "/admin/BanhangTaiQuay/index";
//    }


    @GetMapping("/phieugiamgia")
    public String phieuGiamGia(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "ngayBatDau", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
                               @RequestParam(value = "ngayKetThuc", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc,
                               @RequestParam(value = "kieuGiamGia", required = false) Integer kieuGiamGia,
                               @RequestParam(value = "trangThai", required = false) Integer trangThai,
                               Model model) {
        Page<PhieuGiamGia> pageFind = phieuGiamGiaService.findByCriteria(keyword, ngayBatDau,
                ngayKetThuc, kieuGiamGia, trangThai, page, size);
        List<PhieuGiamGia> listPGG = pageFind.getContent();
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());

        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("ngayBatDau", ngayBatDau);
        model.addAttribute("ngayKetThuc", ngayKetThuc);
        model.addAttribute("kieuGiamGia", kieuGiamGia);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("idHoaDon", idHoaDon);
        model.addAttribute("khachHangThemNhanh",new KhachHang());
        return "/admin/BanhangTaiQuay/index";
    }
// TODO: SAN PHAM
    @GetMapping("/sanpham")
    public String sanpham(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "5") int size,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                          @RequestParam(value = "chatLieuId", required = false) Integer chatLieuId,
                          @RequestParam(value = "tayAoId", required = false) Integer tayAoId,
                          @RequestParam(value = "coAoId", required = false) Integer coAoId,
                          @RequestParam(value = "kichCoId", required = false) Integer kichCoId,
                          @RequestParam(value = "mauSacId", required = false) Integer mauSacId,
                          @RequestParam(value = "trangThai", required = false) Integer trangThai,
                          Model model) {
        Page<SanPhamChiTiet> pageFind = sanPhamChiTietService.timKiemSanPham(keyword, thuongHieuId, chatLieuId, tayAoId,
                coAoId, kichCoId, mauSacId, trangThai, page, size);
        List<SanPhamChiTiet> ListSPCT = pageFind.getContent();
        model.addAttribute("ListSPCT", ListSPCT);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());
        //
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("thuongHieu", thuongHieuService.getAll());
        model.addAttribute("chatLieu", chatLieuService.getAll());
        model.addAttribute("tayAo", kieuTayAoService.getAll());
        model.addAttribute("coAo", kieuCoAoService.getAll());
        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("kichCoId", kichCoId);
        model.addAttribute("mauSacId", mauSacId);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("idHoaDon", idHoaDon);
        model.addAttribute("khachHangThemNhanh",new KhachHang());
        return "/admin/BanhangTaiQuay/index";
    }

    @PostMapping("/taohoadon")
    @ResponseBody
    public HoaDon taoHoaDon(Model model) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(1); // Set trạng thái mới tạo
        hoaDonRepository.save(hoaDon); // Lưu vào cơ sở dữ liệu
        return hoaDon;
    }

    // TODO : Detail
    @GetMapping("/detail/{idHD}")
    public String detailHoaDon(@PathVariable("idHD") Integer idHD,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                               @RequestParam(value = "chatLieuId", required = false) Integer chatLieuId,
                               @RequestParam(value = "tayAoId", required = false) Integer tayAoId,
                               @RequestParam(value = "coAoId", required = false) Integer coAoId,
                               @RequestParam(value = "kichCoId", required = false) Integer kichCoId,
                               @RequestParam(value = "mauSacId", required = false) Integer mauSacId,
                               @RequestParam(value = "trangThai", required = false) Integer trangThai,
             Model model) {
        idHoaDon = idHD;
        Page<SanPhamChiTiet> pageFind = sanPhamChiTietService.timKiemSanPham(keyword, thuongHieuId, chatLieuId, tayAoId,
                coAoId, kichCoId, mauSacId, trangThai, page, size);
        List<SanPhamChiTiet> listCTSP = pageFind.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());
        //
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("thuongHieu", thuongHieuService.getAll());
        model.addAttribute("chatLieu", chatLieuService.getAll());
        model.addAttribute("tayAo", kieuTayAoService.getAll());
        model.addAttribute("coAo", kieuCoAoService.getAll());
        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("kichCoId", kichCoId);
        model.addAttribute("mauSacId", mauSacId);
        model.addAttribute("trangThai", trangThai);
        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
        KhachHang khachHang = hoaDon.getKhachHang();
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("hoaDon", hoaDon);

        // Lấy danh sách sản phẩm chi tiết và hóa đơn chi tiết
        model.addAttribute("listCTSP", listCTSP);
        List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findAllByHoaDonId(idHD);
        model.addAttribute("listHDCT", hoaDonChiTiet);

//        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
        // model.addAttribute("firstHoaDon", firstHoaDon != null ? firstHoaDon : new HoaDon());
        // Tạo danh sách hóa đơn chờ và thêm hóa đơn đầu tiên vào danh sách nếu cần
        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
        // Kiểm tra xem hóa đơn đầu tiên đã có trong danh sách chưa
//        if (firstHoaDon != null && !listHD.contains(firstHoaDon)) {
//            listHD.add(0, firstHoaDon); // Thêm hóa đơn đầu tiên vào đầu danh sách
//        }
        model.addAttribute("listHD", listHD.stream().limit(5).collect(Collectors.toList())); // Giới hạn danh sách về 5 hóa đơn

        BigDecimal tongTien = hoaDonChiTiet.stream()
                .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lastTongTien = BigDecimal.ZERO;
        System.out.println("Ma Phieu Giam Gia : " + maPhieuGiamGia);
        if (maPhieuGiamGia != null && maPhieuGiamGia.length() != 0) {
            lastTongTien = phieuGiamGiaService.applyDiscount(maPhieuGiamGia, tongTien);
            System.out.println("Tong Tien : " + lastTongTien);
            model.addAttribute("maPhieuGiamGia",maPhieuGiamGia);
            maPhieuGiamGia = "";
        }
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("idHoaDon", idHoaDon);
        model.addAttribute("tongTienSauGiam", hoaDon != null ? lastTongTien : null);
        model.addAttribute("khachHangThemNhanh",new KhachHang());
        return "/admin/BanhangTaiQuay/index";
    }
// TODO : khachhang
@GetMapping("tim-kiem")
public String timKiemKhachHang(@RequestParam(name = "soDienThoai", required = false) String soDienThoai, HttpSession session
        , RedirectAttributes redirectAttributes,Model model) {

    KhachHang khachHang = khachHangService.findBySoDienThoaiKhachHang(soDienThoai);
    if (khachHang == null) {
        // Tạo đối tượng khách hàng mới với số điện thoại đã nhập
        KhachHang newKhachHang = new KhachHang();
        newKhachHang.setSoDienThoai(soDienThoai);
        // Gửi thông báo và form khách hàng mới đến view
        model.addAttribute("khachHangThemNhanh", newKhachHang);
        model.addAttribute("soDienThoaiNotFound", soDienThoai);
//            return "redirect:/admin/taiquay";
        return "redirect:/admin/taiquay#themnhanhkhachhang"; // View thêm khách hàng
    }

    HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
            .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
    // Cập nhật khách hàng cho hóa đơn
    if (hoaDon != null || hoaDon.getKhachHang() != null) {
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTrangThai(1);
        hoaDonRepository.save(hoaDon);
        redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
        redirectAttributes.addFlashAttribute("khachHang", khachHang);
//        if (hoaDon.getDiaChi() != null) {
//            model.addAttribute("diaChi", hoaDon.getDiaChi().getDiaChiChiTiet());
//        }
//    }else {
//        System.out.println("bug ơi");
    }
//        model.addAttribute("khachHang", khachHang);
//        model.addAttribute("hoaDon", hoaDon);
//        model.addAttribute("diaChi", hoaDon.getDiaChi());
    // Tạo ModelAndView để chuyển dữ liệu sang view Thymeleaf
    return "redirect:/admin/taiquay/detail/" + idHoaDon; // Tên view Thymeleaf để hiển thị kết quả
}
//    @Autowired
//    DiaChiRepository diaChiRepository;
//    @PostMapping("/cap-nhat-hoa-don")
//    public String capNhatHoaDon(@RequestParam Integer idHoaDon, @RequestParam String diaChi) {
//        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
//                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
//
//        // Tìm hoặc tạo mới địa chỉ
//        DiaChi diaChiObj = diaChiRepository.findByDiaChiChiTiet(diaChi)
//                .orElseGet(() -> {
//                    DiaChi newDiaChi = new DiaChi();
//                    newDiaChi.setDiaChiChiTiet(diaChi);
//                    diaChiRepository.save(newDiaChi);
//                    return newDiaChi;
//                });
//
//        // Cập nhật địa chỉ trong hóa đơn
//        hoaDon.setDiaChi(diaChiObj);
//        hoaDonRepository.save(hoaDon);
//
//        // Chuyển hướng về trang chi tiết hóa đơn
//        return "redirect:/admin/taiquay/detail/" + idHoaDon;
//    }
//    @PostMapping("/add")
//    public String add(@Valid @ModelAttribute("khachHangThemNhanh") KhachHang khachHang, BindingResult result, Model model,RedirectAttributes redirectAttributes) {
////        if (result.hasErrors()) {
////            for (FieldError error : result.getFieldErrors()) {
////                model.addAttribute(error.getField(), error.getDefaultMessage());
////            }
////            model.addAttribute("alertMessage", "Vui lòng kiểm tra thông tin nhập!");
////            return "redirect:/admin/taiquay#themnhanhkhachhang";
//////          return "redirect:/admin/khachhang/add" + "#demo-modal";
////        }
//        khachHangService.addKhachHang(khachHang);
//
//        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
//                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
//        // Cập nhật khách hàng cho hóa đơn
//        if (hoaDon != null || hoaDon.getKhachHang() != null) {
//            hoaDon.setKhachHang(khachHang);
//            hoaDon.setTrangThai(1);
//            hoaDonRepository.save(hoaDon);
//            redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
//            redirectAttributes.addFlashAttribute("khachHang", khachHang);
//        }
//        redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được thêm thành công!");
//        return "redirect:/admin/taiquay/detail/" + idHoaDon;
//    }
    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    //TODO THANH TOAN
    @GetMapping("/thanhtoan")
    @ResponseBody
    public String thanhtoan(
            @RequestParam("ghiChu") String ghiChu,
            @RequestParam("phuongThucThanhToan") Integer phuongThucId,
            @RequestParam(value = "maPhieuGiamGia", required = false) String maPhieuGiamGiaInput
            , Model model) throws IOException {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại."));
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);

        BigDecimal tongTien = hoaDonChiTietList.stream()
                .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongTienSauGiam = tongTien;
        System.out.println("Ma PGG Input: "+maPhieuGiamGiaInput);
        if (maPhieuGiamGiaInput != null && !maPhieuGiamGiaInput.isEmpty()) {
            try {
                PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGiaInput)
                        .orElseThrow(() -> new IllegalArgumentException("Mã phiếu giảm giá không hợp lệ."));
                tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGiaInput, tongTien);

                hoaDon.setPhieuGiamGia(phieuGiamGia);
                hoaDon.setTongTien(tongTienSauGiam);
                hoaDonRepository.save(hoaDon);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                return "redirect:/admin/taiquay/detail/" + idHoaDon;
            }
        }
        model.addAttribute("tongTien", tongTien);
        PhuongThucThanhToan phuongThuc = phuongThucThanhToanRepository.findById(phuongThucId).orElse(null);
        hoaDon.setPhuongThucThanhToan(phuongThuc);
        hoaDon.setTrangThai(tongTienSauGiam.compareTo(BigDecimal.ZERO) == 0 ? 1 : 2);
        hoaDon.setGhiChu(ghiChu);
        hoaDonRepository.save(hoaDon);

        byte[] pdfData = invoidPdfService.generateInvoicePdf(hoaDon);

        String fileName = "invoice_" + hoaDon.getId() + ".pdf";
        Path path = Paths.get("D://FALL_2024//DATN//DoAnTotNghiep//upload/" + fileName);
        Files.write(path, pdfData);

        // Return the file URL to the frontend
        String fileUrl = "D://FALL_2024//DATN//DoAnTotNghiep//upload/" + fileName;
        model.addAttribute("pdfUrl", fileUrl);

//        return "redirect:/admin/taiquay";
        return "/uploads/" + fileName;
//        return "redirect:/admin/hoadon";
    }

    //  TODO ADDSP
    @GetMapping("/add-sanpham-hdct/{id}")
    public String addSanPhamHDCT(@PathVariable("id") Integer id,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                                 @RequestParam(value = "chatLieuId", required = false) Integer chatLieuId,
                                 @RequestParam(value = "tayAoId", required = false) Integer tayAoId,
                                 @RequestParam(value = "coAoId", required = false) Integer coAoId,
                                 @RequestParam(value = "kichCoId", required = false) Integer kichCoId,
                                 @RequestParam(value = "mauSacId", required = false) Integer mauSacId,
                                 @RequestParam(value = "trangThai", required = false) Integer trangThai,
                                 Model model) {
        Page<SanPhamChiTiet> pageFind = sanPhamChiTietService.timKiemSanPham(keyword, thuongHieuId, chatLieuId, tayAoId,
                coAoId, kichCoId, mauSacId, trangThai, page, size);
        List<SanPhamChiTiet> ListSPCT = pageFind.getContent();
        model.addAttribute("ListSPCT", ListSPCT);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("thuongHieu", thuongHieuService.getAll());
        model.addAttribute("chatLieu", chatLieuService.getAll());
        model.addAttribute("tayAo", kieuTayAoService.getAll());
        model.addAttribute("coAo", kieuCoAoService.getAll());
        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("kichCoId", kichCoId);
        model.addAttribute("mauSacId", mauSacId);
        model.addAttribute("trangThai", trangThai);


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
                    model.addAttribute("tongTien", tongTien);
                    hoaDonChiTietRepository.save(hdct);
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

            if (sanPhamChiTiet.getSoLuong() > 0) {
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - 1);
                sanPhamChiTietRepository.save(sanPhamChiTiet); // Lưu lại số lượng đã cập nhật trước khi thêm vào hóa đơn

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
        }

        model.addAttribute("idHoaDon", idHoaDon);
        return "redirect:/admin/taiquay/sanpham#sanpham";
    }

    //TODO XOA SP
    @GetMapping("/xoa-sanpham-hdct/{id}")
    public String xoaSanPhamHDCT(@PathVariable("id") Integer id, Model model) {
        // Tìm sản phẩm chi tiết trong hóa đơn chi tiết theo ID
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm trong hóa đơn chi tiết"));
        // Tăng lại số lượng của sản phẩm chi tiết trong kho
        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        // Xóa sản phẩm khỏi hóa đơn chi tiết
        // Cập nhật tổng tiền trong hóa đơn sau khi xóa
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
        BigDecimal tongTien = hoaDonChiTietList.stream()
                .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("listHDCT", hoaDonChiTietList);
        model.addAttribute("idHoaDon", idHoaDon);
        hoaDonChiTietRepository.delete(hoaDonChiTiet);

        return "redirect:/admin/taiquay/detail/" + idHoaDon;
    }

    // TODO huyhd
    @PostMapping("/huyhoadon/{id}")
    public String huyHoaDon(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            hoaDonService.cancelHoaDon(id);
            idHoaDon = null;
            message = "Hóa đơn đã được hủy thành công.";
        } catch (IllegalArgumentException e) {
            message = "Không tìm thấy hóa đơn để hủy.";
        } catch (Exception e) {
            message = "Không thể hủy hóa đơn.";
        }
        // Thêm thông báo vào RedirectAttributes
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/taiquay"; // Chuyển hướng về trang chủ
    }

//    @DeleteMapping("/huyhoadon/{id}")
//    @ResponseBody
//    public ResponseEntity<?> huyHoaDon(@PathVariable("id") Integer id) {
//        try {
//            hoaDonService.cancelHoaDon(id);
//            idHoaDon = null;
//            return ResponseEntity.ok("Hóa đơn đã được hủy thành công.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn để hủy.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể hủy hóa đơn.");
//        }
//    }

    //TODO: Kiem Tra Phieu Giam Gia
    @GetMapping("/kiemtrama/{maphieugiamgia}")
    public String kiemTraMaGiamGia(@PathVariable("maphieugiamgia") String maPhieuGiamGiaInput,
                                   Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại."));
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
        System.out.println("Ma Phieu Giam Gia Input : " + maPhieuGiamGiaInput);
        try {
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGiaInput, tongTien);
            hoaDon.setTongTien(tongTienSauGiam);
            hoaDonRepository.save(hoaDon);
            maPhieuGiamGia = maPhieuGiamGiaInput;
//            model.addAttribute("tongTien", tongTien);
            model.addAttribute("hoaDon", hoaDon);
//            model.addAttribute("tongTienSauGiam", tongTienSauGiam);
            model.addAttribute("message", "Mã giảm giá hợp lệ!");
            model.addAttribute("maPhieuGiamGia", maPhieuGiamGia);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/taiquay/detail/" + idHoaDon;
    }

    @PostMapping("/kiemtrama")
    private String maGiamGiaPost(
            Model model, @RequestParam(name = "maphieugiamgia") String maPhieuGiamGia
    ) {
        try {
            return kiemTraMaGiamGia(maPhieuGiamGia, model);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
// TODO: ADD SP TO HDCT FOR QR
    @GetMapping("/add-sanpham-hdctqr/{id}")
    public ResponseEntity<Map<String, String>> addSanPhamHDCT(@PathVariable("id") String idRaw,
                                                              @RequestParam(value = "quantity") int quantity,
                                                              @RequestParam(value = "idHoaDon") Integer idHoaDon
                                                              ) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println("Received ID: " + idRaw);
            Integer id = Integer.parseInt(idRaw);
//            if (!id.equals("\\d+")) { // Kiểm tra nếu không phải số
//                response.put("error", "ID sản phẩm không hợp lệ.");
//                return ResponseEntity.badRequest().body(response);
//            }
            // Tìm sản phẩm chi tiết từ id
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Sản phẩm không tồn tại"));

            // Kiểm tra xem sản phẩm còn hàng không
            if (sanPhamChiTiet.getSoLuong() < quantity) {
                response.put("errorSoLuong", "Sản phẩm không đủ số lượng.");
                return ResponseEntity.badRequest().body(response);
            }

            // Cập nhật số lượng sản phẩm trong kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Tìm hóa đơn theo idHoaDon (cần lấy idHoaDon từ nơi khác)
            HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn"));

            // Kiểm tra nếu sản phẩm đã có trong hóa đơn, cộng dồn số lượng
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            boolean productExistsInBill = false;
            for (HoaDonChiTiet hdct : hoaDonChiTietList) {
                if (hdct.getSanPhamChiTiet().getId().equals(id)) {
                    hdct.setSoLuong(hdct.getSoLuong() + quantity);
                    hoaDonChiTietRepository.save(hdct);
                    productExistsInBill = true;
                    break;
                }
            }

            // Nếu sản phẩm chưa có trong hóa đơn, tạo mới
            if (!productExistsInBill) {
                HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                        .hoaDon(hoaDon)
                        .sanPhamChiTiet(sanPhamChiTiet)
                        .soLuong(quantity)
                        .build();
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }

            // Tính tổng tiền và trả về
            hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            response.put("tongTien", tongTien.toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
