package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    boolean existsSanPhamByTenSanPham(String tenSanPham);

    boolean existsSanPhamByTenSanPhamAndIdNot(String tenSanPham, Integer id);

    Page<SanPham> findByTenSanPhamContainingAndTrangThai(String tenSanPham, Integer trangThai, Pageable pageable);

    Page<SanPham> findByTenSanPhamContaining(String tenSanPham, Pageable pageable);

    Page<SanPham> findByTrangThai(Integer trangThai, Pageable pageable);
}
