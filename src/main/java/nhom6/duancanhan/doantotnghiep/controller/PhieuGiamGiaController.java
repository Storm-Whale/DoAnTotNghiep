package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaService phieuGiamGiaService;

    public PhieuGiamGiaController(PhieuGiamGiaService phieuGiamGiaService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
    }

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1,model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<PhieuGiamGia> page = phieuGiamGiaService.phanTrang(pageNo, pageSize);
        List<PhieuGiamGia> listPGG = page.getContent();
        model.addAttribute("phieuGiamGia", new PhieuGiamGia());
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "/admin/PhieuGiamGia/PhieuGiamGia";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaService.getById(id);
        if (phieuGiamGia.isPresent()){
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
