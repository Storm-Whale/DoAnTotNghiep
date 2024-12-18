package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
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
    public String getAll(@RequestParam(value = "tenKichCo", required = false, defaultValue = "") String tenKichCo,
                         @RequestParam(value = "trangThai", required = false) Integer trangThai,
                         Model model) {
        int defaultPageNo = 1;
        int defaultPageSize = 5;
        return phanTrang(defaultPageNo, defaultPageSize, tenKichCo, trangThai, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
                            @RequestParam(value = "tenKichCo", required = false, defaultValue = "") String tenKichCo,
                            @RequestParam(value = "trangThai", required = false, defaultValue = "-1") Integer trangThai,
                            Model model) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        Page<KichCo> page;
        if (trangThai != null && trangThai != -1 && !tenKichCo.isEmpty()) {
            page = kichCoService.timKiemVaPhanTrang(tenKichCo, trangThai, pageNo, pageSize);
        } else if (trangThai != null && trangThai != -1) {
            page = kichCoService.phanTrangTheoTrangThai(trangThai, pageNo, pageSize);
        } else if (!tenKichCo.isEmpty()) {
            page = kichCoService.phanTrangTheoTen(tenKichCo, pageNo, pageSize);
        } else {
            page = kichCoService.phanTrang(pageNo, pageSize);
        }
        List<KichCo> listKC = page.getContent();
        model.addAttribute("kichCo", new KichCo());
        model.addAttribute("listKC", listKC);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("tenKichCo", tenKichCo);
        model.addAttribute("trangThai", trangThai);
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
    public String add(@ModelAttribute("kichCo") @Valid KichCo kichCo,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      Model model) {
        // Kiểm tra khoảng trắng và tên
        if (kichCo.getTenKichCo() == null || kichCo.getTenKichCo().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kích cỡ!");
            return "redirect:/admin/kichco";
        }
        // Loại bỏ khoảng trắng thừa
        String tenKichCo = kichCo.getTenKichCo().trim();
        // Kiểm tra độ dài
        if (tenKichCo.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "Tên kích cỡ không được quá 20 kí tự!");
            return "redirect:/admin/kichco";
        }
        // Kiểm tra không bắt đầu bằng số hoặc ký tự đặc biệt
        if (!tenKichCo.matches("^[a-zA-Z].*")) {
            redirectAttributes.addFlashAttribute("error", "Tên kích cỡ phải bắt đầu bằng chữ cái!");
            return "redirect:/admin/kichco";
        }
        // Kiểm tra không có khoảng trắng liên tiếp
        if (tenKichCo.matches(".*\\s{2,}.*")) {
            redirectAttributes.addFlashAttribute("error", "Tên kích cỡ không được chứa nhiều khoảng trắng liên tiếp!");
            return "redirect:/admin/kichco";
        }
        // Kiểm tra trùng lặp (nếu cần)
        if (kichCoService.existsByTenKichCo(tenKichCo)) {
            redirectAttributes.addFlashAttribute("error", "Tên kích cỡ đã tồn tại!");
            return "redirect:/admin/kichco";
        }
        // Nếu vượt qua tất cả các kiểm tra
        kichCo.setTenKichCo(tenKichCo); // Đặt lại tên đã trim
        kichCo.setTrangThai(1);
        kichCoService.addKichCo(kichCo);

        redirectAttributes.addFlashAttribute("success", "Thêm kích cỡ thành công!");
        return "redirect:/admin/kichco";
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kích cỡ");
//            String tenKichCo = kichCo.getTenKichCo();
//            if (kichCo.getTenKichCo().isEmpty()) {
//                redirectAttributes.addFlashAttribute("error", "Tên kích cỡ không được để trống!");
//                return "redirect:/admin/kichco";
//            }
//
//            // Check leading/trailing whitespaces
//            if (!kichCo.getTenKichCo().trim().equals(kichCo.getTenKichCo())) {
//                bindingResult.rejectValue("tenKichCo", "tenKichCo.whitespace", "Tên kích cỡ không được chứa khoảng trắng ở đầu hoặc cuối!");
//                return "redirect:/admin/kichco";
//            }
//            // Check length
//            else if (tenKichCo.length() > 20) {
//                redirectAttributes.addFlashAttribute("error", "Tên kích cỡ không được quá 20 kí tự!");
//                return "redirect:/admin/kichco";
//            }
//            // Check for special characters
//            else if (kichCo.getTenKichCo().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
//                redirectAttributes.addFlashAttribute("error", "Tên kích cỡ không được chứa ký tự đặc biệt!");
//                return "redirect:/admin/kichco";
//            }
//
//            // Check duplicates (implement database logic)
//            else if (kichCoService.existsByTenKichCo(tenKichCo)) {
//                redirectAttributes.addFlashAttribute("error", "Tên kích cỡ đã tồn tại!");
//                return "redirect:/admin/kichco";
//            }
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.kichCo", bindingResult);
//            redirectAttributes.addFlashAttribute("kichCo", kichCo);
//            // Nếu có lỗi validation, trả lại form với thông báo lỗi
//            return "redirect:/admin/kichco";
//        }
//        kichCo.setTrangThai(1);
//        kichCoService.addKichCo(kichCo);
//        return "redirect:/admin/kichco";
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
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("kichCo") @Valid KichCo kichCo,
                         BindingResult bindingResult) {
        Optional<KichCo> existingKichCoOpt = kichCoService.detail(id);
        if (bindingResult.hasErrors() || existingKichCoOpt.isEmpty()) {
            return "/admin/sanpham/KichCo/Update"; // Trả về form sửa nếu có lỗi
        }
            KichCo existingKichCo = existingKichCoOpt.get();
            kichCo.setNgayTao(existingKichCo.getNgayTao());
            kichCo.setId(existingKichCo.getId());
            kichCoService.updateKichCo(id, kichCo);
            return "redirect:/admin/kichco";
    }

    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KichCo> optionalKichCo = kichCoService.detail(id);
        if (optionalKichCo.isPresent()) {
            KichCo kichCo = optionalKichCo.get();
            kichCo.setTrangThai(0);
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
