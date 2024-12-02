package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {

            @Query("SELECT nv FROM NhanVien nv WHERE (:keyword IS NULL OR " +
                    "LOWER(COALESCE(nv.ten, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "COALESCE(nv.sdt, '') LIKE CONCAT('%', :keyword, '%') OR " +
                    "LOWER(COALESCE(nv.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                    "AND (:trangThai IS NULL OR nv.trangThai = :trangThai)")
    Page<NhanVien> searchNhanVien(@Param(("keyword")) String keyword, @Param(("trangThai")) Integer trangThai, Pageable pageable
    );

    NhanVien findByTaiKhoanId(Integer idTaiKhoan);

    @Query("SELECT k FROM NhanVien k WHERE "
            + "(:keyword IS NULL OR k.ten LIKE %:keyword%) AND "
            + "(:trangThai IS NULL OR k.trangThai = :trangThai) OR (:keyword IS NULL OR k.sdt LIKE %:keyword%) AND (:trangThai IS NULL OR k.trangThai = :trangThai) OR (:keyword IS NULL OR k.email LIKE %:keyword%) AND (:trangThai IS NULL OR k.trangThai = :trangThai)")
    Page<NhanVien> findByKeywordAndTrangThai(
            @Param("keyword") String keyword,
            @Param("trangThai") Integer trangThai,
            Pageable pageable);

    boolean existsBySdt(String sdt);
}
