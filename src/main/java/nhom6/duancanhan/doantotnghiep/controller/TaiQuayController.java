package nhom6.duancanhan.doantotnghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/taiquay")
public class TaiQuayController {
    @GetMapping("")
    public String showIndex(){
        return "/admin/BanhangTaiQuay/index";
    }
}
