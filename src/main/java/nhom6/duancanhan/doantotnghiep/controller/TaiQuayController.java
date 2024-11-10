package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    // Khai báo ModelAttribute toàn cục
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        idHoaDon = 1; // Gán giá trị cho biến toàn cục
//        model.addAttribute("idHoaDon", idHoaDon); // Thêm vào model
//    }

    public TaiQuayController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, MauSacService mauSacService, KichCoService kichCoService, HoaDonService hoaDonService, KhachHangService khachHangService, PhieuGiamGiaService phieuGiamGiaService, ThuongHieuService thuongHieuService, ChatLieuService chatLieuService, KieuCoAoService kieuCoAoService, KieuTayAoService kieuTayAoService) {
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
    }

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
        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
//        model.addAttribute("firstHoaDon", firstHoaDon != null ? firstHoaDon : new HoaDon());
        // Tạo danh sách hóa đơn chờ và thêm hóa đơn đầu tiên vào danh sách nếu cần
        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
        // Kiểm tra xem hóa đơn đầu tiên đã có trong danh sách chưa
        if (firstHoaDon != null && !listHD.contains(firstHoaDon)) {
            listHD.add(0, firstHoaDon); // Thêm hóa đơn đầu tiên vào đầu danh sách
        }
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

        return "/admin/BanhangTaiQuay/index";
    }

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
                               HttpSession session
            , Model model) {
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

        HoaDon firstHoaDon = hoaDonRepository.findFirstByOrderByIdAsc();
//        model.addAttribute("firstHoaDon", firstHoaDon != null ? firstHoaDon : new HoaDon());
        // Tạo danh sách hóa đơn chờ và thêm hóa đơn đầu tiên vào danh sách nếu cần
        List<HoaDon> listHD = hoaDonRepository.findHoaDonsWithStatusOne();
        // Kiểm tra xem hóa đơn đầu tiên đã có trong danh sách chưa
        if (firstHoaDon != null && !listHD.contains(firstHoaDon)) {
            listHD.add(0, firstHoaDon); // Thêm hóa đơn đầu tiên vào đầu danh sách
        }
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
        return "/admin/BanhangTaiQuay/index";
    }
// TODO : khachhang
    @GetMapping("tim-kiem")
    public String timKiemKhachHang(@RequestParam(name = "soDienThoai", required = false) String soDienThoai, HttpSession session
            , RedirectAttributes redirectAttributes) {

        KhachHang khachHang = khachHangService.findBySoDienThoaiKhachHang(soDienThoai);
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + idHoaDon));
        // Cập nhật khách hàng cho hóa đơn
        if (hoaDon != null || hoaDon.getKhachHang() != null) {
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTrangThai(1);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("hoaDon", hoaDon);
            redirectAttributes.addFlashAttribute("khachHang", khachHang);
        }else {
            System.out.println("bug ơi");
        }
        // Tạo ModelAndView để chuyển dữ liệu sang view Thymeleaf
        return "redirect:/admin/taiquay/detail/" + idHoaDon; // Tên view Thymeleaf để hiển thị kết quả
    }

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    //TODO THANH TOAN
    @GetMapping("/thanhtoan")
    public String thanhtoan(
            @RequestParam("ghiChu") String ghiChu,
            @RequestParam("phuongThucThanhToan") Integer phuongThucId,
            @RequestParam(value = "maPhieuGiamGia", required = false) String maPhieuGiamGiaInput
            , Model model) {
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
        return "redirect:/admin/hoadon";
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
    @DeleteMapping("/huyhoadon/{id}")
    @ResponseBody
    public ResponseEntity<?> huyHoaDon(@PathVariable("id") Integer id) {
        try {
            hoaDonService.cancelHoaDon(id);
            return ResponseEntity.ok("Hóa đơn đã được hủy thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn để hủy.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể hủy hóa đơn.");
        }
    }
//    @GetMapping("/xoahd/{id}")
//    public String xoaHD(@PathVariable("id") Integer id){
////        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElse(null);
//        hoaDonService.deleteHoaDon(id);
//        return "redirect:/admin/taiquay";
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
}
