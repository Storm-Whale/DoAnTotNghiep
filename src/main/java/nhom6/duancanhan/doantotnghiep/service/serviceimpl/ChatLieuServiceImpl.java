package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.repository.ChatLieuRepository;

import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatLieuServiceImpl implements ChatLieuService {
    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Override
    public List<ChatLieu> getAll() {
        return chatLieuRepository.findAll();
    }

    @Override
    public Page<ChatLieu> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.chatLieuRepository.findAll(pageable);
    }

    @Override
    public Optional<ChatLieu> detail(Integer id) {
        return chatLieuRepository.findById(id);
    }

    @Override
    public ChatLieu addChatLieu(ChatLieu chatLieu) {
        return chatLieuRepository.save(chatLieu);
    }

    @Override
    public void updateChatLieu(Integer id, ChatLieu chatLieu) {
        chatLieuRepository.save(chatLieu);
    }

    @Override
    public void deleteChatLieu(Integer id) {
        chatLieuRepository.deleteById(id);
    }
}
