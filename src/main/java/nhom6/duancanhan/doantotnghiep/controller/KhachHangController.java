package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
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
            Model model
    ) {
        // Xử lý tham số tìm kiếm, loại bỏ khoảng trắng thừa
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null; // Đặt thành null nếu keyword rỗng
            }
        }

        // Kiểm tra valid cho page và size
        if (page < 0) {
            page = 0; // Đảm bảo không có trang âm
        }
        if (size < 1) {
            size = 5; // Đảm bảo số lượng kết quả trên mỗi trang hợp lệ
        }

        // Lấy danh sách khách hàng với phân trang và tìm kiếm
        Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listKH.getTotalPages();

        // Kiểm tra nếu trang hiện tại vượt quá tổng số trang
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0; // Về trang cuối nếu vượt quá
            listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size); // Lấy lại dữ liệu
        }

        // Tạo danh sách số trang để hiển thị
        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());

        // Thêm dữ liệu vào model
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("listKH", listKH);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
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
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/khachhang";
    }
    @GetMapping("/view_add")
    public String viewadd(@ModelAttribute("khachHang") KhachHang khachHang) {
//        model.addAttribute("khachHang",khachHangService.getAll());
//        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/adKhachHang";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/customer/adkhachhang";
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

}
