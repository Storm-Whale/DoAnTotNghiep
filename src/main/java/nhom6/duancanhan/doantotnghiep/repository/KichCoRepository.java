package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, Integer> {
    @Query("SELECT k FROM KichCo k WHERE k.trangThai = :trangThai")
    Page<KichCo> findByTrangThai(@Param("trangThai") int trangThai, Pageable pageable);

    @Query("SELECT k FROM KichCo k WHERE k.tenKichCo LIKE %:tenKichCo%")
    Page<KichCo> findKichCoByTen(@Param("tenKichCo") String tenKichCo, Pageable pageable);

    @Query("SELECT k FROM KichCo k WHERE " +
            "(LOWER(k.tenKichCo) LIKE LOWER(CONCAT('%', :tenKichCo, '%')) OR :tenKichCo IS NULL OR :tenKichCo = '') " +
            "AND (k.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<KichCo> findByCriteria(@Param("tenKichCo") String tenKichCo,
                                  @Param("trangThai") Integer trangThai,
                                  Pageable pageable);

    boolean existsByTenKichCo(String tenKichCo);

    List<KichCo> findAllBytrangThai(Integer trangThai);
}
