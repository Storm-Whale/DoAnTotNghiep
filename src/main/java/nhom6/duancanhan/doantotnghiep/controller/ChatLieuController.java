package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/chatlieu")
public class ChatLieuController {

    @Autowired
    private final ChatLieuService chatLieuService;
    final SanPhamService sanPhamService;

    public ChatLieuController(ChatLieuService chatLieuService, SanPhamService sanPhamService) {
        this.chatLieuService = chatLieuService;
        this.sanPhamService = sanPhamService;
    }

    @GetMapping("")
    public String getAll(Model model) {
        int defaultPageSize = 5;
        return phanTrang(1, defaultPageSize, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int pageSize,
                            Model model) {
        Page<ChatLieu> page = chatLieuService.phanTrang(pageNo, pageSize);
        List<ChatLieu> listCL = page.getContent();
        model.addAttribute("chatLieu", new ChatLieu());
        model.addAttribute("listCL", listCL);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("size", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/sanpham/ChatLieu/Chatlieu";
    }
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        Optional<ChatLieu> chatLieu = chatLieuService.detail(id);
        List<SanPhamResponse> sanPhams = sanPhamService.getSanPhamByChatLieuId(id);
        if (chatLieu.isPresent()) {
            model.addAttribute("chatLieu", chatLieu.get());
            model.addAttribute("sanPhams", sanPhams);
            return "/admin/sanpham/ChatLieu/Detail";
        }
        return "redirect:/admin/chatlieu";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("chatLieu") @Valid ChatLieu chatLieu,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên chất liệu");
            String tenChatLieu = chatLieu.getTenChatLieu();
            if (chatLieu.getTenChatLieu().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên chất liệu không được để trống!");
                return "redirect:/admin/chatlieu";
            }

            // Check leading/trailing whitespaces
            if (!chatLieu.getTenChatLieu().trim().equals(chatLieu.getTenChatLieu())) {
                bindingResult.rejectValue("tenChatLieu", "tenChatLieu.whitespace", "Tên chất liệu không được chứa khoảng trắng ở đầu hoặc cuối!");
            }
            // Check length
            else if (tenChatLieu.length() > 20) {
                redirectAttributes.addFlashAttribute("error", "Tên chất liệu không được quá 20 kí tự!");
                return "redirect:/admin/chatlieu";
            }
            // Check for special characters
            else if (chatLieu.getTenChatLieu().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                redirectAttributes.addFlashAttribute("error", "Tên chất liệu không được chứa ký tự đặc biệt!");
                return "redirect:/admin/chatlieu";
            }

            // Check duplicates (implement database logic)
            else if (chatLieuService.existsByTenChatLieu(tenChatLieu)) {
                redirectAttributes.addFlashAttribute("error", "Tên chất liệu đã tồn tại!");
                return "redirect:/admin/chatlieu";
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.chatLieu", bindingResult);
            redirectAttributes.addFlashAttribute("chatLieu", chatLieu);
            // Nếu có lỗi validation, trả lại form với thông báo lỗi
            return "redirect:/admin/chatlieu";
        }
        chatLieu.setTrangThai(1);
        chatLieuService.addChatLieu(chatLieu);
        return "redirect:/admin/chatlieu";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<ChatLieu> chatLieu = chatLieuService.detail(id);
        if (chatLieu.isPresent()) {
            model.addAttribute("chatLieu", chatLieu.get());
            return "/admin/sanpham/ChatLieu/Update";
        }
        return "redirect:/admin/chatlieu";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @ModelAttribute("chatLieu") @Valid ChatLieu chatLieu,
                         BindingResult bindingResult) {
        Optional<ChatLieu> existingChatLieuOpt = chatLieuService.detail(id);
        if (bindingResult.hasErrors() || existingChatLieuOpt.isEmpty()) {
            return "/admin/sanpham/ChatLieu/Update";
        }
            ChatLieu existingChatLieu = existingChatLieuOpt.get();
            chatLieu.setNgayTao(existingChatLieu.getNgayTao());
            chatLieu.setId(existingChatLieu.getId());
            chatLieuService.updateChatLieu(id, chatLieu);
            return "redirect:/admin/chatlieu";
    }

    @PostMapping("/updatett/{id}")
    public String updateTrangthai(@PathVariable("id") Integer id) {
        Optional<ChatLieu> optionalChatLieu = chatLieuService.detail(id);
        if (optionalChatLieu.isPresent()) {
            ChatLieu chatLieu = optionalChatLieu.get();
            chatLieu.setTrangThai(1);
            chatLieuService.updateChatLieuById(id, chatLieu);
        }
        return "redirect:/admin/chatlieu";
    }

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            ChatLieu chatLieu = ChatLieu.builder()
                    .tenChatLieu(ten)
                    .trangThai(1)
                    .build();

            ChatLieu saveChatLieu = chatLieuService.addChatLieu(chatLieu);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", saveChatLieu.getId());
            response.put("ten", saveChatLieu.getTenChatLieu());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
