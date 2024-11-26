package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Integer> {

    List<DiaChi> findByKhachHangId(Integer khachHangId);

    @Query(value = """
            select dc from DiaChi dc where dc.khachHang.id = :khachHangId and dc.trangThai = :trangThai
        """)
    List<DiaChi> findByKhachHangId(@Param("khachHangId") Integer khachHangId, @Param("trangThai") Integer trangThai);

    Optional<DiaChi> findByDiaChiChiTiet(String diaChi);
}
