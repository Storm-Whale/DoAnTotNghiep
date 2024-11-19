package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {


           @Query("SELECT nv FROM KhachHang nv WHERE (:keyword IS NULL OR " +
                   "LOWER(COALESCE(nv.ten, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                   "COALESCE(nv.soDienThoai, '') LIKE CONCAT('%', :keyword, '%') OR " +
                   "LOWER(COALESCE(nv.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR nv.trangThai = :trangThai)")
   Page<KhachHang> searchKhachHang(@Param(("keyword")) String keyword, @Param(("trangThai")) Integer trangThai, Pageable pageable);

   KhachHang findByTaiKhoanId(int idTaiKhoan);

   List<KhachHang> findBySoDienThoai(String soDienThoai);

   Optional<KhachHang> findByEmail(String email);
}
