package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/products/")
public class SanPhamController {

    private final SanPhamService sanPhamService;
    private final ThuongHieuService thuongHieuService;
    private final ChatLieuService chatLieuService;
    private final KieuCoAoService kieuCoAoService;
    private final KieuTayAoService kieuTayAoService;

    @GetMapping(value = "/index")
    public String index(Model model) {
        List<SanPhamResponse> products = sanPhamService.getAllSanPham();
        model.addAttribute("products", products);
        return "/admin/sanpham/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new SanPhamRequest());
        model.addAttribute("thuongHieus", thuongHieuService.getAll());
        model.addAttribute("chatLieus", chatLieuService.getAll());
        model.addAttribute("kieuCoAos", kieuCoAoService.getAll());
        model.addAttribute("kieuTayAos", kieuTayAoService.getAll());
        return "/admin/sanpham/create";
    }

    @GetMapping(value = "/find_by_id/{id}")
    public ResponseEntity<SanPhamResponse> findById(@PathVariable(name = "id") Integer id) {
        SanPhamResponse product = sanPhamService.getSanPhamById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/store")
    public String store(@Valid @ModelAttribute("product") SanPhamRequest productRequest, BindingResult bindingResult) {
        System.out.println(productRequest);
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamResponse productResponse = sanPhamService.storeSanPham(productRequest);
        return "redirect:/admin/sanpham/index";
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name = "id") Integer id,
            @Valid @RequestBody SanPhamRequest productRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamResponse productResponse = sanPhamService.updateSanPham(id, productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công với id: " + id);
    }
}