package nhom6.duancanhan.doantotnghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/sanphams/index")
    private String sanphamindex() {
        return "/admin/sanpham/index";
    }
}
