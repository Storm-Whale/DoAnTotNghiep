package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class SanPhamController {

    private final SanPhamService sanPhamService;
    private final ThuongHieuService thuongHieuService;
    private final ChatLieuService chatLieuService;
    private final KieuCoAoService kieuCoAoService;
    private final KieuTayAoService kieuTayAoService;
    private final SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/index")
    public String index(Model model) {
        List<SanPhamResponse> products = sanPhamService.getAllSanPham();
        model.addAttribute("products", products);
        return "/admin/sanpham/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        addSanPhamModelAttributes(model, new SanPhamRequest());
        return "/admin/sanpham/create";
    }

    @GetMapping("/find_by_id/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        SanPhamResponse product = sanPhamService.getSanPhamById(id);
        model.addAttribute("product", product);
        model.addAttribute("spcts", sanPhamChiTietService.getSanPhamChiTietByIdSP(id));
        return "admin/sanpham/detail";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("product") SanPhamRequest productRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
            addSanPhamModelAttributes(model, productRequest);
            return "/admin/sanpham/create";
        }
        productRequest.setTrangThai(1);
        sanPhamService.storeSanPham(productRequest);
        return "redirect:/admin/products/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product_old", sanPhamService.getSanPhamById(id));
        addSanPhamModelAttributes(model, new SanPhamRequest());
        return "/admin/sanpham/update";
    }

    @PostMapping(value = "/update/{id}")
    public String update(
            @PathVariable(name = "id") Integer id, @RequestParam("ten_sp") String tenSP,
            @RequestParam("id_thuong_hieu") Integer idThuongHieu, @RequestParam("id_chat_lieu") Integer idChatLieu,
            @RequestParam("id_co_ao") Integer idCoAo, @RequestParam("id_tay_ao") Integer idTayAo,
            @RequestParam("trang_thai") Integer trangThai
    ) {
        SanPhamRequest sanPhamRequest = SanPhamRequest.builder()
                .tenSanPham(tenSP).idChatLieu(idChatLieu)
                .idThuongHieu(idThuongHieu).idCoAo(idCoAo)
                .idTayAo(idTayAo).trangThai(trangThai).build();
        sanPhamService.updateSanPham(id, sanPhamRequest);
        return "redirect:/admin/products/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        sanPhamService.sortDeleteSanPham(id);
        return "redirect:/admin/products/index";

    }

    private void addSanPhamModelAttributes(Model model, SanPhamRequest sanPhamRequest) {
        model.addAttribute("product", sanPhamRequest);
        model.addAttribute("thuongHieus", thuongHieuService.getAll());
        model.addAttribute("chatLieus", chatLieuService.getAll());
        model.addAttribute("kieuCoAos", kieuCoAoService.getAll());
        model.addAttribute("kieuTayAos", kieuTayAoService.getAll());
    }
}
