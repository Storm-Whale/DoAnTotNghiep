package nhom6.duancanhan.doantotnghiep.controller;


import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1,model);
    }

    @GetMapping("{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<KhachHang> page = khachHangService.phanTrang(pageNo,pageSize);
        List<KhachHang> listKH = page.getContent();
        model.addAttribute("listKH",listKH);
        model.addAttribute("currentPage ", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        return "/admin/customer/khachhang";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("detailKH",khachHangService.detail(id));
        model.addAttribute("listKH",khachHangService.getAll());
        return "/admin/customer/khachhang";
    }

    @PostMapping("/add")
    public String add(@RequestBody KhachHang khachHang) {
        khachHangService.addKhachHang(khachHang);
        return "redirect :/admin/khachhang";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @RequestBody KhachHang khachHang) {
        khachHangService.updateKhachHang(id, khachHang);
        return "redirect :/admin/khachhang";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        khachHangService.deleteKhachHang(id);
        return "redirect :/admin/khachhang";
    }

}
