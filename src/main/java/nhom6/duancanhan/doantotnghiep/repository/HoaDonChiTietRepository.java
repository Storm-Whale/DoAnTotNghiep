package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {

    List<HoaDonChiTiet> findAllByHoaDonId(Integer hoaDonId);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.id = :id")
    List<HoaDonChiTiet> findHoaDonChiTietById(@Param("id") Integer id);

    List<HoaDonChiTiet> findByHoaDonId(Integer hoaDonId);
}
