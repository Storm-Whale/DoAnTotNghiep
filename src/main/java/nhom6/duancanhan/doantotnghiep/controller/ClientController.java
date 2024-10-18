package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final SanPhamService sanPhamService;

    @GetMapping("")
    private String trangchuindex(Model model) {
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient());
        return "/client/trangchu";
    }

    @GetMapping("/san_pham_chi_tiet/{id}")
    private String sanPhamChiTiet(@PathVariable int id, Model model) {
        return "/client/chitiet";
    }
    @GetMapping("chitiet")
    private String trangct() {
        return "/client/chitiet";
    }
}
