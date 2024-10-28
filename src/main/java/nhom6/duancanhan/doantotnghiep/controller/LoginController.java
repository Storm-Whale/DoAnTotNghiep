package nhom6.duancanhan.doantotnghiep.controller;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private TaiKhoanService service;



    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        TaiKhoan user = service.findByTenDangNhap(username);
        if (user != null && user.getMat_khau().equals(password)) {
            redirectAttributes.addFlashAttribute("loginStatus", "success");
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
            return "redirect:/client/LG"; // Chuyển hướng đến trang chủ
        } else {
            redirectAttributes.addFlashAttribute("loginStatus", "error");
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/client/LG"; // Chuyển hướng lại đến trang đăng nhập
        }
    }






    @GetMapping("/add")
    public String addUser(@ModelAttribute TaiKhoan user) {
        service.addTaiKhoan(user);
        return "redirect:/users";
    }

    @GetMapping
    public List<TaiKhoan> getAllUsers() {
        return service.getAll();
    }

}
