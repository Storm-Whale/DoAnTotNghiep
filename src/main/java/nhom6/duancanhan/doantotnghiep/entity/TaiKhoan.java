package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    @Column(name = "mat_khau")
    private String mat_khau;
    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "trang_thai")
    private Integer trangThai;
    @Column(name = "ngay_sua")
    private LocalDate ngaySua;
    @Column(name = "reset_code")
    private String resetCode;
}
