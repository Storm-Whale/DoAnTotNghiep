package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/kieu-co-ao")
public class KieuCoAoController {
    private final KieuCoAoService kieuCoAoService;
    final SanPhamService sanPhamService;

    public KieuCoAoController(KieuCoAoService kieuCoAoService, SanPhamService sanPhamService) {
        this.kieuCoAoService = kieuCoAoService;
        this.sanPhamService = sanPhamService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "tenCoAo", required = false, defaultValue = "") String tenCoAo,
                         @RequestParam(value = "trangThai", required = false) Integer trangThai,
                         Model model) {
        int defaultPageNo = 1;
        int defaultPageSize = 5;
        return phanTrangKieuCoAo(defaultPageNo, defaultPageSize, tenCoAo, trangThai, model);
    }
    @GetMapping("/{pageNo}")
    public String phanTrangKieuCoAo(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value = "tenCoAo", required = false, defaultValue = "") String tenCoAo,
            @RequestParam(value = "trangThai", required = false, defaultValue = "-1") Integer trangThai,
            Model model) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        Page<KieuCoAo> page;
        if (trangThai != null && trangThai != -1 && !tenCoAo.isEmpty()) {
            page = kieuCoAoService.timKiemVaPhanTrang(tenCoAo, trangThai, pageNo, pageSize);
        } else if (trangThai != null && trangThai != -1) {
            page = kieuCoAoService.phanTrangTheoTrangThai(trangThai, pageNo, pageSize);
        } else if (!tenCoAo.isEmpty()) {
            page = kieuCoAoService.phanTrangTheoTen(tenCoAo, pageNo, pageSize);
        } else {
            page = kieuCoAoService.phanTrang(pageNo, pageSize);
        }
        List<KieuCoAo> listKCA = page.getContent();
        model.addAttribute("kieuCoAo", new KieuCoAo());
        model.addAttribute("listKCA",listKCA.isEmpty() ? Collections.emptyList() : listKCA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("tenCoAo", tenCoAo);
        model.addAttribute("trangThai", trangThai);

        return "/admin/sanpham/KieuCoAo/KieuCoAo";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<KieuCoAo> kieuCoAo = kieuCoAoService.detail(id);
        List<SanPhamResponse> sanPhams = sanPhamService.getSanPhamByKieuCoAoId(id);

        if (kieuCoAo.isPresent()) {
            model.addAttribute("kieuCoAo", kieuCoAo.get());
            model.addAttribute("sanPhams", sanPhams);
            return "/admin/sanpham/KieuCoAo/Detail";
        }
        return "redirect:/admin/kieu-co-ao";
    }
//    @PostMapping("/add")
//    public String add(@ModelAttribute("kieuCoAo") @Valid KieuCoAo kieuCoAo,
//                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
//                      Model model) {
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kiểu cổ áo!");
//            if(kieuCoAo.getTenCoAo().length() > 20){
//                redirectAttributes.addFlashAttribute("error", "Tên kiểu cổ áo không được quá 20 kí tự!");
//            }
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.kieuCoAo", bindingResult);
//            redirectAttributes.addFlashAttribute("kieuCoAo", kieuCoAo);
//            // Nếu có lỗi validation, trả lại form với thông báo lỗi
//            return "redirect:/admin/kieu-co-ao";
//        }
//        // Gọi service để thêm đối tượng vào cơ sở dữ liệu
//        kieuCoAo.setTrangThai(1);
//        kieuCoAoService.addKieuCoAo(kieuCoAo);
//        // Trả về lại danh sách và hiển thị trang hiện tại
//        return "redirect:/admin/kieu-co-ao";
//    }
        @PostMapping("/add")
        public String add(@ModelAttribute("kieuCoAo") @Valid KieuCoAo kieuCoAo,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes,
                          Model model) {
            // Kiểm tra khoảng trắng và tên
            if (kieuCoAo.getTenCoAo() == null || kieuCoAo.getTenCoAo().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kiểu cổ áo!");
                return "redirect:/admin/kieu-co-ao";
            }
            // Loại bỏ khoảng trắng thừa
            String tenCoAo = kieuCoAo.getTenCoAo().trim();
            // Kiểm tra độ dài
            if (tenCoAo.length() > 20) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu cổ áo không được quá 20 kí tự!");
                return "redirect:/admin/kieu-co-ao";
            }
            // Kiểm tra không bắt đầu bằng số hoặc ký tự đặc biệt
            if (!tenCoAo.matches("^[a-zA-Z].*")) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu cổ áo phải bắt đầu bằng chữ cái!");
                return "redirect:/admin/kieu-co-ao";
            }
            // Kiểm tra không có khoảng trắng liên tiếp
            if (tenCoAo.matches(".*\\s{2,}.*")) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu cổ áo không được chứa nhiều khoảng trắng liên tiếp!");
                return "redirect:/admin/kieu-co-ao";
            }
            // Kiểm tra trùng lặp (nếu cần)
            if (kieuCoAoService.existsByTenCoAo(tenCoAo)) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu cổ áo đã tồn tại!");
                return "redirect:/admin/kieu-co-ao";
            }
            // Nếu vượt qua tất cả các kiểm tra
            kieuCoAo.setTenCoAo(tenCoAo); // Đặt lại tên đã trim
            kieuCoAo.setTrangThai(1);
            kieuCoAoService.addKieuCoAo(kieuCoAo);

            redirectAttributes.addFlashAttribute("success", "Thêm kiểu cổ áo thành công!");
            return "redirect:/admin/kieu-co-ao";
        }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<KieuCoAo> kieuCoAo = kieuCoAoService.detail(id);
        if (kieuCoAo.isPresent()) {
            model.addAttribute("kieuCoAo", kieuCoAo.get());
            return "/admin/sanpham/KieuCoAo/Update";
        }
        return "redirect:/admin/kieu-tay-ao";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("kieuCoAo") @Valid KieuCoAo kieuCoAo,
                         BindingResult bindingResult) {
        Optional<KieuCoAo> existingKieuCoAoOpt = kieuCoAoService.detail(id);
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors() || existingKieuCoAoOpt.isEmpty()) {
            return "/admin/sanpham/KieuCoAo/Update"; // Trả về form sửa nếu có lỗi
        }
        // Gán các giá trị không thay đổi từ đối tượng cũ
        KieuCoAo existingKieuCoAo = existingKieuCoAoOpt.get();
        kieuCoAo.setNgayTao(existingKieuCoAo.getNgayTao());
        kieuCoAo.setId(existingKieuCoAo.getId());
        // Cập nhật và chuyển hướng
        kieuCoAoService.updateKieuCoAo(id, kieuCoAo);
        return "redirect:/admin/kieu-co-ao";
    }

    //chinh sua trang thai kieu co ao
    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KieuCoAo> optionalKieuCoAo = kieuCoAoService.detail(id);
        if (optionalKieuCoAo.isPresent()) {
            KieuCoAo kieuCoAo = optionalKieuCoAo.get();
            kieuCoAo.setTrangThai(0);
            kieuCoAoService.updateKieuCoAoById(id, kieuCoAo);
        }
        return "redirect:/admin/kieu-co-ao";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            KieuCoAo kieuCoAo = KieuCoAo.builder()
                    .tenCoAo(ten)
                    .trangThai(1)
                    .build();

            KieuCoAo savedKieuCoAo = kieuCoAoService.addKieuCoAo(kieuCoAo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedKieuCoAo.getId());
            response.put("ten", savedKieuCoAo.getTenCoAo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
