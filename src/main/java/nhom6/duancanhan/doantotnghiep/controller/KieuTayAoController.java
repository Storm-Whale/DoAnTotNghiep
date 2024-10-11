package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/kieu-tay-ao")
public class KieuTayAoController {
    private final KieuTayAoService kieuTayAoService;

    public KieuTayAoController(KieuTayAoService kieuTayAoService) {
        this.kieuTayAoService = kieuTayAoService;
    }

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1,model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<KieuTayAo> page = kieuTayAoService.phanTrang(pageNo, pageSize);
        List<KieuTayAo> listKCA = page.getContent();
        model.addAttribute("kieuTayAo", new KieuTayAo());
        model.addAttribute("listKTA", listKCA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "/admin/sanpham/KieuTayAo/KieuTayAo";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<KieuTayAo> kieuTayAo = kieuTayAoService.detail(id);
        if (kieuTayAo.isPresent()){
            model.addAttribute("kieuTayAo", kieuTayAo.get());
            return "/admin/sanpham/KieuTayAo/Detail";
        }
        return "redirect:/admin/kieu-tay-ao";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("kieuTayAo") KieuTayAo kieuTayAo) {
        kieuTayAoService.addKieuTayAo(kieuTayAo);
        return "redirect:/admin/kieu-tay-ao";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kieuTayAo") KieuTayAo kieuTayAo) {
        kieuTayAoService.updateKieuTayAo(id, kieuTayAo);
        return "redirect:/admin/kieu-tay-ao";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        kieuTayAoService.deleteKieuTayAo(id);
        return "redirect:/admin/kieu-tay-ao";
    }
}
