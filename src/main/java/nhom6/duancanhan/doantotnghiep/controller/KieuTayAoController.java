package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
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
@RequestMapping("/admin/kieu-tay-ao")
public class KieuTayAoController {
    private final KieuTayAoService kieuTayAoService;
    final SanPhamService sanPhamService;


    public KieuTayAoController(KieuTayAoService kieuTayAoService, SanPhamService sanPhamService) {
        this.kieuTayAoService = kieuTayAoService;
        this.sanPhamService = sanPhamService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "tenTayAo", required = false, defaultValue = "") String tenTayAo,
                         @RequestParam(value = "trangThai", required = false) Integer trangThai,
                         Model model) {
        int defaultPageNo = 1;
        int defaultPageSize = 5;
        return phanTrangKieuTayAo(defaultPageNo, defaultPageSize, tenTayAo, trangThai, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrangKieuTayAo(@PathVariable(value = "pageNo") int pageNo,
                                     @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
                                     @RequestParam(value = "tenTayAo", required = false, defaultValue = "") String tenTayAo,
                                     @RequestParam(value = "trangThai", required = false) Integer trangThai,
                                     Model model) {
        Page<KieuTayAo> page;
        if (trangThai != null) {
            page = kieuTayAoService.phanTrangTheoTrangThai(trangThai, pageNo, pageSize);
        } else if (!tenTayAo.isEmpty()) {
            page = kieuTayAoService.phanTrangTheoTen(tenTayAo, pageNo, pageSize);
        } else {
            page = kieuTayAoService.phanTrang(pageNo, pageSize);
        }
        List<KieuTayAo> listKTA = page.getContent();
        model.addAttribute("kieuTayAo", new KieuTayAo());
        model.addAttribute("listKTA", listKTA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("tenTayAo", tenTayAo);
        model.addAttribute("trangThai", trangThai);

        return "/admin/sanpham/KieuTayAo/KieuTayAo";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<KieuTayAo> kieuTayAo = kieuTayAoService.detail(id);
        List<SanPhamResponse> sanPhams = sanPhamService.getSanPhamByKieuTayAoId(id);
        if (kieuTayAo.isPresent()) {
            model.addAttribute("kieuTayAo", kieuTayAo.get());
            model.addAttribute("sanPhams", sanPhams);
            return "/admin/sanpham/KieuTayAo/Detail";
        }
        return "redirect:/admin/kieu-tay-ao";
    }

//    @PostMapping("/add")
//    public String add(@ModelAttribute("kieuTayAo") @Valid KieuTayAo kieuTayAo,
//                      BindingResult bindingResult, RedirectAttributes redirectAttributes
//                      ) {
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kiểu tay áo!");
//            if(kieuTayAo.getTenTayAo().length() > 20){
//                redirectAttributes.addFlashAttribute("error", "Tên kiểu tay áo không được quá 20 kí tự!");
//            }
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.kieuCoAo", bindingResult);
//            redirectAttributes.addFlashAttribute("kieuTayAo", kieuTayAo);
//            // Nếu có lỗi validation, trả lại form với thông báo lỗi
//            return "redirect:/admin/kieu-tay-ao";
//        }
//        kieuTayAo.setTrangThai(1);
//        kieuTayAoService.addKieuTayAo(kieuTayAo);
//        return "redirect:/admin/kieu-tay-ao";
//    }
        @PostMapping("/add")
        public String add(@ModelAttribute("kieuTayAo") @Valid KieuTayAo kieuTayAo,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {
            // Kiểm tra khoảng trắng và tên
            if (kieuTayAo.getTenTayAo() == null || kieuTayAo.getTenTayAo().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên kiểu tay áo!");
                return "redirect:/admin/kieu-tay-ao";
            }
            // Loại bỏ khoảng trắng thừa
            String tenTayAo = kieuTayAo.getTenTayAo().trim();
            // Kiểm tra độ dài
            if (tenTayAo.length() > 20) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu tay áo không được quá 20 kí tự!");
                return "redirect:/admin/kieu-tay-ao";
            }
            // Kiểm tra không bắt đầu bằng số hoặc ký tự đặc biệt
            if (!tenTayAo.matches("^[a-zA-Z].*")) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu tay áo phải bắt đầu bằng chữ cái!");
                return "redirect:/admin/kieu-tay-ao";
            }
            // Kiểm tra không có khoảng trắng liên tiếp
            if (tenTayAo.matches(".*\\s{2,}.*")) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu tay áo không được chứa nhiều khoảng trắng liên tiếp!");
                return "redirect:/admin/kieu-tay-ao";
            }
            // Kiểm tra trùng lặp (nếu cần)
            if (kieuTayAoService.existsByTenTayAo(tenTayAo)) {
                redirectAttributes.addFlashAttribute("error", "Tên kiểu tay áo đã tồn tại!");
                return "redirect:/admin/kieu-tay-ao";
            }
            // Nếu vượt qua tất cả các kiểm tra
            kieuTayAo.setTenTayAo(tenTayAo); // Đặt lại tên đã trim
            kieuTayAo.setTrangThai(1);
            kieuTayAoService.addKieuTayAo(kieuTayAo);

            redirectAttributes.addFlashAttribute("success", "Thêm kiểu tay áo thành công!");
            return "redirect:/admin/kieu-tay-ao";
        }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<KieuTayAo> kieuTayAo = kieuTayAoService.detail(id);
        if (kieuTayAo.isPresent()) {
            model.addAttribute("kieuTayAo", kieuTayAo.get());
            return "/admin/sanpham/KieuTayAo/Update";
        }
        return "redirect:/admin/kieu-tay-ao";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("kieuTayAo") @Valid KieuTayAo kieuTayAo,
                         BindingResult bindingResult,
                         Model model) {
        Optional<KieuTayAo> existingKieuTayAoOpt = kieuTayAoService.detail(id);

        // Kiểm tra lỗi hoặc không tìm thấy đối tượng
        if (bindingResult.hasErrors() || existingKieuTayAoOpt.isEmpty()) {
            model.addAttribute("kieuTayAo", kieuTayAo); // Đưa lại đối tượng vào model
            return "/admin/sanpham/KieuTayAo/Update"; // Trả về form sửa
        }

        // Lấy thông tin từ đối tượng cũ
        KieuTayAo existingKieuTayAo = existingKieuTayAoOpt.get();
        kieuTayAo.setNgayTao(existingKieuTayAo.getNgayTao());
        kieuTayAo.setId(existingKieuTayAo.getId());

        // Cập nhật và chuyển hướng
        kieuTayAoService.updateKieuTayAo(id, kieuTayAo);
        return "redirect:/admin/kieu-tay-ao";
    }



    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KieuTayAo> optionalKieuTayAo = kieuTayAoService.detail(id);
        if (optionalKieuTayAo.isPresent()) {
            KieuTayAo kieuTayAo = optionalKieuTayAo.get();
            kieuTayAo.setTrangThai(0);
            kieuTayAoService.updateKieuTayAoById(id, kieuTayAo);
        }
        return "redirect:/admin/kieu-tay-ao";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody  // Thêm annotation này
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            KieuTayAo kieuTayAo = KieuTayAo.builder()
                    .tenTayAo(ten)
                    .trangThai(1)
                    .build();

            // Lưu và lấy entity đã được persist
            KieuTayAo savedKieuTayAo = kieuTayAoService.addKieuTayAo(kieuTayAo);

            // Tạo response data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedKieuTayAo.getId());
            response.put("ten", savedKieuTayAo.getTenTayAo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
