package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KieuCoAoRepository extends JpaRepository<KieuCoAo, Integer> {

    //    @Query("SELECT k.tenCoAo, s.tenSanPham, k.ngayTao " +
//            "FROM KieuCoAo k " +
//            "JOIN SanPham s ON s.id_co_ao = k.id " +
//            "WHERE k.id = :id")
//    List<KieuCoAo> findKieuCoAoById(@Param("id") Integer id);
//    @Query("SELECT k FROM KieuCoAo k JOIN k.sanPhams s WHERE k.id = :id")
//    List<KieuCoAo> findKieuCoAoById(@Param("id") Integer id);
    @Query("SELECT k FROM KieuCoAo k WHERE k.trangThai = :trangThai")
    Page<KieuCoAo> findByTrangThai(@Param("trangThai") int trangThai, Pageable pageable);


    @Query("SELECT k FROM KieuCoAo k WHERE k.tenCoAo LIKE %:tenCoAo%")
    Page<KieuCoAo> findKieuCoAoByTen(@Param("tenCoAo") String tenCoAo, Pageable pageable);

    @Query("SELECT k FROM KieuCoAo k WHERE " +
            "(LOWER(k.tenCoAo) LIKE LOWER(CONCAT('%', :tenCoAo, '%')) OR :tenCoAo IS NULL OR :tenCoAo = '') " +
            "AND (k.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<KieuCoAo> findByCriteria(@Param("tenCoAo") String tenCoAo,
                                  @Param("trangThai") Integer trangThai,
                                  Pageable pageable);

    List<KieuCoAo> findByTenCoAoContainingIgnoreCase(String tenCoAo);



    @Query(value = """
            select kca.tenCoAo from KieuCoAo kca 
        """)
    List<String> findAllTenKieuCoAo();
}
