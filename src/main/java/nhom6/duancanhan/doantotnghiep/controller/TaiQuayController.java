package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showIndex(Model model){
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("sanPhamChiTiet", sanPhamChiTietService.getAllSanPhamChiTiet());
        model.addAttribute("sanPham", sanPhamService.getAllSanPham());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("listKH",khachHangService.getAll());
        model.addAttribute("listPGG",phieuGiamGiaService.getAll());
        return "/admin/BanhangTaiQuay/index";
    }

//    @GetMapping("khachhang")
//    public String khachhang(Model model){
//        model.addAttribute("listKH",khachHangService.getAll());
//        return "/admin/BanhangTaiQuay/index";
//    }
//
//    @GetMapping("phieugiamgia")
//    public String phieuGiamGia(Model model){
//        model.addAttribute("listPGG",phieuGiamGiaService.getAll());
//        return "/admin/BanhangTaiQuay/index";
//    }
}
