package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.KhachHangDTO;
import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.repository.ForgotKHRepository;
import nhom6.duancanhan.doantotnghiep.service.service.ForgotPasswordService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    private final KhachHangService khachHangService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    private final NhanVienService nhanVienService;

    private final ForgotKHRepository forgotKHRepository;

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
        if (user != null && user.getMatKhau().equals(password)) {
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
        } else {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        return "redirect:/client/LG";
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
        if (user != null && user.getMatKhau().equals(password)) {
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
        } else {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        return "redirect:/login/ad";
    }

    @GetMapping("/add")
    public String addUser(@ModelAttribute TaiKhoan user) {
        taiKhoanService.addTaiKhoan(user);
        return "redirect:/users";
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
    public String dangKy(@ModelAttribute TaiKhoanDTO taiKhoandt, @ModelAttribute KhachHangDTO khachHangdt) {
        taiKhoandt.setTrangThai(1); // Thêm dòng này
        taiKhoandt.setNgayTao(LocalDate.now());
        taiKhoandt.setIdvt(3);
        khachHangdt.setTrangThai(1);
        khachHangdt.setNgayTao(LocalDate.now());
        khachHangdt.setAnhUrl("img_1.png");
        TaiKhoanDTO savedTaiKhoan = taiKhoanService.saveTaiKhoan(taiKhoandt);
        khachHangdt.setTaiKhoan(savedTaiKhoan);
        forgotKHRepository.save(khachHangdt);

        return "redirect:login/check-login";
    }

    //  TODO : Vào Trang Đăng nhập
    @GetMapping(value = "/login-client")
    public String loginClient(Model model) {
        return "/client/Login";
    }

    //  TODO : Login
    @PostMapping(value = "/check-login")
    public String login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpSession session, Model model
    ) {
        if (taiKhoanService.checkAccount(username, password)) {
            TaiKhoan taiKhoan = taiKhoanService.findByTTKAndMK(username, password);
            int role = taiKhoan.getVaiTro().getId();
            switch (role){
                case 1, 2:
                    session.setAttribute("nhanvien", nhanVienService.getNhanVienByIdTaiKhoan(taiKhoan.getId()));
                    session.setAttribute("account", taiKhoan);
                    session.setAttribute("role", role);
                    session.setAttribute("loginStatus", true);  // Thêm flag
                    return "redirect:/admin";
                case 3:
                    session.setAttribute("user", khachHangService.findByIdTaiKhoan(taiKhoan.getId()));
                    session.setAttribute("account", taiKhoan);
                    session.setAttribute("role", role);
                    session.setAttribute("loginStatus", true);  // Thêm flag
                    return "redirect:/client";
            }
        } else {
            model.addAttribute("loginStatus", "Vui Lòng Xem Lại Tên Đăng Nhập Hoặc Mật Khẩu.");
            return "/client/Login";
        }
        return "/client/Login";
    }

    //   TODO : Logout
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        // Xóa session người dùng và trạng thái đăng nhập
        session.removeAttribute("user");
        session.removeAttribute("loginStatus");
        session.invalidate();

        // Xóa tất cả cookie để đảm bảo thông tin phiên được xóa hoàn toàn
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0); // Xóa cookie ngay lập tức
                response.addCookie(cookie);
            }
        }
        // Chuyển hướng về trang chính
        return "redirect:/client";
    }
}