package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hoa_don")
public class HoaDon {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id")
    private KhachHang khachHang;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email_nguoi_nhan", length = 255)
    private String emailNguoiNhan;

    @ManyToOne
    @JoinColumn(name = "id_dia_chi", referencedColumnName = "id")
    private DiaChi diaChi;

    @ManyToOne
    @JoinColumn(name = "id_phieu_giam_gia", referencedColumnName = "id")
    private PhieuGiamGia phieuGiamGia;

    @ManyToOne
    @JoinColumn(name = "id_thanh_toan", referencedColumnName = "id")
    private PhuongThucThanhToan phuongThucThanhToan;

    @Column(name = "tong_tien", precision = 10, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_tao", referencedColumnName = "id")
    private NhanVien nguoiTao;

    @Column(name = "trang_thai")
    private int trangThai;

}
