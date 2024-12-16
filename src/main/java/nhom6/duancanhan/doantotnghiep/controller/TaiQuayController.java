package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.entity.PhuongThucThanhToan;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.entity.VaiTro;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
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
    private final ForgotPasswordService forgotPasswordService;

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
    @Autowired
    HttpSession session;

    @Autowired
    private DiaChiService diaChiService;

    private Integer idHoaDon = 1;

    private String maPhieuGiamGia = "";

    public TaiQuayController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, MauSacService mauSacService, KichCoService kichCoService, HoaDonService hoaDonService, KhachHangService khachHangService, PhieuGiamGiaService phieuGiamGiaService, ThuongHieuService thuongHieuService, ChatLieuService chatLieuService, KieuCoAoService kieuCoAoService, KieuTayAoService kieuTayAoService, InvoidPdfService invoidPdfService, ForgotPasswordService forgotPasswordService) {
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
        this.forgotPasswordService = forgotPasswordService;
    }

    // TODO: SHOWINDEX
    @GetMapping("")
    public String showIndex(
            @RequestParam(value = "page", defaultValue = "0") int page,
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
            @RequestParam(name = "soDienThoai", required = false) String soDienThoai,
            @SessionAttribute(value = "nhanvien", required = false) NhanVien nhanVien
            , RedirectAttributes redirectAttributes, Model model
    ) {
        boolean isLoggedIn = session != null &&
                session.getAttribute("loginStatus") != null &&
                (Boolean) session.getAttribute("loginStatus");
        if (!isLoggedIn) {
            model.addAttribute("nhanvien", null);
        } else {
            model.addAttribute("nhanvien", nhanVien);
        }
        ///
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("sanPhamChiTiet", sanPhamChiTietService.getAllSanPhamChiTiet());
        model.addAttribute("sanPham", sanPhamService.getAllSanPham());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("thuongHieu", thuongHieuService.getAll());
        model.addAttribute("chatLieu", chatLieuService.getAll());
        model.addAttribute("tayAo", kieuTayAoService.getAll());
        model.addAttribute("coAo", kieuCoAoService.getAll());

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

        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();

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
        model.addAttribute("khachHangThemNhanh", new KhachHang());

        // Thêm vào model
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
        model.addAttribute("hoaDon", hoaDon);

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
        model.addAttribute("khachHangThemNhanh", new KhachHang());
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
        model.addAttribute("hoaDon", hoaDon);
        DiaChi diaChiObj = hoaDon.getDiaChi();
        if (diaChiObj == null) {
            diaChiObj = new DiaChi(); // Tạo đối tượng DiaChi mới nếu chưa tồn tại
            hoaDon.setDiaChi(diaChiObj); // Cập nhật địa chỉ vào hóa đơn nếu cần lưu sau này
        }
        // Gán thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("diaChi", diaChiObj);
        return "/admin/BanhangTaiQuay/index";
    }

    //TODO: TẠO HÓA ĐƠN
    @PostMapping("/taohoadon")
    @ResponseBody
    public ResponseEntity<?> taoHoaDon(Model model) {
        NhanVien nhanVien = (NhanVien) session.getAttribute("nhanvien");
//        if (nhanVien.getTaiKhoan().getVaiTro() == null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Bạn chưa đăng nhập, vui lòng đăng nhập để tạo hóa đơn!");
//        }
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(1); // Set trạng thái mới tạo
        hoaDon.setLoaiHoaDon("Tai quay");
        hoaDon.setNguoiTao(nhanVien);
        hoaDonRepository.save(hoaDon); // Lưu vào cơ sở dữ liệu
        return ResponseEntity.ok(hoaDon);
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

        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();

        model.addAttribute("listHD", listHD.stream().limit(5).collect(Collectors.toList())); // Giới hạn danh sách về 5 hóa đơn

        BigDecimal tongTien = hoaDonChiTiet.stream()
                .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        reset phiếu giảm giá
        if (hoaDon != null && hoaDon.getPhieuGiamGia() != null) {
            // Nếu muốn reset hoàn toàn
            hoaDon.setPhieuGiamGia(null);
            hoaDon.setTongTien(tongTien);
            hoaDonRepository.save(hoaDon);
        }
        // Tìm phiếu giảm giá phù hợp
        PhieuGiamGia phieuGiamGiaAuto = timPhieuGiamGiaHopLe(tongTien);

        BigDecimal lastTongTien = tongTien;
        if (phieuGiamGiaAuto != null) {
            // Tự động áp dụng phiếu giảm giá
            maPhieuGiamGia = phieuGiamGiaAuto.getMaPhieuGiamGia();
            lastTongTien = phieuGiamGiaService.applyDiscount(maPhieuGiamGia, tongTien);

            System.out.println("Tự động áp dụng PGG: " + maPhieuGiamGia);
            System.out.println("Tổng tiền sau giảm: " + lastTongTien);

            model.addAttribute("maPhieuGiamGia", maPhieuGiamGia);
        }

        model.addAttribute("tongTien", tongTien);
        model.addAttribute("idHoaDon", idHoaDon);
        model.addAttribute("tongTienSauGiam", lastTongTien);
        model.addAttribute("khachHangThemNhanh", new KhachHang());
        DiaChi diaChiObj = hoaDon.getDiaChi();
        if (diaChiObj == null) {
            diaChiObj = new DiaChi(); // Tạo đối tượng DiaChi mới nếu chưa tồn tại
            hoaDon.setDiaChi(diaChiObj); // Cập nhật địa chỉ vào hóa đơn nếu cần lưu sau này
        }
        // Gán thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("diaChi", diaChiObj);
        return "/admin/BanhangTaiQuay/index";
    }

    // TODO : timkiemkhachhang
    @GetMapping("tim-kiem")
    public String timKiemKhachHang(@RequestParam(name = "soDienThoai", required = false) String soDienThoai, HttpSession session
            , RedirectAttributes redirectAttributes, Model model) {


        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));

        boolean hasProducts = hoaDon.getHoaDonChiTietList() != null && !hoaDon.getHoaDonChiTietList().isEmpty();

        // Tìm khách hàng theo số điện thoại
        KhachHang khachHang = khachHangService.findBySoDienThoaiKhachHang(soDienThoai);

        // Nếu hóa đơn đã có sản phẩm, không cho phép thêm khách hàng mới
        if (hasProducts) {
            if (hoaDon.getKhachHang() != null) {
                // Nếu đã có khách hàng, hiển thị thông báo
                redirectAttributes.addFlashAttribute("errorMessage", "Đã có khách hàng trong hóa đơn");
                return "redirect:/admin/taiquay/detail/" + idHoaDon; // Quay lại trang chi tiết hóa đơn
            }
        }

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

        if (hoaDon.getKhachHang() == null) {
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTrangThai(1); // Trạng thái hóa đơn có thể thay đổi theo yêu cầu của bạn
            hoaDonRepository.save(hoaDon);

            redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
            redirectAttributes.addFlashAttribute("khachHang", khachHang);
        }



        // Cập nhật khách hàng cho hóa đơn
        if (hoaDon != null || hoaDon.getKhachHang() != null) {
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTrangThai(1);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
            redirectAttributes.addFlashAttribute("khachHang", khachHang);

            if (hoaDon.getDiaChi() != null) {
                model.addAttribute("diaChi", hoaDon.getDiaChi().getDiaChiChiTiet());
            } else {
                model.addAttribute("diaChi", "Địa chỉ chưa được cập nhật");
            }
        }
        model.addAttribute("listhoadondiachi", hoaDon);
        // Xử lý địa chỉ: bỏ qua nếu chưa có
        DiaChi diaChiObj = hoaDon.getDiaChi();
        if (diaChiObj == null) {
            diaChiObj = new DiaChi(); // Đối tượng rỗng để không bị lỗi
        }
        // Gán thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("diaChi", diaChiObj);
        return "redirect:/admin/taiquay/detail/" + idHoaDon; // Tên view Thymeleaf để hiển thị kết quả
    }

    @Autowired
    DiaChiRepository diaChiRepository;

    // TODO: DIACHI
    @PostMapping("/cap-nhat-hoa-don")
    public String capNhatHoaDon(
            @RequestParam Integer idHoaDon,
            @RequestParam String diaChi,
            @RequestParam(value = "soDienThoai", required = false) String soDienThoai,
            @RequestParam(value = "tenNguoiNhan", required = false) String tenNguoiNhan,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            Model model
    ) {

        // Tìm hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
        DiaChi diaChiObj = null;
        KhachHang khachHang = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang() : null;

        String soDienThoaiSuDung = (soDienThoai != null) ? soDienThoai : (khachHang != null ? khachHang.getSoDienThoai() : null);

        if (khachHang != null) {
            diaChiObj = diaChiService.newDiaChi(diaChi, tenNguoiNhan, soDienThoaiSuDung, khachHang.getId());
        } else {
            diaChiObj = diaChiService.newDiaChi(diaChi, tenNguoiNhan, soDienThoai, null);
        }



        hoaDon.setDiaChi(diaChiObj);
        hoaDon.setGhiChu(ghiChu);
        hoaDonRepository.save(hoaDon);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("diaChi", diaChiObj);
        return "redirect:/admin/taiquay/detail/" + idHoaDon;
    }

    // TODO:themnhanhkhachhang

    @GetMapping("/themnhanhkhachhang")
    public String addKhachHnagTaiQuay(@ModelAttribute("khachHangThemNhanh") KhachHang khachHang, Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
        DiaChi diaChiObj = hoaDon.getDiaChi();
        if (diaChiObj == null) {
            diaChiObj = new DiaChi(); // Tạo đối tượng DiaChi mới nếu chưa tồn tại
            hoaDon.setDiaChi(diaChiObj); // Cập nhật địa chỉ vào hóa đơn nếu cần lưu sau này
        }
        // Gán thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("diaChi", diaChiObj);
        return "/admin/BanhangTaiQuay/addNhanhKhachHang";
    }
    @Autowired
    VaiTroService vaiTroService;
    @Autowired
    GioHangService gioHangService;
    @Autowired
    TaiKhoanService taiKhoanService;
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachHangThemNhanh") KhachHang khachHang,
                      BindingResult result, Model model,
                      RedirectAttributes redirectAttributes) {


        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));

