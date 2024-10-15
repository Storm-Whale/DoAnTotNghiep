package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/product-details")
public class SanPhamChiTietController {

    private final SanPhamChiTietService sanPhamChiTietService;
    private final SanPhamService sanPhamService;
    private final KichCoService kichCoService;
    private final MauSacService mauSacService;

    @GetMapping(value = "/index")
    public ResponseEntity<List<SanPhamChiTietResponse>> index() {
        return new ResponseEntity<>(sanPhamChiTietService.getAllSanPhamChiTiet(), HttpStatus.OK);
    }

    @GetMapping(value = "/find_by_id/{id}")
    public ResponseEntity<SanPhamChiTietResponse> findById(@PathVariable(name = "id") Integer id) {
        SanPhamChiTietResponse product_detail = sanPhamChiTietService.getSanPhamChiTietById(id);
        return new ResponseEntity<>(product_detail, HttpStatus.OK);
    }

    @GetMapping("/create")
    public String create(Model model) {
        addSanPhamChiTietModelAttributes(model, new SanPhamChiTietRequest());
        model.addAttribute("products", sanPhamService.getAllSanPham());
        return "/admin/sanphamchitiet/create";
    }

    @PostMapping(value = "/store")
    public String store(
            @RequestParam(value = "id_sp") Integer idSP,
            @RequestParam(value = "id_mau_sac") List<Integer> idMauSacs,
            @RequestParam(value = "id_kich_co") List<Integer> idKichCos,
            @RequestParam(value = "so_luong") Integer soLuong,
            @RequestParam(value = "gia") Double gia
    ) {
        if (idMauSacs.isEmpty() || idKichCos.isEmpty()) {
            // Xử lý nếu không có màu sắc hoặc kích cỡ nào được chọn
            return "redirect:/admin/product-details";
        }

        for (Integer idMauSac : idMauSacs) {
            for (Integer idKichCo : idKichCos) {
                SanPhamChiTietRequest sanPhamChiTietRequest = SanPhamChiTietRequest.builder()
                        .idSanPham(idSP)
                        .idMauSac(idMauSac)
                        .idKichCo(idKichCo)
                        .soLuong(soLuong)
                        .gia(BigDecimal.valueOf(gia))
                        .trangThai(1)
                        .build();
                sanPhamChiTietService.storeSanPhamChiTiet(sanPhamChiTietRequest);
            }
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")  Integer id, Model model) {
        addSanPhamChiTietModelAttributes(model, new SanPhamChiTietRequest());
        model.addAttribute("products", sanPhamService.getAllSanPham());
        model.addAttribute("old_product_details", sanPhamChiTietService.getSanPhamChiTietById(id));
        return "/admin/sanphamchitiet/update";
    }

    @PostMapping(value = "/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @RequestParam(value = "id_sp") Integer idSP,
            @RequestParam(value = "id_mau_sac") Integer idMauSac,
            @RequestParam(value = "id_kich_co") Integer idKichCo,
            @RequestParam(value = "so_luong") Integer soLuong,
            @RequestParam(value = "gia") Double gia,
            @RequestParam(value = "trang_thai") Integer trangThai
    ) {
        SanPhamChiTietRequest sanPhamChiTietRequest = SanPhamChiTietRequest.builder()
                .idSanPham(idSP)
                .idMauSac(idMauSac)
                .idKichCo(idKichCo)
                .soLuong(soLuong)
                .gia(BigDecimal.valueOf(gia))
                .trangThai(trangThai)
                .build();
        sanPhamChiTietService.updateSanPhamChiTiet(id, sanPhamChiTietRequest);
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        sanPhamChiTietService.sortDeleteSanPhamChiTiet(id);
        return "redirect:/admin";
    }

    private void addSanPhamChiTietModelAttributes(Model model, SanPhamChiTietRequest sanPhamChiTietRequest) {
        model.addAttribute("product_detail", sanPhamChiTietRequest);
        model.addAttribute("mau_sacs", mauSacService.getAll());
        model.addAttribute("kich_cos", kichCoService.getAll());
    }
}