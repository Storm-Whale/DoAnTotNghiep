package nhom6.duancanhan.doantotnghiep.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "hoa_don")
public class HoaDon extends BaseEntity implements Serializable {

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

    @Column(name = "email_nguoi_nhan")
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

    @Column(name = "loai_hoa_don")
    private String loaiHoaDon;

    @Column(name = "trang_thai_thanh_toan")
    private Integer trangThaiThanhToan;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    private List<HoaDonChiTiet> hoaDonChiTietList;

    // Getter và Setter cho hoaDonChiTietList
    public List<HoaDonChiTiet> getHoaDonChiTietList() {
        return hoaDonChiTietList;
    }

    public void setHoaDonChiTietList(List<HoaDonChiTiet> hoaDonChiTietList) {
        this.hoaDonChiTietList = hoaDonChiTietList;
    }

}
