package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private Integer idHoaDon;

    // Khai báo ModelAttribute toàn cục
    @ModelAttribute
    public void addAttributes(Model model) {
        idHoaDon = 1; // Gán giá trị cho biến toàn cục
        model.addAttribute("idHoaDon", idHoaDon); // Thêm vào model
    }

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
        model.addAttribute("listHDCT", hoaDonChiTietList);
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
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("idHoaDon", idHoaDon); // Thêm vào model
        return "/admin/BanhangTaiQuay/index";
    }

    @GetMapping("khachhang")
    public String khachhang(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null; // Đặt thành null để khớp với logic xử lý trong service
            }
        }
        if (page < 0) {
            page = 0;
        }
        Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listKH.getTotalPages();
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0; // Go to the last page if out of bounds or reset to 0 if no pages exist
            listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size); // Fetch the last page data
        }
        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("listKH", listKH);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageNumbers", pageNumbers);
        return "/admin/BanhangTaiQuay/index";
    }

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
        return "/admin/BanhangTaiQuay/index";
    }



}
