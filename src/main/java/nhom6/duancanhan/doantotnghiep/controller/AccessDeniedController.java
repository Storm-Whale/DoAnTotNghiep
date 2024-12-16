package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/access-denied")
    public String accessDenied(Model model, HttpSession session) {
        model.addAttribute("errorMessage", "Bạn cần đăng nhập với vai trò admin để thực hiện thao tác này.");
        return "/admin/BanhangTaiQuay/accessdenied"; // Tạo view tương ứng
    }
}
