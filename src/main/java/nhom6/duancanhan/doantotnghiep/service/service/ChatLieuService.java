package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ChatLieuService {
    List<ChatLieu> getAll();

    Page<ChatLieu> phanTrang(int pageNo, int pageSize);

    Optional<ChatLieu> detail(Integer id);

    ChatLieu addChatLieu(ChatLieu chatLieu);

    void updateChatLieu(Integer id, ChatLieu chatLieu);

    void deleteChatLieu(Integer id);
}
