package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Page<HoaDon> findByTenNguoiNhanContaining(String keyword, Pageable pageable);

    // Lấy hóa đơn đầu tiên (theo thứ tự tăng dần của ID)
    HoaDon findFirstByOrderByIdAsc();

    // Lấy 4 hóa đơn mới nhất (theo thứ tự giảm dần của ID)
    List<HoaDon> findTop4ByOrderByIdDesc();
    @Query(value = "SELECT * FROM hoa_don ORDER BY id DESC LIMIT 4", nativeQuery = true)
    List<HoaDon> findTop4ByOrderByIdDescNative();
    @Query(value = "(SELECT * FROM hoa_don WHERE id = 1) " +
            "UNION " +
            "(SELECT * FROM hoa_don WHERE id != 1 ORDER BY id DESC LIMIT 4)",
            nativeQuery = true)
    List<HoaDon> findHoaDonWithId1AndTop4Newest();
    @Query("SELECT h FROM HoaDon h WHERE h.trangThai = 1")
    List<HoaDon> findHoaDonsWithStatusOne();
    @Query("SELECT COUNT(h) FROM HoaDon h")
    long demSoLuongHoaDon();
    @Query("SELECT new nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO(" +
            "hd.id, hd.ngaySua, pgg.tenPhieuGiamGia, pgg.ngayBatDau, pgg.ngayKetThuc, " +
            "pgg.giaTriGiam, hd.tongTien, pgg.kieuGiamGia) " +
            "FROM HoaDon hd " +
            "JOIN hd.phieuGiamGia pgg " +
            "WHERE pgg.id = :phieuGiamGiaId")
    List<PhieuGiamGiaHoaDonDTO> findHoaDonByPhieuGiamGia(@Param("phieuGiamGiaId") Integer phieuGiamGiaId);

}
