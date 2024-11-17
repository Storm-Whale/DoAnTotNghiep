package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
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
@RequestMapping("/admin/mausac")
public class MauSacController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<MauSac> page = mauSacService.phanTrang(pageNo, pageSize);
        List<MauSac> listMS = page.getContent();
        model.addAttribute("mauSac", new MauSac());
        model.addAttribute("listMS", listMS);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/MauSac/Mausac";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<MauSac> mauSac = mauSacService.detail(id);
        if (mauSac.isPresent()) {
            model.addAttribute("mauSac", mauSac.get());
            return "admin/sanpham/Mausac/Detail";
        }
        return "redirect:/admin/mausac";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("mauSac") MauSac mauSac, Model model) {
        mauSac.setTrangThai(1);
        mauSacService.addMauSac(mauSac);
        return "redirect:/admin/mausac";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("mauSac") MauSac mauSac) {
        mauSacService.updateMauSac(id, mauSac);
        return "redirect:/admin/mausac";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        mauSacService.deleteMauSac(id);  // Xóa bản ghi dựa trên ID
        return "redirect:/admin/mausac";  // Chuyển hướng về trang danh sách sau khi xóa thành công
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
