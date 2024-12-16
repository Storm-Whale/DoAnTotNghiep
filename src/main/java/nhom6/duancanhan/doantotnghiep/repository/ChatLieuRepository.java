package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {

    @Query(value = """
            select cl.tenChatLieu from ChatLieu cl
        """)
    List<String> findAllTenChatLieu();

    List<ChatLieu> findAllByTrangThai(Integer trangThai);
}
