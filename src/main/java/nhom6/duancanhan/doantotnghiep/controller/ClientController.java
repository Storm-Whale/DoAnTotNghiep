package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final SanPhamService sanPhamService;

    @GetMapping("")
    private String trangchuindex() {
        return "/client/trangchu";
    }

    @GetMapping("2")
    private String trangchu2index(Model model) {
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient());
        return "/client/trangchu2";
    }
}
