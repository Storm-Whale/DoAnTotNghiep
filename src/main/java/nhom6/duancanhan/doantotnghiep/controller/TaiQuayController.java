package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.service.service.*;
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

    public TaiQuayController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, MauSacService mauSacService, KichCoService kichCoService, HoaDonService hoaDonService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.sanPhamService = sanPhamService;
        this.mauSacService = mauSacService;
        this.kichCoService = kichCoService;
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("")
    public String showIndex(Model model){
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("sanPhamChiTiet", sanPhamChiTietService.getAllSanPhamChiTiet());
        model.addAttribute("sanPham", sanPhamService.getAllSanPham());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        return "/admin/BanhangTaiQuay/index";
    }
}
