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
<<<<<<< HEAD

    List<ChatLieu> findAllByTrangThai(Integer trangThai);
=======
    boolean existsByTenChatLieu(String tenChatLieu);
>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d
}
