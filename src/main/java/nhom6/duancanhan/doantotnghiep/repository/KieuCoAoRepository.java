package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KieuCoAoRepository extends JpaRepository<KieuCoAo, Integer> {

    //    @Query("SELECT k.tenCoAo, s.tenSanPham, k.ngayTao " +
//            "FROM KieuCoAo k " +
//            "JOIN SanPham s ON s.id_co_ao = k.id " +
//            "WHERE k.id = :id")
//    List<KieuCoAo> findKieuCoAoById(@Param("id") Integer id);
    @Query("SELECT k FROM KieuCoAo k JOIN k.sanPhams s WHERE k.id = :id")
    List<KieuCoAo> findKieuCoAoById(@Param("id") Integer id);

    @Query(value = """
            select kca.tenCoAo from KieuCoAo kca 
        """)
    List<String> findAllTenKieuCoAo();
}