//        // Kiểm tra tên khách hàng
//        if (khachHang.getTen() != null && !khachHang.getTen().matches("^[a-zA-Z ]+$")) {
//            result.rejectValue("ten", "error.khachHang", "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng.");
//        }        // Tạo mới tài khoản
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(khachHang.getSoDienThoai());
        taiKhoan.setMatKhau(khachHang.getSoDienThoai());
        taiKhoan.setTrangThai(1);
        VaiTro vaiTro = vaiTroService.findById(3);
        if (vaiTro != null) {
            taiKhoan.setVaiTro(vaiTro);
        } else {
            model.addAttribute("error", "Vai trò không tồn tại.");
          return "/admin/BanhangTaiQuay/addNhanhKhachHang";
        }
        taiKhoanService.addTaiKhoan(taiKhoan);
        khachHang.setTaiKhoan(taiKhoan);

        String email = khachHang.getEmail();

        // Kiểm tra email không phải là chuỗi rỗng
        if (email != null && !email.isEmpty()) {
            // Kiểm tra email bắt đầu bằng chữ thường
//            if (!Character.isLowerCase(email.charAt(0))) {
//                result.rejectValue("email", "error.khachHang", "Email phải bắt đầu bằng chữ thường.");
//            }

            // Kiểm tra email đã tồn tại
            if (khachHangService.isEmailExist(email)) {
                result.rejectValue("email", "error.khachHangThemNhanh", "Email đã tồn tại.");
            }
        }
