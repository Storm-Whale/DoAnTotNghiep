package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KieuTayAoRepository extends JpaRepository<KieuTayAo, Integer> {

    @Query("SELECT k FROM KieuTayAo k WHERE k.trangThai = :trangThai")
    Page<KieuTayAo> findKieuTayAoByTrangThai(@Param("trangThai") int trangThai, Pageable pageable);

    @Query("SELECT k FROM KieuTayAo k WHERE k.tenTayAo LIKE %:tenTayAo%")
    Page<KieuTayAo> findKieuTayAoByTen(@Param("tenTayAo") String tenTayAo, Pageable pageable);

    @Query(value = """
            select kta.tenTayAo from KieuTayAo kta
        """)
    List<String> findAllTenKieuTayAo();
<<<<<<< HEAD

    List<KieuTayAo> findAllByTrangThai(int trangThai);
=======
    boolean existsByTenTayAo(String tenTayAo);

>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d
}
