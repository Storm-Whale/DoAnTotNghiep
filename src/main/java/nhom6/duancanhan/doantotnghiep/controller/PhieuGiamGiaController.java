package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaService phieuGiamGiaService;
    private final HoaDonService hoaDonService;

    public PhieuGiamGiaController(PhieuGiamGiaService phieuGiamGiaService, HoaDonService hoaDonService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("/index")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "ngayBatDau", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc,
            @RequestParam(value = "kieuGiamGia", required = false) Integer kieuGiamGia,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            Model model) {
        Page<PhieuGiamGia> pageFind = phieuGiamGiaService.findByCriteria(keyword, ngayBatDau,
                ngayKetThuc, kieuGiamGia, trangThai, page, size);
        Pageable pageable = PageRequest.of(page, size);

        System.out.println("Total Elements: " + pageFind.getTotalElements());
        List<PhieuGiamGia> listPGG = pageFind.getContent();
//        model.addAttribute("phieuGiamGia", new PhieuGiamGia());
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());

        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("ngayBatDau", ngayBatDau);
        model.addAttribute("ngayKetThuc", ngayKetThuc);
        model.addAttribute("kieuGiamGia", kieuGiamGia);
        model.addAttribute("trangThai", trangThai);

        return "/admin/PhieuGiamGia/PhieuGiamGia";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaService.getById(id);
        if (phieuGiamGia.isPresent()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia.get());
            List<PhieuGiamGiaHoaDonDTO> hoaDonList = hoaDonService.getHoaDonByPhieuGiamGia(id);
            model.addAttribute("hoaDonList", hoaDonList);
            return "/admin/PhieuGiamGia/Detail";
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia) {
        return "/admin/PhieuGiamGia/Add";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaService.getById(id);
        if (phieuGiamGia.isPresent()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia.get());
            return "/admin/PhieuGiamGia/Update";
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
            BindingResult bindingResult,
            Model model) {
        // Kiểm tra ngày kết thúc phải sau hoặc bằng ngày bắt đầu
        if (phieuGiamGia.getNgayBatDau() != null
                && phieuGiamGia.getNgayKetThuc() != null
                && phieuGiamGia.getNgayKetThuc().before(phieuGiamGia.getNgayBatDau())) {
            bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
        }
        // Kiểm tra giá trị giảm khi kiểu giảm giá là 1 (%)
        if (phieuGiamGia.getKieuGiamGia() == 1 && phieuGiamGia.getGiaTriGiam() != null
                && phieuGiamGia.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            bindingResult.rejectValue("giaTriGiam", "error.phieuGiamGia", "Giá trị giảm không được lớn hơn 100% khi giảm theo phần trăm.");
        }
        // Nếu có lỗi, trả về lại form sửa và hiển thị lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia);
            return "/admin/PhieuGiamGia/Update"; // Đảm bảo đường dẫn đúng với file HTML
        }
        // Cập nhật thông tin nếu không có lỗi
        phieuGiamGiaService.update(id, phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @PostMapping("/update-status/{id}")
    @Transactional
    public String updateStatus(@PathVariable("id") Integer id) {
        Optional<PhieuGiamGia> phieuGiamGiaOptional = phieuGiamGiaService.getById(id);
        if (phieuGiamGiaOptional.isPresent()) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaOptional.get();
            phieuGiamGia.setTrangThai(0);
            phieuGiamGiaService.updateStatus(id, phieuGiamGia);
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }


    @PostMapping("/addpg")
    public String addpg(@Valid @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                        BindingResult bindingResult, Model model) {
        // Kiểm tra ngày kết thúc phải sau hoặc trùng ngày bắt đầu
        if (phieuGiamGia.getNgayBatDau() != null
                && phieuGiamGia.getNgayKetThuc() != null
                && phieuGiamGia.getNgayKetThuc().before(phieuGiamGia.getNgayBatDau())) {
            bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc phải sau hoặc trùng với ngày bắt đầu");
        }
        if (phieuGiamGia.getKieuGiamGia() == 1 && phieuGiamGia.getGiaTriGiam() != null
                && phieuGiamGia.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            bindingResult.rejectValue("giaTriGiam", "error.phieuGiamGia", "Giá trị giảm không được lớn hơn 100% khi giảm theo phần trăm.");
        }
        if (bindingResult.hasErrors()) {
            return "/admin/PhieuGiamGia/Add";
        }
        phieuGiamGiaService.create(phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia/index";
    }
}
