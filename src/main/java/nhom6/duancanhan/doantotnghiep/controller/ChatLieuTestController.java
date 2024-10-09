package nhom6.duancanhan.doantotnghiep.controller;
import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/chatlieu")
public class ChatLieuTestController {
    @Autowired
    private ChatLieuService chatLieuService;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<ChatLieu> page = chatLieuService.phanTrang(pageNo, pageSize);
        List<ChatLieu> listCL = page.getContent();
        model.addAttribute("chatLieu", new ChatLieu());
        model.addAttribute("listCL", listCL);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/ChatLieu/Chatlieu";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<ChatLieu> chatLieu = chatLieuService.detail(id);
        if (chatLieu.isPresent()) {
            model.addAttribute("chatLieu", chatLieu.get());
            return "admin/sanpham/Chatlieu/Detail";
        }
        return "redirect:/admin/chatlieu";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("chatLieu") ChatLieu chatLieu, Model model) {
        chatLieu.setTrangThai(1);
        chatLieuService.addChatLieu(chatLieu);
        return "redirect:/admin/chatlieu";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("chatLieu") ChatLieu chatLieu) {
        chatLieuService.updateChatLieu(id, chatLieu);
        return "redirect:/admin/chatlieu";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, @RequestParam("_method") String method) {
        if ("delete".equals(method)) {
            chatLieuService.deleteChatLieu(id);
        }
        return "redirect:/admin/chatlieu";
    }
}
