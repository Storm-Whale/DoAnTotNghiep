package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
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

    List<HoaDon> findByKhachHangId(Integer khachHangId);
    List<HoaDon> findByKhachHang_IdAndTrangThai(Integer khachHangId, Integer trangThai);

    List<HoaDon> findByNguoiTao(NhanVien nguoiTao);

    // Hoặc truy vấn theo ID của NhanVien
    @Query("SELECT h FROM HoaDon h WHERE h.nguoiTao.id = :nhanVienId")
    List<HoaDon> findByNhanVienId(@Param("nhanVienId") Integer nhanVienId);
    @Query("SELECT new nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO(" +
            "hd.id, hd.ngaySua, pgg.tenPhieuGiamGia, pgg.ngayBatDau, pgg.ngayKetThuc, " +
            "pgg.giaTriGiam, hd.tongTien, pgg.kieuGiamGia) " +
            "FROM HoaDon hd " +
            "JOIN hd.phieuGiamGia pgg " +
            "WHERE pgg.id = :phieuGiamGiaId")
    List<PhieuGiamGiaHoaDonDTO> findHoaDonByPhieuGiamGia(@Param("phieuGiamGiaId") Integer phieuGiamGiaId);
    List<HoaDon> findHoaDonByKhachHangId(Integer khachHangId);
    List<HoaDon> findHoaDonByKhachHangIdAndTrangThai(Integer khachHangId, int trangThai);

//    @Query("SELECT h FROM HoaDon h " +
//            "WHERE (LOWER(h.tenNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(h.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(h.emailNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(h.diaChi.diaChiChiTiet) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(h.phuongThucThanhToan.tenPhuongThuc) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "CAST(h.tongTien AS string) LIKE :keyword OR " +  // Chuyển đổi tongTien thành chuỗi
//            "LOWER(h.ghiChu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "CAST(h.trangThai AS string) LIKE :keyword)") // Chuyển đổi trangThai thành chuỗi
//    Page<HoaDon> findByKeywordInAllFields(@Param("keyword") String keyword, Pageable pageable);
@Query("SELECT h FROM HoaDon h " +
        "WHERE (h.tenNguoiNhan LIKE CONCAT('%', :keyword, '%') OR " +
        "h.sdt LIKE CONCAT('%', :keyword, '%') OR " +
        "h.emailNguoiNhan LIKE CONCAT('%', :keyword, '%') OR " +
        "h.diaChi.diaChiChiTiet LIKE CONCAT('%', :keyword, '%') OR " +
        "h.phuongThucThanhToan.tenPhuongThuc LIKE CONCAT('%', :keyword, '%') OR " +
        "CAST(h.tongTien AS string) LIKE :keyword OR " +
        "h.ghiChu LIKE CONCAT('%', :keyword, '%') OR " +
        "CAST(h.trangThai AS string) LIKE :keyword)")
Page<HoaDon> findByKeywordInAllFields(@Param("keyword") String keyword, Pageable pageable);

}
