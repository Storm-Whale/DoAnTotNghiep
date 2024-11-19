package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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

    @Autowired
    private UploadImage uploadImage;

    @GetMapping("")
    public String searchKhachHang(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model
    ) {
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null; // Đặt thành null để khớp với logic xử lý trong service
            }
        }
//        if (page < 0) {
//            page = 0;
//        }
        Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listKH.getTotalPages();
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0; // Go to the last page if out of bounds or reset to 0 if no pages exist
            listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size); // Fetch the last page data
        }
        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("listKH", listKH);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageNumbers", pageNumbers);
        return "/admin/customer/khachhang";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/khachhang";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/customer/khachhang";
//          return "redirect:/admin/khachhang/add" + "#demo-modal";
        }
        khachHangService.addKhachHang(khachHang);
        return "redirect:/admin/khachhang";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/updateKhachHang";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
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

    @PostMapping("/update-client/{id}")
    public String updateClient(
            @PathVariable("id") Integer id, @RequestParam(name = "ten", required = false) String ten,
            @RequestParam(name = "email", required = false) String email, @RequestParam(name = "soDienThoai", required = false) String soDienThoai,
            @RequestParam(name = "gioiTinh", required = false) Integer gioiTinh, @RequestParam(name = "ngaySinh", required = false) LocalDate ngaySinh,
            @RequestParam(name = "image", required = false) MultipartFile image,
            HttpSession session, Model model
    ) throws IOException {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (!image.isEmpty()) {
            uploadImage.deleteOldImage(khachHang.getAnhUrl());
            String anhUrl = uploadImage.saveImage(image);
            khachHang.setAnhUrl(anhUrl);
        }
        khachHang.setTen(ten);
        khachHang.setEmail(email);
        khachHang.setSoDienThoai(soDienThoai);
        khachHang.setGioiTinh(gioiTinh);
        khachHang.setNgaySinh(ngaySinh);
        khachHangService.updateKhachHang(khachHang);
        session.setAttribute("user", khachHang);
        return "redirect:/client/showInfoCustomer";
    }

}
