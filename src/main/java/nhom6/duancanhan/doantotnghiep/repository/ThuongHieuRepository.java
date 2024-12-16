package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {

    @Query(value = """
                select th.tenThuongHieu from ThuongHieu th
            """)
    List<String> findAllTenThuongHieu();
<<<<<<< HEAD

    List<ThuongHieu> findAllByTrangThai(Integer trangThai);
=======
    boolean existsByTenThuongHieu(String tenThuongHieu);
>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d
}
