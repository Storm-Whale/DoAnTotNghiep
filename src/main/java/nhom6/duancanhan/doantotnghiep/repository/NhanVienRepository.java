package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {
    @Query(
            "SELECT nv FROM NhanVien nv WHERE (:keyword IS NULL OR " +
                    "lower(nv.ten) LIKE lower(concat('%', :keyword, '%')) OR " +
                    "nv.sdt LIKE concat('%', :keyword, '%') OR " +
                    "nv.email LIKE concat('%', :keyword, '%')) " +
                    "AND (:trangThai IS NULL OR nv.trangThai = :trangThai)")
    Page<NhanVien> searchNhanVien(@Param(("keyword")) String keyword,
                                  @Param(("trangThai")) Integer trangThai,
                                  Pageable pageable
    );
}
