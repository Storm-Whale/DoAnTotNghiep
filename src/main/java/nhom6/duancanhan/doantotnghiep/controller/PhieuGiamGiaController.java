package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaService phieuGiamGiaService;

    public PhieuGiamGiaController(PhieuGiamGiaService phieuGiamGiaService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
    }

//    @GetMapping("/{pageNo}")
//    public String getAll(@PathVariable(value = "pageNo") int pageNo,
//                            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
//                            Model model) {
//        Page<PhieuGiamGia> page = phieuGiamGiaService.phanTrang(pageNo, pageSize);
//        List<PhieuGiamGia> listPGG = page.getContent();
//        model.addAttribute("phieuGiamGia", new PhieuGiamGia());
//        model.addAttribute("listPGG", listPGG);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("size", pageSize);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems",page.getTotalElements());
//        return "/admin/PhieuGiamGia/PhieuGiamGia";
//    }

    @GetMapping("/index")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "maPhieuGiamGia", required = false) String maPhieuGiamGia,
            @RequestParam(value = "tenPhieuGiamGia", required = false) String tenPhieuGiamGia,
            @RequestParam(value = "ngayBatDau", required = false) Date ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) Date ngayKetThuc,
            @RequestParam(value = "kieuGiamGia", required = false) Integer kieuGiamGia,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            Model model) {
        Page<PhieuGiamGia> pageFind = phieuGiamGiaService.findByCriteria(maPhieuGiamGia, tenPhieuGiamGia, ngayBatDau,
                ngayKetThuc, kieuGiamGia, trangThai, page, size);
        Pageable pageable = PageRequest.of(page, size);

        System.out.println("Total Elements: " + pageFind.getTotalElements());
        List<PhieuGiamGia> listPGG = pageFind.getContent();
        model.addAttribute("phieuGiamGia", new PhieuGiamGia());
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());

        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("maPhieuGiamGia", maPhieuGiamGia);
        model.addAttribute("tenPhieuGiamGia", tenPhieuGiamGia);
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
            return "/admin/PhieuGiamGia/Detail";
        }
        return "redirect:/admin/phieu-giam-gia";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia) {
        return "/admin/PhieuGiamGia/Add";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaService.update(id, phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        phieuGiamGiaService.delete(id);
        return "redirect:/admin/phieu-giam-gia";
    }

    @PostMapping("/addpg")
    public String addpg(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaService.create(phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia";
    }
}
