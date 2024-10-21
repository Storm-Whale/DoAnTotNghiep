package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE "
            + "(LOWER(p.maPhieuGiamGia) LIKE LOWER(CONCAT('%', :maPhieuGiamGia, '%')) OR "
            + " LOWER(p.tenPhieuGiamGia) LIKE LOWER(CONCAT('%', :tenPhieuGiamGia, '%'))) AND "
            + "(p.ngayBatDau >= :ngayBatDau AND p.ngayKetThuc <= :ngayKetThuc) AND "
            + "(p.kieuGiamGia = :kieuGiamGia OR :kieuGiamGia IS NULL) AND "
            + "(p.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<PhieuGiamGia> findByCriteria(
            @Param("maPhieuGiamGia") String maPhieuGiamGia,
            @Param("tenPhieuGiamGia") String tenPhieuGiamGia,
            @Param("ngayBatDau") Date ngayBatDau,
            @Param("ngayKetThuc") Date ngayKetThuc,
            @Param("kieuGiamGia") Integer kieuGiamGia,
            @Param("trangThai") Integer trangThai,
            Pageable pageable);
}
