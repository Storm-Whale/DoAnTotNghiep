package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("")
    public String searchKhachHang(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "trangThai", required = false) Integer trangThai,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size,
        Model model) {
            if(page < 0) {
                page = 0;
            }
            Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
            int totalPages = listKH.getTotalPages();
            if(page >= totalPages) {
                page = totalPages > 0 ? totalPages - 1 : 0; // Go to the last page if out of bounds or reset to 0 if no pages exist
                listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size); // Fetch the last page data
            }
            List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("khachHang", new KhachHang());
            model.addAttribute("listKH", listKH);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages",totalPages);
            model.addAttribute("keyword", keyword);
            model.addAttribute("trangThai", trangThai);
            model.addAttribute("pageNumbers", pageNumbers);
            return "/admin/customer/khachhang";
       }

//    @GetMapping("{pageNo}")
//    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
//        int pageSize = 3;
//        Page<KhachHang> page = khachHangService.phanTrang(pageNo,pageSize);
//        List<KhachHang> listKH = page.getContent();
//        model.addAttribute("khachHang",new KhachHang());
//        model.addAttribute("listKH",listKH);
//        model.addAttribute("listTK",taiKhoanService.getAll());
//        model.addAttribute("currentPage ", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems",page.getTotalElements());
//        return "/admin/customer/khachhang";
//    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang",khachHangService.detail(id));
        model.addAttribute("listKH",khachHangService.getAll());
        model.addAttribute("listTK",taiKhoanService.getAll());
        return "/admin/customer/khachhang";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result, Model model) {
        if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "/admin/customer/khachhang";
//          return "redirect:/admin/khachhang/add" + "#demo-modal";
        }
        khachHangService.addKhachHang(khachHang);
        return "redirect:/admin/khachhang";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id,Model model) {
        model.addAttribute("khachHang",khachHangService.detail(id));
        model.addAttribute("listKH",khachHangService.getAll());
        model.addAttribute("listTK",taiKhoanService.getAll());
        return "/admin/customer/updateKhachHang";
    }

    @PostMapping("/update")
    public String update( @Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result, Model model) {
        if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "/admin/customer/updateKhachHang";
        }
        khachHangService.updateKhachHang(khachHang);
        return "redirect:/admin/khachhang";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        khachHangService.deleteKhachHang(id);
        return "redirect:/admin/khachhang";
    }

}
