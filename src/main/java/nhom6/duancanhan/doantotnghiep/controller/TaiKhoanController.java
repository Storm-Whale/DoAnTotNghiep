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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    private final KhachHangService khachHangService;

    private final ForgotPasswordService forgotPasswordService;

    private final NhanVienService nhanVienService;

    private final ForgotKHRepository forgotKHRepository;

    @GetMapping("mkpas")
    public String quenmk() {
        return "/client/Quenmk";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        return "/client/Quenmk"; // Tên file HTML (Thymeleaf)
    }

    // Gửi mã xác nhận
    @PostMapping("/forgot-password")
    public String sendResetCode(@RequestParam("email") String email, Model model) {
        String message = forgotPasswordService.sendResetCode(email);
        model.addAttribute("message", message);
        return "/client/Quenmk"; // Trả về trang quên mật khẩu với thông báo
    }

    // Hiển thị trang đặt lại mật khẩu
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("code") String code, Model model) {
        model.addAttribute("resetCode", code);
        return "/client/Quenmk"; // Tên file HTML (Thymeleaf) cho trang đặt lại mật khẩu
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("code") String resetCode,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        // Tìm tài khoản dựa trên mã xác nhận
        TaiKhoanDTO user = forgotPasswordService.findByResetCode(resetCode);

        if (user != null) {
            // Nếu mã xác nhận hợp lệ, cập nhật mật khẩu
            user.setMat_khau(newPassword); // Cập nhật mật khẩu mới
            user.setResetCode(null); // Xóa mã xác nhận sau khi sử dụng
            user.setNgaySua(LocalDate.now());
            forgotPasswordService.saveTaiKhoan(user); // Lưu thay đổi vào CSDL

            model.addAttribute("message", "Mật khẩu đã được đặt lại thành công.");
        } else {
            model.addAttribute("message", "Mã xác nhận không hợp lệ.");
        }

        return "/client/Quenmk"; // Trả về trang quên mật khẩu
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
    public String loginClient(HttpSession session, HttpServletRequest request,Model model) {
        String requestUrl = (String) session.getAttribute("requestUrl");
        if (requestUrl != null) {
            session.removeAttribute("requestUrl");
            return "redirect:" + requestUrl;
        }
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