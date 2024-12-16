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
    @Query(value = "(SELECT * FROM hoa_don WHERE id = 1) " +
            "UNION " +
            "(SELECT * FROM hoa_don WHERE id != 1 ORDER BY id DESC LIMIT 4)",
            nativeQuery = true)
    List<HoaDon> findHoaDonWithId1AndTop4Newest();

    @Query("SELECT h FROM HoaDon h WHERE h.trangThai = 1")
    List<HoaDon> findHoaDonsWithStatusOne();

    List<HoaDon> findByKhachHangId(Integer khachHangId);

    List<HoaDon> findByKhachHang_IdAndTrangThai(Integer khachHangId, Integer trangThai);
//    List<HoaDon> findByKhachHang_IdAndTrangThai(Integer khachHangId);

    @Query("SELECT h FROM HoaDon h WHERE h.nguoiTao.id = :nhanVienId")
    List<HoaDon> findByNhanVienId(@Param("nhanVienId") Integer nhanVienId);

    @Query("SELECT new nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO(" +
            "hd.id, hd.ngaySua, pgg.tenPhieuGiamGia, pgg.ngayBatDau, pgg.ngayKetThuc, " +
            "pgg.giaTriGiam, hd.tongTien, pgg.kieuGiamGia) " +
            "FROM HoaDon hd " +
            "JOIN hd.phieuGiamGia pgg " +
            "WHERE pgg.id = :phieuGiamGiaId")
    List<PhieuGiamGiaHoaDonDTO> findHoaDonByPhieuGiamGia(@Param("phieuGiamGiaId") Integer phieuGiamGiaId);

    Page<HoaDon> findByKhachHang_IdAndTrangThai(Integer khachHangId, Integer trangThai, Pageable pageable);
    Page<HoaDon> findByKhachHang_Id(Integer khachHangId, Pageable pageable);

    Page<HoaDon> findByNguoiTao_Id(Integer nguoiTaoId, Pageable pageable);

    Page<HoaDon> findHoaDonByKhachHangId(Integer khachHangId, Pageable pageable);


    Page<HoaDon> findHoaDonByKhachHangIdAndTrangThai(Integer khachHangId, int trangThai, Pageable pageable);

    @Query("SELECT hd FROM HoaDon hd JOIN hd.diaChi dc JOIN hd.phuongThucThanhToan ptt WHERE " +
            "(hd.tenNguoiNhan LIKE %:keyword% OR hd.sdt LIKE %:keyword% OR hd.emailNguoiNhan LIKE %:keyword% OR " +
            "dc.diaChiChiTiet LIKE %:keyword% OR ptt.tenPhuongThuc LIKE %:keyword% OR " +
            "CAST(hd.tongTien AS string) LIKE %:keyword% OR hd.ghiChu LIKE %:keyword%)")
    Page<HoaDon> findByKeywordInAllFields(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT h FROM HoaDon h WHERE (:loaiHoaDon = 'all' OR h.loaiHoaDon = :loaiHoaDon)")
    Page<HoaDon> findByLoaiHoaDon(@Param("loaiHoaDon") String loaiHoaDon, Pageable pageable);

    @Query("SELECT h FROM HoaDon h " +
            "LEFT JOIN h.phuongThucThanhToan ptt " +
            "WHERE (:tenPhuongThuc = 'all' OR ptt.tenPhuongThuc = :tenPhuongThuc)")
    Page<HoaDon> findByTenPhuongThuc(@Param("tenPhuongThuc") String tenPhuongThuc, Pageable pageable);

    @Query("SELECT h FROM HoaDon h " +
            "LEFT JOIN h.khachHang k " +
            "LEFT JOIN h.diaChi dc " +
            "WHERE (k.ten LIKE %:keyword% OR dc.soDienThoai LIKE %:keyword%)")
    Page<HoaDon> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT h FROM HoaDon h " +
            "LEFT JOIN FETCH h.khachHang " +
            "LEFT JOIN FETCH h.diaChi " +
            "LEFT JOIN FETCH h.phuongThucThanhToan " +
            "WHERE h.trangThai = :trangThai",
            countQuery = "SELECT COUNT(h) FROM HoaDon h WHERE h.trangThai = :trangThai")
    Page<HoaDon> findByTrangThai(@Param("trangThai") Integer trangThai, Pageable pageable);

    Page<HoaDon> findByTrangThai(int trangThai, Pageable pageable);

    List<HoaDon> findByTrangThai(int trangThai);
}
