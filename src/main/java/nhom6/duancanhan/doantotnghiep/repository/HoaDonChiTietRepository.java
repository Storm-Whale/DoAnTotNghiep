package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
}
