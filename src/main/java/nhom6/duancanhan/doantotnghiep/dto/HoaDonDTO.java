package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class HoaDonDTO {
    @Id
    private Integer id;
    private String TenKH; // Khách hàng
    private String ten_nguoi_nhan;  // Người nhận
    private String sdt;           // Số điện thoại
    private String email_nguoi_nhan;         // Email

    private String DiaChi; // Địa chỉ chi tiết
    private String PhieuGiamGia; // Mã phiếu giảm giá
    private String PhuongThucThanhToan;  // Tên phương thức thanh toán
    private BigDecimal tong_tien;    // Tổng tiền
    private String ghi_chu;          // Ghi chú
    private String NhanVien;     // Tên nhân viên
    private Integer trang_thai;
}