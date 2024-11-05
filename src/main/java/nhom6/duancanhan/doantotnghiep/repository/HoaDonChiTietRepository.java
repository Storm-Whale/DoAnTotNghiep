package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
    List<HoaDonChiTiet> findAllByHoaDonId(Integer hoaDonId);
    List<HoaDonChiTiet> findHoaDonChiTietById(Integer id);
}
