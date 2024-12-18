package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.repository.ChatLieuRepository;

import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
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
    public void deleteChatLieu() {
        deleteChatLieu(null);
    }

    @Override
    public void deleteChatLieu(Integer id) {
        chatLieuRepository.deleteById(id);
    }

    @Override
    public List<String> getAllTenChatLieu() {
        return DatabaseOperationHandler.handleDatabaseOperation(() ->
                        chatLieuRepository.findAllTenChatLieu()
                , "Lỗi khi lấy dữ liêu từ cơ sở dữ liệu");
    }

    @Override
    public void updateChatLieuById(Integer id, ChatLieu chatLieu) {
        chatLieuRepository.save(chatLieu);
    }

    @Override
    public List<ChatLieu> getAllChatLieuByTrangThai(int trangThai) {
        return DatabaseOperationHandler.handleDatabaseOperation(() ->
                        chatLieuRepository.findAllByTrangThai(trangThai)
                , "Lỗi khi lấy dữ liêu từ cơ sở dữ liệu");
    }

    public boolean existsByTenChatLieu(String tenChatLieu) {
        return chatLieuRepository.existsByTenChatLieu(tenChatLieu);
    }

    @Override
    public Page<KichCo> timKiemVaPhanTrang(String tenChatLieu, Integer trangThai, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<KichCo> phanTrangTheoTrangThai(Integer trangThai, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<KichCo> phanTrangTheoTen(String tenChatLieu, int pageNo, int pageSize) {
        return null;
    }

}
