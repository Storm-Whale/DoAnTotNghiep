package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ChatLieuController {
    @Autowired
    private ChatLieuService chatLieuService;

    @GetMapping("/chat-lieu/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(chatLieuService.getAll());
    }

    @GetMapping("/chat-lieu/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<ChatLieu> page = chatLieuService.phanTrang(pageNo,pageSize);
        List<ChatLieu> listCL = page.getContent();
        return ResponseEntity.ok(listCL);
    }

    @GetMapping("/chat-lieu/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(chatLieuService.detail(id));
    }

    @PostMapping("/chat-lieu/add")
    public ResponseEntity<?> add(@RequestBody ChatLieu chatLieu) {
        chatLieuService.addChatLieu(chatLieu);
        return ResponseEntity.ok(chatLieu);
    }

    @PutMapping("/chat-lieu/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody ChatLieu chatLieu) {
        chatLieuService.updateChatLieu(id, chatLieu);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/chat-lieu/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        chatLieuService.deleteChatLieu(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }
}
