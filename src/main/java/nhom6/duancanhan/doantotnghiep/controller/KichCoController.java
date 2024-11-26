package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/kichco")
public class KichCoController {
    @Autowired
    private final KichCoService kichCoService;
    final SanPhamChiTietService sanPhamChiTietService;
    public KichCoController(KichCoService kichCoService, SanPhamChiTietService sanPhamChiTietService) {
        this.kichCoService = kichCoService;
        this.sanPhamChiTietService = sanPhamChiTietService;
    }

    @GetMapping("")
    public String getAll(Model model) {
        int defaultPageSize = 5;
        return phanTrang(1, defaultPageSize, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
                            Model model) {
        Page<KichCo> page = kichCoService.phanTrang(pageNo, pageSize);
        List<KichCo> listKC = page.getContent();
        model.addAttribute("kichCo", new KichCo());
        model.addAttribute("listKC", listKC);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/KichCo/Kichco";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<KichCo> kichCo = kichCoService.detail(id);
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getKichCoId(id);
        if (kichCo.isPresent()) {
            model.addAttribute("kichCo", kichCo.get());
            model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
            return "admin/sanpham/Kichco/Detail";
        }
        return "redirect:/admin/kichco";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("kichCo") KichCo kichCo, Model model) {
        kichCo.setTrangThai(1);
        kichCoService.addKichCo(kichCo);
        return "redirect:/admin/kichco";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<KichCo> kichCo = kichCoService.detail(id);
        if (kichCo.isPresent()) {
            model.addAttribute("kichCo", kichCo.get());
            return "/admin/sanpham/KichCo/Update";
        }
        return "redirect:/admin/kichco";
    }
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kichCo") KichCo kichCo) {
        Optional<KichCo> existingKichCoOpt = kichCoService.detail(id);
        if (existingKichCoOpt.isPresent()) {
            KichCo existingKichCo = existingKichCoOpt.get();
            kichCo.setNgayTao(existingKichCo.getNgayTao());
            kichCo.setId(existingKichCo.getId());
            kichCoService.updateKichCo(id, kichCo);
            return "redirect:/admin/kichco";
        }
        return "redirect:/admin/kichco";
    }

    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KichCo> optionalKichCo = kichCoService.detail(id);
        if (optionalKichCo.isPresent()) {
            KichCo kichCo = optionalKichCo.get();
            kichCo.setTrangThai(1);
            kichCoService.updateKichCoById(id, kichCo);
        }
        return "redirect:/admin/kichco";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            KichCo kichCo = KichCo.builder()
                    .tenKichCo(ten)
                    .trangThai(1)
                    .build();

            // Lưu và lấy entity đã được persist
            KichCo savedKichCo = kichCoService.addKichCo(kichCo);

            // Tạo response data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedKichCo.getId());
            response.put("ten", savedKichCo.getTenKichCo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
