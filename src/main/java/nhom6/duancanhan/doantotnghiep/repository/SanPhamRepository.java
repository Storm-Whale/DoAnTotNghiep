package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    boolean existsSanPhamByTenSanPham(String tenSanPham);

    boolean existsSanPhamByTenSanPhamAndIdNot(String tenSanPham, Integer id);
}
