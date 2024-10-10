package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/kieu-co-ao")
public class KieuCoAoController {
    private final KieuCoAoService kieuCoAoService;

    public KieuCoAoController(KieuCoAoService kieuCoAoService) {
        this.kieuCoAoService = kieuCoAoService;
    }
    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1,model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<KieuCoAo> page = kieuCoAoService.phanTrang(pageNo, pageSize);
        List<KieuCoAo> listKCA = page.getContent();
        model.addAttribute("kieuCoAo", new KieuCoAo());
        model.addAttribute("listKCA", listKCA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "/admin/sanpham/KieuCoAo/KieuCoAo";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<KieuCoAo> kieuCoAo = kieuCoAoService.detail(id);
        if (kieuCoAo.isPresent()){
            model.addAttribute("kieuCoAo", kieuCoAo.get());
            return "/admin/sanpham/KieuCoAo/Detail";
        }
        return "redirect:/admin/kieu-co-ao";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("kieuCoAo") KieuCoAo kieuCoAo) {
        kieuCoAoService.addKieuCoAo(kieuCoAo);
        return "redirect:/admin/kieu-co-ao";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kieuCoAo") KieuCoAo kieuCoAo) {
        kieuCoAoService.updateKieuCoAo(id, kieuCoAo);
        return "redirect:/admin/kieu-co-ao";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        kieuCoAoService.deleteKieuCoAo(id);
        return "redirect:/admin/kieu-co-ao";
    }
}
