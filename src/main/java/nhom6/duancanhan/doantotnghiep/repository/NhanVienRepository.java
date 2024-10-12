package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Integer> {
    @Query(
            "select nv from NhanVien nv where lower(nv.ten) like lower(concat('%', :keyword, '%') ) " +
                    "or lower(nv.sdt) like lower(concat('%', :keyword, '%') ) " +
                    "or lower(nv.vaiTro.tenVaiTro) like lower(concat('%', :keyword, '%') )" )
    List<NhanVien> searchNhanVien(@Param(("keyword")) String keyword);
}
