package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private KhachHangService khachHangService;


//    @PostMapping
//    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
//        TaiKhoan user = service.findByTenDangNhap(username);
//        if (user != null && user.getMat_khau().equals(password)) {
//            redirectAttributes.addFlashAttribute("loginStatus", "success");
//            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
//            return "redirect:/client/LG"; // Chuyển hướng đến trang chủ
//        } else {
//            redirectAttributes.addFlashAttribute("loginStatus", "error");
//            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
//            return "redirect:/client/LG"; // Chuyển hướng lại đến trang đăng nhập
//        }
//    }


//
//@PostMapping
//public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
//
//    if (username.toLowerCase().contains("admin") || username.toLowerCase().contains("nhanvien")) {
//        redirectAttributes.addFlashAttribute("loginStatus", "error");
//        redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
//        return "redirect:/client/LG";
//    }
//
//
//    TaiKhoan user = taiKhoanService.findByTenDangNhap(username);
//    if (user != null && user.getMat_khau().equals(password)) {
//        redirectAttributes.addFlashAttribute("loginStatus", "success");
//        redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
//        return "redirect:/client/LG";
//    } else {
//        redirectAttributes.addFlashAttribute("loginStatus", "error");
//        redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
//        return "redirect:/client/LG";
//    }
//}

    @PostMapping
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        System.out.println("Đang thực hiện đăng nhập với tên người dùng: " + username);


        if (username.equalsIgnoreCase("admin") || username.equalsIgnoreCase("nhanvien")) {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/client/LG";
        }


        TaiKhoan user = taiKhoanService.findByTenDangNhap(username);
        if (user != null && user.getMat_khau().equals(password)) {
            // Lưu tên người dùng vào session
            session.setAttribute("currentUser", user.getTenDangNhap());
            System.out.println("Session được lưu với tên người dùng: " + user.getTenDangNhap());

            KhachHang khachHang = khachHangService.findByIdTaiKhoan(user.getId());
            if (khachHang != null) {
                session.setAttribute("currentUserImage", khachHang.getAnhUrl());
                System.out.println("Ảnh của người dùng: " + khachHang.getAnhUrl());
            }


            redirectAttributes.addFlashAttribute("loginStatus", "success");
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
            return "redirect:/client/LG";
        } else {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/client/LG";
        }
    }

    @PostMapping("/login2")
    public String login2(@RequestParam String username,
                         @RequestParam String password,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
        System.out.println("Đang thực hiện đăng nhập với tên người dùng: " + username);


        if (username.equalsIgnoreCase("admin") || username.equalsIgnoreCase("nhanvien")) {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/login/ad";
        }


        TaiKhoan user = taiKhoanService.findByTenDangNhap(username);
        if (user != null && user.getMat_khau().equals(password)) {
            // Lưu tên người dùng vào session
            session.setAttribute("currentUser", user.getTenDangNhap());
            System.out.println("Session được lưu với tên người dùng: " + user.getTenDangNhap());

            KhachHang khachHang = khachHangService.findByIdTaiKhoan(user.getId());
            if (khachHang != null) {
                session.setAttribute("currentUserImage", khachHang.getAnhUrl());
                System.out.println("Ảnh của người dùng: " + khachHang.getAnhUrl());
            }


            redirectAttributes.addFlashAttribute("loginStatus", "success");
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
            return "redirect:/login/ad";
        } else {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/login/ad";
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutStatus", "success");
        redirectAttributes.addFlashAttribute("message", "Đăng xuất thành công!");
        return "redirect:/client";
    }


    @GetMapping("/add")
    public String addUser(@ModelAttribute TaiKhoan user) {
        taiKhoanService.addTaiKhoan(user);
        return "redirect:/users";
    }

    @GetMapping
    public List<TaiKhoan> getAllUsers() {
        return taiKhoanService.getAll();
    }

    @GetMapping("ad")
    public String Hienthi() {
        return "/client/Loginadmin";
    }

    @GetMapping("dk")
    public String Hienthi2() {
        return "/client/Dangky";
    }

    @GetMapping("/dangky")
    public String showDangKyForm(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        model.addAttribute("khachHang", new KhachHang());
        return "/client/Dangky"; // Trả về tên file HTML "dangky.html"
    }

    @PostMapping("dangkytk")
    public String dangKy(@ModelAttribute TaiKhoan taiKhoan, @ModelAttribute KhachHang khachHang) {
        // Thiết lập trạng thái tài khoản là 1
        taiKhoan.setTrangThai(1); // Thêm dòng này
        taiKhoan.setNgayTao(LocalDate.now());
        khachHang.setTrangThai(1);
        khachHang.setNgayTao(LocalDate.now());
        khachHang.setAnhUrl("img_1.png");
        TaiKhoan savedTaiKhoan = taiKhoanService.saveTaiKhoan(taiKhoan);
        khachHang.setTaiKhoan(savedTaiKhoan);
        khachHangService.saveKhachHang(khachHang);
        return "redirect:/login/ad";
    }

}
