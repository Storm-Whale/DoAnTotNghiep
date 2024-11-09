package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE " +
            "(:keyword IS NULL OR " +
            "(LOWER(p.maPhieuGiamGia) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.tenPhieuGiamGia) LIKE LOWER(CONCAT('%', :keyword, '%'))) ) AND " +
            "(:ngayBatDau IS NULL OR p.ngayBatDau >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR p.ngayKetThuc <= :ngayKetThuc) AND " +
            "(:kieuGiamGia IS NULL OR p.kieuGiamGia = :kieuGiamGia) AND " +
            "(:trangThai IS NULL OR p.trangThai = :trangThai)")
    Page<PhieuGiamGia> findByCriteria(
            @Param("keyword") String keyword, @Param("ngayBatDau") Date ngayBatDau,
            @Param("ngayKetThuc") Date ngayKetThuc, @Param("kieuGiamGia") Integer kieuGiamGia,
            @Param("trangThai") Integer trangThai, Pageable pageable);

    Optional<PhieuGiamGia> findByMaPhieuGiamGia(String maPhieuGiamGia);

    List<PhieuGiamGia> findPhieuGiamGiaByTrangThai(Integer trangThai);
}
