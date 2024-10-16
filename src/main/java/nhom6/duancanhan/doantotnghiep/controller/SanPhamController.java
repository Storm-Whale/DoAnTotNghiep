package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

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
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "thuongHieuId", required = false) Integer thuongHieuId,
            @RequestParam(name = "chatLieuId", required = false) Integer chatLieuId,
            @RequestParam(name = "tayAoId", required = false) Integer tayAoId,
            @RequestParam(name = "coAoId", required = false) Integer coAoId,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size, // Đặt mặc định là 10
            @PageableDefault(size = 10) Pageable pageable, // Đặt mặc định là 10
            Model model
    ) {
        // Sử dụng size từ @RequestParam để phân trang
        Page<SanPhamResponse> products = sanPhamService.timKiemSanPham(
                keyword, status, thuongHieuId, chatLieuId, tayAoId, coAoId, pageable.getPageNumber(), size);

        model.addAttribute("products", products.getContent());

        // Tính toán startPage và endPage
        int currentPage = products.getNumber();
        int totalPages = products.getTotalPages();
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        // Điều chỉnh nếu dải trang không đủ 5 trang
        if (endPage - startPage < 4) {
            if (startPage == 0) {
                endPage = Math.min(4, totalPages - 1);
            } else if (endPage == totalPages - 1) {
                startPage = Math.max(0, totalPages - 5);
            }
        }

        // Truyền các giá trị vào model
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);

        addSanPhamModelAttributes(model);

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

    private void addSanPhamModelAttributes(Model model) {
        model.addAttribute("thuongHieus", thuongHieuService.getAll());
        model.addAttribute("chatLieus", chatLieuService.getAll());
        model.addAttribute("kieuCoAos", kieuCoAoService.getAll());
        model.addAttribute("kieuTayAos", kieuTayAoService.getAll());
    }
}