// SoDienThoai
        if (khachHangService.isSoDienThoaiExist(khachHang.getSoDienThoai())) {
            result.rejectValue("soDienThoai", "error.khachHangThemNhanh", "Số điện thoại đã tồn tại.");
        }

        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/BanhangTaiQuay/addNhanhKhachHang";
        }
        // Xử lý tệp tải lên
        MultipartFile anhUrlFile = khachHang.getAnhUrlFile();
        if (anhUrlFile == null || anhUrlFile.isEmpty()) {
            khachHang.setAnhUrl("u.png");
        } else {
            // Kiểm tra file ảnh hợp lệ
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.khachHang", "Tệp tải lên không phải là hình ảnh.");
            } else {
                try {
                    // Tạo tên file duy nhất bằng UUID
                    String fileName = UUID.randomUUID() + "_" + anhUrlFile.getOriginalFilename();
                    // Đường dẫn thư mục lưu file (ngoài thư mục dự án)
                    String uploadDir = System.getProperty("user.dir") + "/upload/";
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists()) {
                        uploadFolder.mkdirs();
                    }

                    File uploadFile = new File(uploadDir + fileName);
                    anhUrlFile.transferTo(uploadFile);

                    khachHang.setAnhUrl(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                    return "/admin/BanhangTaiQuay/addNhanhKhachHang";
                }
            }
        }

        khachHangService.addKhachHang(khachHang);
        gioHangService.addGioHang(khachHang);

        // Cập nhật khách hàng cho hóa đơn
        if (hoaDon != null || hoaDon.getKhachHang() != null) {
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTrangThai(1);
            khachHang.setTrangThai(1);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
            redirectAttributes.addFlashAttribute("khachHang", khachHang);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được thêm thành công!");
        return "redirect:/admin/taiquay/detail/" + idHoaDon;
    }

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;


    private PhieuGiamGia timPhieuGiamGiaHopLe(BigDecimal tongTien) {
        Date now = new Date(); // Lấy thời gian hiện tại
        return phieuGiamGiaRepository.findAll().stream()
                .filter(pgg ->
                        pgg.getTrangThai() == 1 &&
                                pgg.getSoLuong() > 0 &&
                                pgg.getDieuKien().compareTo(tongTien) <= 0 &&
                                pgg.getNgayBatDau().before(now) && // So sánh với ngày hiện tại
                                pgg.getNgayKetThuc().after(now) // So sánh với ngày hiện tại
                )
                .max(Comparator.comparing(PhieuGiamGia::getDieuKien))
                .orElse(null);
    }



    //TODO THANH TOAN
    @GetMapping("/thanhtoan")
    @ResponseBody
    public String thanhtoan(
            @RequestParam(value = "phuongThucThanhToan", required = false) Integer phuongThucId,
            @RequestParam(value = "maPhieuGiamGia", required = false) String maPhieuGiamGiaInput,
            @RequestParam(value = "themKhachHang", required = false) Boolean themKhachHang // Thêm tham số này
            , Model model) throws IOException {
        // Lấy hóa đơn hiện tại
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại."));
        // kiểm tra đã có khách hàng chưa
        if (hoaDon.getKhachHang() == null) {
            if (themKhachHang == null || !themKhachHang) {
                // Nếu không có khách hàng và người dùng không muốn thêm khách hàng
                // Thiết lập khách hàng mặc định là khách lẻ
                KhachHang khachHang = new KhachHang();
                hoaDon.setKhachHang(khachHang);
            }
        }
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            // Kiểm tra nếu chưa có sản phẩm trong hóa đơn
            if (hoaDonChiTietList.isEmpty()) {
                return "Hóa đơn chưa có sản phẩm. Vui lòng thêm sản phẩm trước khi thanh toán.";
            }
            // Tính tổng tiền ban đầu
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Biến lưu tổng tiền sau giảm
            BigDecimal tongTienSauGiam = tongTien;

            // Nếu không có `tongTienSauGiam`, không cho phép thanh toán
            if (tongTienSauGiam.compareTo(BigDecimal.ZERO) <= 0) {
                return "Không thể thanh toán vì tổng tiền không hợp lệ.";
            }
            // Nếu không có mã nhập, tự động tìm phiếu giảm giá
//        PhieuGiamGia phieuGiamGiaAuto = timPhieuGiamGiaHopLe(tongTien);
            PhieuGiamGia phieuGiamGiaApdung = null;
            // Kiểm tra điều kiện áp dụng phiếu giảm giá
            boolean apDungPgg = false;
            if (maPhieuGiamGiaInput != null && !maPhieuGiamGiaInput.isEmpty()) {
                try {
                    // Tìm phiếu giảm giá theo mã nhập
                    PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGiaInput)
                            .orElseThrow(() -> new IllegalArgumentException("Mã phiếu giảm giá không hợp lệ."));
                    // Áp dụng phiếu giảm giá
                    tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGiaInput, tongTien);
                    // Cập nhật thông tin phiếu giảm giá
                    phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
                    phieuGiamGiaRepository.save(phieuGiamGia);
                    // Lưu phiếu giảm giá vào hóa đơn
//                hoaDon.setPhieuGiamGia(phieuGiamGia);
                    phieuGiamGiaApdung = phieuGiamGia;
                    apDungPgg = true;
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", e.getMessage());
                    return "redirect:/admin/taiquay/detail/" + idHoaDon;
                }
            }// Nếu không có mã nhập, tự động tìm phiếu giảm giá
            else {
                PhieuGiamGia phieuGiamGiaAuto = timPhieuGiamGiaHopLe(tongTien);
                // Tìm phiếu giảm giá phù hợp nhất
                if (phieuGiamGiaAuto != null) {
                    // Áp dụng phiếu giảm giá tự động
                    tongTienSauGiam = phieuGiamGiaService.applyDiscount(
                            phieuGiamGiaAuto.getMaPhieuGiamGia(),
                            tongTien
                    );
                    // Cập nhật thông tin phiếu giảm giá
                    phieuGiamGiaAuto.setSoLuong(phieuGiamGiaAuto.getSoLuong() - 1);
                    phieuGiamGiaRepository.save(phieuGiamGiaAuto);
                    // Lưu phiếu giảm giá vào hóa đơn
//                hoaDon.setPhieuGiamGia(phieuGiamGiaAuto);
                    phieuGiamGiaApdung = phieuGiamGiaAuto;
                    apDungPgg = true;
                } else {
                    System.out.println("Không tìm thấy phiếu giảm giá phù hợp");
                }
            }
            // Nếu có phiếu giảm giá được áp dụng
            if (apDungPgg) {
                hoaDon.setPhieuGiamGia(phieuGiamGiaApdung);
                hoaDon.setTongTien(tongTienSauGiam);
            } else {
                // Nếu không có phiếu giảm giá, giữ nguyên tổng tiền ban đầu
                hoaDon.setPhieuGiamGia(null);
                hoaDon.setTongTien(tongTien);
            }
            PhuongThucThanhToan phuongThuc = phuongThucThanhToanRepository.findById(phuongThucId).orElse(null);
            hoaDon.setPhuongThucThanhToan(phuongThuc);
            if (hoaDon.getKhachHang() != null) {
                hoaDon.setTrangThai(1); // Chờ xác nhận
            } else {
                hoaDon.setTrangThai(6); // Đã thanh toán
            }
            if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getEmail() != null){
                forgotPasswordService.sendHoaDon(hoaDon.getKhachHang().getEmail(), hoaDon.getId());
            }
            hoaDonRepository.save(hoaDon);

            byte[] pdfData = invoidPdfService.generateInvoicePdf(hoaDon);

            String fileName = "invoice_" + hoaDon.getId() + ".pdf";
            Path path = Paths.get("D://DoAnTotNghiep//DoAnTotNghiep//upload/" + fileName);
            Files.write(path, pdfData);
