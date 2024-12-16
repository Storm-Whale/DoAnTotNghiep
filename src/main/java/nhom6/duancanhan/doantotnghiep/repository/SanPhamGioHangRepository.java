package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamGioHangRepository extends JpaRepository<SanPhamGioHang, Integer> {
      List<SanPhamGioHang> findByGioHangIdAndTrangThai(Integer gioHangId, Integer trangThai);

      boolean existsBySanPhamChiTietIdAndTrangThaiAndGioHangId(Integer sanPhamChiTietId, Integer trangThai, Integer gioHangId);

      SanPhamGioHang findSanPhamGioHangByGioHangIdAndSanPhamChiTietIdAndTrangThai(Integer gioHangId, Integer sanPhamChiTietId, Integer trangThai);
}
