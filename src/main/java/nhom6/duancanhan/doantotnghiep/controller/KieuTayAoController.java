package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
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
    final SanPhamService sanPhamService;


    public KieuTayAoController(KieuTayAoService kieuTayAoService, SanPhamService sanPhamService) {
        this.kieuTayAoService = kieuTayAoService;
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
        Page<KieuTayAo> page = kieuTayAoService.phanTrang(pageNo, pageSize);
        List<KieuTayAo> listKTA = page.getContent();
        model.addAttribute("kieuTayAo", new KieuTayAo());
        model.addAttribute("listKTA", listKTA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/KieuTayAo/KieuTayAo";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<KieuTayAo> kieuTayAo = kieuTayAoService.detail(id);
        List<SanPhamResponse> sanPhams = sanPhamService.getSanPhamByKieuTayAoId(id);
        if (kieuTayAo.isPresent()){
            model.addAttribute("kieuTayAo", kieuTayAo.get());
            model.addAttribute("sanPhams", sanPhams);
            return "/admin/sanpham/KieuTayAo/Detail";
        }
        return "redirect:/admin/kieu-tay-ao";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("kieuTayAo") KieuTayAo kieuTayAo) {
        kieuTayAo.setTrangThai(0);
        kieuTayAoService.addKieuTayAo(kieuTayAo);
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

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kieuCoAo") KieuTayAo kieuTayAo) {
        Optional<KieuTayAo> existingKieuTayAoOpt = kieuTayAoService.detail(id);
        if (existingKieuTayAoOpt.isPresent()) {
            KieuTayAo existingKieuTayAo = existingKieuTayAoOpt.get();
            kieuTayAo.setNgayTao(existingKieuTayAo.getNgayTao());
            kieuTayAo.setId(existingKieuTayAo.getId());
            kieuTayAoService.updateKieuTayAo(id, kieuTayAo);
            return "redirect:/admin/kieu-tay-ao";
        }
        return "redirect:/admin/kieu-tay-ao";
    }


    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KieuTayAo> optionalKieuTayAo = kieuTayAoService.detail(id);
        if (optionalKieuTayAo.isPresent()) {
            KieuTayAo kieuTayAo = optionalKieuTayAo.get();
            kieuTayAo.setTrangThai(1);
            kieuTayAoService.updateKieuTayAoById(id, kieuTayAo);
        }
        return "redirect:/admin/kieu-tay-ao";
    }
}
