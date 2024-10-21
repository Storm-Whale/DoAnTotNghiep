package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public TaiQuayController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, MauSacService mauSacService, KichCoService kichCoService, HoaDonService hoaDonService, KhachHangService khachHangService, PhieuGiamGiaService phieuGiamGiaService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.sanPhamService = sanPhamService;
        this.mauSacService = mauSacService;
        this.kichCoService = kichCoService;
        this.hoaDonService = hoaDonService;
        this.khachHangService = khachHangService;
        this.phieuGiamGiaService = phieuGiamGiaService;
    }

    @GetMapping("")
    public String showIndex(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "trangThai", required = false) Integer trangThai,
                            @RequestParam(name = "mauSacId", required = false) Integer mauSacId,
                            @RequestParam(name = "kichCoId", required = false) Integer kichCoId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            Model model){
        Page<SanPhamChiTietResponse> sanPhamChiTietResponses = sanPhamChiTietService.timKiemSanPham(keyword, trangThai,
                mauSacId, kichCoId, page, size);
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("sanPhamChiTiet", sanPhamChiTietResponses.getContent());
        model.addAttribute("sanPham", sanPhamService.getAllSanPham());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("listKH",khachHangService.getAll());
        model.addAttribute("listPGG",phieuGiamGiaService.getAll());
        return "/admin/BanhangTaiQuay/index";
    }
}