//        D://FALL_2024//DATN//DoAnTotNghiep//upload/
            // Return the file URL to the frontend
            String fileUrl = "D://DoAnTotNghiep//DoAnTotNghiep//upload/" + fileName;
            model.addAttribute("pdfUrl", fileUrl);
            return "/uploads/" + fileName;
        }


        //TODO: xac nhan don hang
        @GetMapping("/xac-nhan-don-hang")
        public ResponseEntity<String> xacNhanDonHang(@RequestParam("hoaDonId") Integer hoaDonId) {
            try {
                HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                        .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
                // Kiểm tra trạng thái hóa đơn
                if (hoaDon.getTrangThai() == 4) {
                    hoaDon.setTrangThai(6);
                    hoaDonRepository.save(hoaDon);
                    return ResponseEntity.ok("Xác nhận đơn hàng thành công! Cảm ơn quý khách");
                } else if (hoaDon.getTrangThai() == 6) {
                    return ResponseEntity.badRequest().body("Đơn hàng đã được xác nhận trước đó.");
                } else {
                    return ResponseEntity.badRequest().body("Hiện chưa thể xác nhận đơn hàng!");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Có lỗi xảy ra: " + e.getMessage());
            }
        }
    @GetMapping("/xac-nhan")
    public String xacNhanView(@RequestParam("hoaDonId") Integer hoaDonId, Model model) {
        model.addAttribute("hoaDonId", hoaDonId);
        return "/admin/xac-nhan-don-hang";
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
                                 @RequestParam(value = "soLuong", defaultValue = "1") Integer soLuong,
                                 RedirectAttributes redirectAttributes,
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
//validate sp tồn kho
        if (sanPhamChiTiet.getSoLuong() < soLuong) {
//            redirectAttributes.addFlashAttribute("errorSoLuong", "Số lượng sản phẩm không đủ. Chỉ còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm.");
            return "redirect:/admin/taiquay/sanpham#sanpham"; // Quay lại trang sản phẩm
        }
        if (!checkCongDon) {
            HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn"));

            // Kiểm tra số lượng yêu cầu
            if (sanPhamChiTiet.getSoLuong() >= soLuong) {
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                sanPhamChiTietRepository.save(sanPhamChiTiet);

                HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                        .hoaDon(hoaDon)
                        .sanPhamChiTiet(sanPhamChiTiet)
                        .soLuong(soLuong)
                        .build();
                hoaDonChiTietRepository.save(hoaDonChiTiet);

                hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
                BigDecimal tongTien = hoaDonChiTietList.stream()
                        .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // Tìm phiếu giảm giá phù hợp
                PhieuGiamGia phieuGiamGiaAuto = timPhieuGiamGiaHopLe(tongTien);

                if (phieuGiamGiaAuto != null) {
                    // Tự động áp dụng phiếu giảm giá
                    maPhieuGiamGia = phieuGiamGiaAuto.getMaPhieuGiamGia();
                    BigDecimal tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGia, tongTien);

                    redirectAttributes.addFlashAttribute("maPhieuGiamGia", maPhieuGiamGia);
                    redirectAttributes.addFlashAttribute("tongTienSauGiam", tongTienSauGiam);
                }
                model.addAttribute("tongTien", tongTien);
                model.addAttribute("listHDCT", hoaDonChiTietList);
            } else {
                // Thêm thông báo lỗi nếu số lượng không đủ
                model.addAttribute("errorSoLuong", "Số lượng sản phẩm không đủ. Chỉ còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm.");
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!"); // Thêm thông báo thành công
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

    //TODO: HUY HD
    @PostMapping("/huyhoadon/{id}")
    public ResponseEntity<?> huyHoaDon(@PathVariable("id") Integer id) {
        try {
            // Kiểm tra ID
            if (id == null) {
                return ResponseEntity.badRequest().body("ID hóa đơn không hợp lệ");
            }
            hoaDonService.cancelHoaDon(id);
            idHoaDon = 1;
            return ResponseEntity.ok("Hóa đơn đã được hủy thành công");
        } catch (IllegalArgumentException e) {
            // Ghi log thông tin chi tiết
            System.err.println("Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể hủy hóa đơn");
        }
    }


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
            // Nếu đã có phiếu giảm giá cũ, hoàn lại số lượng
            if (hoaDon.getPhieuGiamGia() != null) {
                PhieuGiamGia phieuGiamGiaCu = hoaDon.getPhieuGiamGia();
                phieuGiamGiaCu.setSoLuong(phieuGiamGiaCu.getSoLuong() + 1);
                phieuGiamGiaRepository.save(phieuGiamGiaCu);
            }
            // Áp dụng phiếu giảm giá mới
            BigDecimal tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGiaInput, tongTien);
//            hoaDon.setTongTien(tongTienSauGiam);
            //update so luong pgg trong db
            PhieuGiamGia phieuGiamGiaMoi = phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGiaInput)
                    .orElseThrow(() -> new IllegalArgumentException("Mã giảm giá không hợp lệ"));
            //
            phieuGiamGiaMoi.setSoLuong(phieuGiamGiaMoi.getSoLuong() - 1);  // Giảm đi 1
            phieuGiamGiaRepository.save(phieuGiamGiaMoi);
            // luu thong tin hoa don
            hoaDon.setPhieuGiamGia(phieuGiamGiaMoi);
            hoaDon.setTongTien(tongTienSauGiam);
            hoaDonRepository.save(hoaDon);
            //cập nhật lại maPhieuGG
            this.maPhieuGiamGia = maPhieuGiamGiaInput;
            model.addAttribute("hoaDon", hoaDon);
            model.addAttribute("message", "Mã giảm giá hợp lệ!");
            model.addAttribute("maPhieuGiamGia", maPhieuGiamGiaInput);
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

    //TODO HỦY DISCOUNT
    @GetMapping("/huy-phieu-giam-gia/{idHoaDon}")
    @ResponseBody
    public ResponseEntity<?> huyPhieuGiamGia(@PathVariable("idHoaDon") Integer idHoaDon) {
        try {
            // Định dạng tiền tệ
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            // Tìm hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn"));
            // Lấy chi tiết hóa đơn
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            // Tính tổng tiền ban đầu (không áp dụng giảm giá)
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Nếu hóa đơn có phiếu giảm giá
            if (hoaDon.getPhieuGiamGia() != null) {
                // Trả lại số lượng cho phiếu giảm giá
                PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia();
                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() + 1);
                phieuGiamGiaRepository.save(phieuGiamGia);
                // Xóa phiếu giảm giá khỏi hóa đơn
//                hoaDon.setPhieuGiamGia(null);
                hoaDon.setTongTien(tongTien);
                hoaDonRepository.save(hoaDon);
                this.maPhieuGiamGia = "";
            }
            // Chuẩn bị response
            Map<String, Object> response = new HashMap<>();
            response.put("tongTien", currencyFormat.format(tongTien));
            response.put("tongTienSauGiam", currencyFormat.format(tongTien));
            response.put("maPhieuGiamGia", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    // TODO: ADD SP TO HDCT FOR QR
    @GetMapping("/add-sanpham-hdctqr/{id}")
    @ResponseBody
    public ResponseEntity<?> addSanPhamHDCTQR(
            @PathVariable("id") String idRaw,
            @RequestParam(value = "quantity") int quantity,
            @RequestParam(value = "idHoaDon") Integer idHoaDon
    ) {
        try {
            // Định dạng tiền tệ
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            // Chuyển đổi ID
            Integer id = Integer.parseInt(idRaw);
            // Tìm sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Sản phẩm không tồn tại"));
            // Kiểm tra số lượng tồn kho
            if (sanPhamChiTiet.getSoLuong() < quantity) {
                return ResponseEntity.badRequest().body(Map.of(
                        "errorSoLuong", "Sản phẩm không đủ số lượng. Chỉ còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm. Vui lòng nhập lại số lượng"
                ));
            }
            // Tìm hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn"));
            // Tìm chi tiết hóa đơn hiện tại
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            // Kiểm tra xem sản phẩm đã tồn tại trong hóa đơn chưa
            Optional<HoaDonChiTiet> existingHoaDonChiTiet = hoaDonChiTietList.stream()
                    .filter(hdct -> hdct.getSanPhamChiTiet().getId().equals(id))
                    .findFirst();

            HoaDonChiTiet hoaDonChiTiet;
            if (existingHoaDonChiTiet.isPresent()) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                hoaDonChiTiet = existingHoaDonChiTiet.get();
                hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + quantity);
            } else {
                // Nếu sản phẩm chưa tồn tại, tạo mới
                hoaDonChiTiet = HoaDonChiTiet.builder()
                        .hoaDon(hoaDon)
                        .sanPhamChiTiet(sanPhamChiTiet)
                        .soLuong(quantity)
                        .build();
            }
            // Lưu chi tiết hóa đơn
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            // Cập nhật số lượng sản phẩm trong kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            // Tính tổng tiền
            BigDecimal tongTien = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon).stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Tự động tìm phiếu giảm giá
            PhieuGiamGia phieuGiamGiaAuto = timPhieuGiamGiaHopLe(tongTien);
            // Biến lưu tổng tiền sau giảm
            BigDecimal tongTienSauGiam = tongTien;
            String maPhieuGiamGia = null;
            // Nếu có phiếu giảm giá phù hợp
            if (phieuGiamGiaAuto != null) {
                maPhieuGiamGia = phieuGiamGiaAuto.getMaPhieuGiamGia();
                tongTienSauGiam = phieuGiamGiaService.applyDiscount(maPhieuGiamGia, tongTien);
            }
            // Lấy danh sách chi tiết hóa đơn mới nhất
            List<HoaDonChiTiet> updatedHoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            // Chuẩn bị dữ liệu trả về
            // Trong controller
            Map<String, Object> response = new HashMap<>();
            response.put("tongTien", currencyFormat.format(tongTien));
            response.put("tongTienSauGiam", currencyFormat.format(tongTienSauGiam));
            response.put("maPhieuGiamGia", maPhieuGiamGia);

            List<Map<String, Object>> listHDCT = updatedHoaDonChiTietList.stream()
                    .map(hdct -> {
                        Map<String, Object> hdctMap = new HashMap<>();
                        hdctMap.put("id", hdct.getId());

                        Map<String, Object> sanPhamChiTietMap = new HashMap<>();
                        sanPhamChiTietMap.put("id", hdct.getSanPhamChiTiet().getId());

                        Map<String, Object> sanPhamMap = new HashMap<>();
                        sanPhamMap.put("tenSanPham", hdct.getSanPhamChiTiet().getSanPham().getTenSanPham());

                        sanPhamChiTietMap.put("sanPham", sanPhamMap);
                        sanPhamChiTietMap.put("gia", hdct.getSanPhamChiTiet().getGia());

                        hdctMap.put("sanPhamChiTiet", sanPhamChiTietMap);
                        hdctMap.put("soLuong", hdct.getSoLuong());

                        return hdctMap;
                    })
                    .collect(Collectors.toList());

            response.put("listHDCT", listHDCT);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    //TODO UPDATE SP CART
    @PostMapping("/update-sanpham-hdct/{id}")
    public ResponseEntity<Map<String, Object>> updateSanPhamHDCT(
            @PathVariable("id") Integer id,
            @RequestParam("delta") int delta) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Tìm sản phẩm trong hóa đơn chi tiết
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm trong hóa đơn chi tiết"));
            // Tính số lượng mới
            int newQuantity = hoaDonChiTiet.getSoLuong() + delta;
            // Kiểm tra số lượng mới
            if (newQuantity < 1) {
                response.put("error", "Số lượng không thể nhỏ hơn 1.");
                return ResponseEntity.badRequest().body(response);
            }
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            // Kiểm tra số lượng tồn kho
            if (delta < 0 && (sanPhamChiTiet.getSoLuong() + delta < 0)) {
                response.put("error", "Không đủ số lượng trong kho. Chỉ còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm.");
                return ResponseEntity.badRequest().body(response);
            }
            // Cập nhật số lượng
            hoaDonChiTiet.setSoLuong(newQuantity);
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - delta);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            // Tính tổng tiền và cập nhật danh sách
            Integer idHoaDon = hoaDonChiTiet.getHoaDon().getId();
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);
            BigDecimal tongTien = hoaDonChiTietList.stream()
                    .map(h -> h.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(h.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Chuẩn bị danh sách chi tiết để trả về
            List<Map<String, Object>> listHDCT = hoaDonChiTietList.stream()
                    .map(hdct -> {
                        Map<String, Object> hdctMap = new HashMap<>();
                        hdctMap.put("id", hdct.getId());
                        hdctMap.put("tongTien", NumberFormat.getInstance(new Locale("vi", "VN"))
                                .format(hdct.getSanPhamChiTiet().getGia().multiply(BigDecimal.valueOf(hdct.getSoLuong()))));
                        return hdctMap;
                    })
                    .collect(Collectors.toList());
            // Thêm số lượng tồn kho vào response
            response.put("stockQuantity", sanPhamChiTiet.getSoLuong());
            response.put("listHDCT", listHDCT);
            response.put("tongTien", NumberFormat.getInstance(new Locale("vi", "VN")).format(tongTien));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
