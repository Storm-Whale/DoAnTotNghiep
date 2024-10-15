package nhom6.duancanhan.doantotnghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @GetMapping("")
    private String trangchuindex() {
        return "/client/trangchu";
    }

    @GetMapping("2")
    private String trangchu2index() {
        return "/client/trangchu2";
    }
}
