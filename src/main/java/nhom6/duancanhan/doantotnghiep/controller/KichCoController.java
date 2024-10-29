package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/kichco")
public class KichCoController {
    @Autowired
    private KichCoService kichCoService;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<KichCo> page = kichCoService.phanTrang(pageNo, pageSize);
        List<KichCo> listKC = page.getContent();
        model.addAttribute("kichCo", new KichCo());
        model.addAttribute("listKC", listKC);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/KichCo/Kichco";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<KichCo> kichCo = kichCoService.detail(id);
        if (kichCo.isPresent()) {
            model.addAttribute("kichCo", kichCo.get());
            return "admin/sanpham/Kichco/Detail";
        }
        return "redirect:/admin/kichco";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("kichCo") KichCo kichCo, Model model) {
        kichCo.setTrangThai(1);
        kichCoService.addKichCo(kichCo);
        return "redirect:/admin/kichco";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kichCo") KichCo kichCo) {
        kichCoService.updateKichCo(id, kichCo);
        return "redirect:/admin/kichco";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        kichCoService.deleteKichCo(id);  // Xóa bản ghi dựa trên ID
        return "redirect:/admin/kichco";  // Chuyển hướng về trang danh sách sau khi xóa thành công
    }

}
