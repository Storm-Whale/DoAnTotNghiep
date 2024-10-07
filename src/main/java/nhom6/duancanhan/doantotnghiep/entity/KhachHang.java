package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten")
    private String ten;


    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;


    @Column(name = "gioi_tinh")
    private Integer gioiTinh;


    @Column(name = "sdt")
    private String soDienThoai;

    @Column(name = "email")
    private String email;


    @Column(name = "anh_url")
    private String anhUrl;


    @JoinColumn(name = "id_tai_khoan")
    @ManyToOne
    private TaiKhoan taiKhoan;


    @Column(name = "trang_thai")
    private Integer trangThai;

}
