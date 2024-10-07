package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDonDTO,Integer> {
    @Query(
            value = "SELECT \n" +
                    "    hd.id, \n" +
                    "    kh.ten AS TenKH, \n" +
                    "    hd.ten_nguoi_nhan, \n" +
                    "    hd.sdt, \n" +
                    "    hd.email_nguoi_nhan, \n" +
                    "    dc.dia_chi_chi_tiet AS DiaChi, \n" +
                    "    pgg.ten_phieu_giam_gia AS PhieuGiamGia, \n" +
                    "    pttt.phuong_thuc_thanh_toan AS PhuongThucThanhToan, \n" +
                    "    hd.tong_tien, \n" +
                    "    hd.ghi_chu, \n" +
                    "    nv.ten AS NhanVien, \n" +
                    "    hd.trang_thai \n" +
                    "FROM \n" +
                    "    hoa_don hd \n" +
                    "LEFT JOIN \n" +
                    "    khach_hang kh ON hd.id_khach_hang = kh.id \n" +
                    "LEFT JOIN \n" +
                    "    dia_chi dc ON hd.id_dia_chi = dc.id \n" +
                    "LEFT JOIN \n" +
                    "    phieu_giam_gia pgg ON hd.id_phieu_giam_gia = pgg.id \n" +
                    "LEFT JOIN \n" +
                    "    phuong_thuc_thanh_toan pttt ON hd.id_thanh_toan = pttt.id \n" +
                    "LEFT JOIN \n" +
                    "    nhan_vien nv ON hd.id_nguoi_tao = nv.id;",
            nativeQuery = true
    )
    Page<HoaDonDTO> phanTrang(Pageable pageable);
    @Query(
            value = "SELECT \n" +
                    "    hd.id, \n" +
                    "    kh.ten AS TenKH, \n" +
                    "    hd.ten_nguoi_nhan, \n" +
                    "    hd.sdt, \n" +
                    "    hd.email_nguoi_nhan, \n" +
                    "    dc.dia_chi_chi_tiet AS DiaChi, \n" +
                    "    pgg.ten_phieu_giam_gia AS PhieuGiamGia, \n" +
                    "    pttt.phuong_thuc_thanh_toan AS PhuongThucThanhToan, \n" +
                    "    hd.tong_tien, \n" +
                    "    hd.ghi_chu, \n" +
                    "    nv.ten AS NhanVien, \n" +
                    "    hd.trang_thai \n" +
                    "FROM \n" +
                    "    hoa_don hd \n" +
                    "LEFT JOIN \n" +
                    "    khach_hang kh ON hd.id_khach_hang = kh.id \n" +
                    "LEFT JOIN \n" +
                    "    dia_chi dc ON hd.id_dia_chi = dc.id \n" +
                    "LEFT JOIN \n" +
                    "    phieu_giam_gia pgg ON hd.id_phieu_giam_gia = pgg.id \n" +
                    "LEFT JOIN \n" +
                    "    phuong_thuc_thanh_toan pttt ON hd.id_thanh_toan = pttt.id \n" +
                    "LEFT JOIN \n" +
                    "    nhan_vien nv ON hd.id_nguoi_tao = nv.id;",
            nativeQuery = true
    )
    List<HoaDonDTO> getAll();
}
