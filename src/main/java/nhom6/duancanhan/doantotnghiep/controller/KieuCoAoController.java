package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String getAll(Model model) {
        int defaultPageSize = 5;
        return phanTrang(1, defaultPageSize, model);
    }
    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
                            Model model) {
        Page<KieuCoAo> page = kieuCoAoService.phanTrang(pageNo, pageSize);
        List<KieuCoAo> listKCA = page.getContent();
        model.addAttribute("kieuCoAo", new KieuCoAo());
        model.addAttribute("listKCA", listKCA);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
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


    @PostMapping("/add")
    public String add(@ModelAttribute("kieuCoAo") KieuCoAo kieuCoAo) {
        kieuCoAo.setTrangThai(0);
        kieuCoAoService.addKieuCoAo(kieuCoAo);
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

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("kieuCoAo") KieuCoAo kieuCoAo) {
        Optional<KieuCoAo> existingKieuCoAoOpt = kieuCoAoService.detail(id);
        if (existingKieuCoAoOpt.isPresent()) {
            KieuCoAo existingKieuCoAo = existingKieuCoAoOpt.get();
            kieuCoAo.setNgayTao(existingKieuCoAo.getNgayTao());
            kieuCoAo.setId(existingKieuCoAo.getId());
            kieuCoAoService.updateKieuCoAo(id, kieuCoAo);
            return "redirect:/admin/kieu-co-ao";
        }
        return "redirect:/admin/kieu-co-ao";
    }

    //chinh sua trang thai kieu co ao
    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<KieuCoAo> optionalKieuCoAo = kieuCoAoService.detail(id);
        if (optionalKieuCoAo.isPresent()) {
            KieuCoAo kieuCoAo = optionalKieuCoAo.get();
            kieuCoAo.setTrangThai(1);
            kieuCoAoService.updateKieuCoAoById(id, kieuCoAo);
        }
        return "redirect:/admin/kieu-co-ao";
    }


}
