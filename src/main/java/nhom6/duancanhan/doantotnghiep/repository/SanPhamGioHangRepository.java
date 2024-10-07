package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamGioHangRepository extends JpaRepository<SanPhamGioHang, Integer> {
}
