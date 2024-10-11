package nhom6.duancanhan.doantotnghiep.controller;


import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.repository.VaiTroRepository;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/admin/nhanvien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1,model);
    }

    @GetMapping("{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<NhanVien> page = nhanVienService.phanTrang(pageNo,pageSize);
        List<NhanVien> listNV = page.getContent();
        model.addAttribute("nhanVien",new NhanVien());
        model.addAttribute("listNV",listNV);
        model.addAttribute("listTK",taiKhoanService.getAll());
        model.addAttribute("listVT",vaiTroRepository.findAll());
        model.addAttribute("currentPage ", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "/admin/nhanvien/nhanvien";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id,Model model) {
        model.addAttribute("nhanVien",nhanVienService.detail(id));
        model.addAttribute("listTK",taiKhoanService.getAll());
        model.addAttribute("listNV",nhanVienService.getAll());
        model.addAttribute("listVT",vaiTroRepository.findAll());
        return "/admin/nhanvien/nhanvien";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        nhanVienService.addNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id,Model model) {
        model.addAttribute("nhanVien",nhanVienService.detail(id));
        model.addAttribute("listNV",nhanVienService.getAll());
        model.addAttribute("listTK",taiKhoanService.getAll());
        model.addAttribute("listVT",vaiTroRepository.findAll());
        return "/admin/nhanvien/updatenhanvien";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        nhanVienService.updateNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        nhanVienService.deleteNhanVien(id);
        return "redirect:/admin/nhanvien";
    }
}
