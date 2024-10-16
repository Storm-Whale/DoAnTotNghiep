package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SearchDTO;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    private VaiTroService vaiTroService;

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
        model.addAttribute("listVT",vaiTroService.getAll());
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
        model.addAttribute("listVT",vaiTroService.getAll());
        return "/admin/nhanvien/nhanvien";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien, BindingResult result, Model model) {
        if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "/admin/nhanvien/adNhanVien";
        }
        model.addAttribute("nhanVien",nhanVien);
        nhanVienService.addNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id,Model model) {
        model.addAttribute("nhanVien",nhanVienService.detail(id));
        model.addAttribute("listNV",nhanVienService.getAll());
        model.addAttribute("listTK",taiKhoanService.getAll());
        model.addAttribute("listVT",vaiTroService.getAll());
        return "/admin/nhanvien/updatenhanvien";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien, BindingResult result, Model model) {
       if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            model.addAttribute("listTK",taiKhoanService.getAll());
            model.addAttribute("listVT",vaiTroService.getAll());
            return "/admin/nhanvien/updatenhanvien";
        }
        model.addAttribute("nhanVien",nhanVien);
        nhanVienService.updateNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        nhanVienService.deleteNhanVien(id);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/search")
    public String search(@Valid @ModelAttribute SearchDTO searchDTO, BindingResult result, Model model) {
        if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
              model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            phanTrang(1,model);
            return "/admin/nhanvien/nhanvien";
        }
        List<NhanVien> nv = nhanVienService.findSearch(searchDTO.getKeyword());
      if(nv.isEmpty()) {
          model.addAttribute("message", "No results found");
      }else{
          model.addAttribute("nhanVien",new NhanVien());
          model.addAttribute("listNV",nv);
      }
        return "/admin/nhanvien/nhanvien";
    }
}
