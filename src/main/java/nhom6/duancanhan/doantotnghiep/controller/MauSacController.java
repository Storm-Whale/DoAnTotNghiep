package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/mausac")
public class MauSacController {
    private final SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private final MauSacService mauSacService;

    public MauSacController(SanPhamChiTietService sanPhamChiTietService, MauSacService mauSacService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.mauSacService = mauSacService;
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
        Page<MauSac> page = mauSacService.phanTrang(pageNo, pageSize);
        List<MauSac> listMS = page.getContent();
        model.addAttribute("mauSac", new MauSac());
        model.addAttribute("listMS", listMS);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/MauSac/Mausac";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<MauSac> mauSac = mauSacService.detail(id);
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getMauSacId(id);
        if (mauSac.isPresent()) {
            model.addAttribute("mauSac", mauSac.get());
            model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
            return "admin/sanpham/Mausac/Detail";
        }
        return "redirect:/admin/mausac";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("mauSac") @Valid MauSac mauSac,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên màu sắc");
            String tenMauSac = mauSac.getTenMauSac();
            if (mauSac.getTenMauSac().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên màu sắc không được để trống!");
                return "redirect:/admin/mausac";
            }

            // Check leading/trailing whitespaces
            if (!mauSac.getTenMauSac().trim().equals(mauSac.getTenMauSac())) {
                bindingResult.rejectValue("tenMauSac", "tenMauSac.whitespace", "Tên màu sắc không được chứa khoảng trắng ở đầu hoặc cuối!");
                return "redirect:/admin/mausac";
            }
            // Check length
            else if (tenMauSac.length() > 20) {
                redirectAttributes.addFlashAttribute("error", "Tên màu sắc không được quá 20 kí tự!");
                return "redirect:/admin/mausac";
            }
            // Check for special characters
            else if (mauSac.getTenMauSac().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                redirectAttributes.addFlashAttribute("error", "Tên màu sắc không được chứa ký tự đặc biệt!");
                return "redirect:/admin/mausac";
            }

            // Check duplicates (implement database logic)
            else if (mauSacService.existsByTenMauSac(tenMauSac)) {
                redirectAttributes.addFlashAttribute("error", "Tên màu sắc đã tồn tại!");
                return "redirect:/admin/mausac";
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.kichCo", bindingResult);
            redirectAttributes.addFlashAttribute("mauSac", mauSac);
            // Nếu có lỗi validation, trả lại form với thông báo lỗi
            return "redirect:/admin/mausac";
        }
        mauSac.setTrangThai(1);
        mauSacService.addMauSac(mauSac);
        return "redirect:/admin/mausac";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<MauSac> mauSac = mauSacService.detail(id);
        if (mauSac.isPresent()) {
            model.addAttribute("mauSac", mauSac.get());
            return "/admin/sanpham/MauSac/Update";
        }
        return "redirect:/admin/mausac";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("mauSac") @Valid MauSac mauSac,
                         BindingResult bindingResult) {
        Optional<MauSac> existingMauSacOpt = mauSacService.detail(id);
        if (bindingResult.hasErrors() || existingMauSacOpt.isEmpty()) {
            return "/admin/sanpham/MauSac/Update";
        }
            MauSac existingMauSac = existingMauSacOpt.get();
            mauSac.setNgayTao(existingMauSac.getNgayTao());
            mauSac.setId(existingMauSac.getId());
            mauSacService.updateMauSac(id, mauSac);
            return "redirect:/admin/mausac";
    }

    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<MauSac> optionalMauSac = mauSacService.detail(id);
        if (optionalMauSac.isPresent()) {
            MauSac mauSac = optionalMauSac.get();
            mauSac.setTrangThai(1);
            mauSacService.updateMauSacById(id, mauSac);
        }
        return "redirect:/admin/mausac";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            MauSac mauSac = MauSac.builder()
                    .tenMauSac(ten)
                    .trangThai(1)
                    .build();

            MauSac savedMauSac = mauSacService.addMauSac(mauSac);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedMauSac.getId());
            response.put("ten", savedMauSac.getTenMauSac());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

}
