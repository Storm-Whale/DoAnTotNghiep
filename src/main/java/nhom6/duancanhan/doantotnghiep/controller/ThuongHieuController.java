package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
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
@RequestMapping(value = "/admin/thuong-hieu")
public class ThuongHieuController {

    @Autowired
    private final ThuongHieuService thuongHieuService;
    final SanPhamService sanPhamService;

    public ThuongHieuController(ThuongHieuService thuongHieuService,SanPhamService sanPhamService) {
        this.thuongHieuService = thuongHieuService;
        this.sanPhamService = sanPhamService;
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
        Page<ThuongHieu> page = thuongHieuService.phanTrang(pageNo, pageSize);
        List<ThuongHieu> listTH = page.getContent();
        model.addAttribute("thuongHieu", new ThuongHieu());
        model.addAttribute("listTH", listTH);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/ThuongHieu/thuonghieu";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<ThuongHieu> thuongHieu = thuongHieuService.detail(id);
        List<SanPhamResponse> sanPhams = sanPhamService.getSanPhamByThuongHieuId(id);
        if (thuongHieu.isPresent()) {
            model.addAttribute("thuongHieu", thuongHieu.get());
            model.addAttribute("sanPhams", sanPhams);
            return "admin/sanpham/Thuonghieu/Detail";
        }
        return "redirect:/admin/thuong-hieu";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("thuongHieu") @Valid ThuongHieu thuongHieu,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      Model model){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên thương hiệu");
            String tenThuongHieu = thuongHieu.getTenThuongHieu();
            if (thuongHieu.getTenThuongHieu().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên thương hiệu không được để trống!");
                return "redirect:/admin/thuong-hieu";
            }

            // Check leading/trailing whitespaces
            if (!thuongHieu.getTenThuongHieu().trim().equals(thuongHieu.getTenThuongHieu())) {
                bindingResult.rejectValue("tenThuongHieu", "tenThuongHieu.whitespace", "Tên thương hiệu không được chứa khoảng trắng ở đầu hoặc cuối!");
                return "redirect:/admin/thuong-hieu";
            }
            // Check length
            else if (tenThuongHieu.length() > 20) {
                redirectAttributes.addFlashAttribute("error", "Tên thương hiệu không được quá 20 kí tự!");
                return "redirect:/admin/thuong-hieu";
            }
            // Check for special characters
            else if (thuongHieu.getTenThuongHieu().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                redirectAttributes.addFlashAttribute("error", "Tên thương hiệu không được chứa ký tự đặc biệt!");
                return "redirect:/admin/thuong-hieu";
            }

            // Check duplicates (implement database logic)
            else if (thuongHieuService.existsByTenThuongHieu(tenThuongHieu)) {
                redirectAttributes.addFlashAttribute("error", "Tên thương hiệu đã tồn tại!");
                return "redirect:/admin/thuong-hieu";
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.thuongHieu", bindingResult);
            redirectAttributes.addFlashAttribute("thuongHieu", thuongHieu);
            // Nếu có lỗi validation, trả lại form với thông báo lỗi
            return "redirect:/admin/thuong-hieu";
        }
        thuongHieu.setTrangThai(1);
        thuongHieuService.addThuongHieu(thuongHieu);
        return "redirect:/admin/thuong-hieu";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<ThuongHieu> thuongHieu = thuongHieuService.detail(id);
        if (thuongHieu.isPresent()) {
            model.addAttribute("thuongHieu", thuongHieu.get());
            return "/admin/sanpham/ThuongHieu/Update";
        }
        return "redirect:/admin/thuong-hieu";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("thuongHieu") @Valid ThuongHieu thuongHieu,
                         BindingResult bindingResult) {
        Optional<ThuongHieu> existingThuongHieuOpt = thuongHieuService.detail(id);
        if (bindingResult.hasErrors() || existingThuongHieuOpt.isEmpty()) {
            return "/admin/sanpham/ThuongHieu/Update";
        }
            ThuongHieu existingThuongHieu = existingThuongHieuOpt.get();
            thuongHieu.setNgayTao(existingThuongHieu.getNgayTao());
            thuongHieu.setId(existingThuongHieu.getId());
            thuongHieuService.updateThuongHieu(id, thuongHieu);

        return "redirect:/admin/thuong-hieu";
    }

    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<ThuongHieu> optionalThuongHieu = thuongHieuService.detail(id);
        if (optionalThuongHieu.isPresent()) {
            ThuongHieu thuongHieu = optionalThuongHieu.get();
            thuongHieu.setTrangThai(1);
            thuongHieuService.updateThuongHieuById(id, thuongHieu);
        }
        return "redirect:/admin/thuong-hieu";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            ThuongHieu thuongHieu = ThuongHieu.builder()
                    .tenThuongHieu(ten)
                    .trangThai(1)
                    .build();

            // Lưu và lấy entity đã được persist
            ThuongHieu savedThuongHieu = thuongHieuService.addThuongHieu(thuongHieu);

            // Tạo response data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedThuongHieu.getId());
            response.put("ten", savedThuongHieu.getTenThuongHieu());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
