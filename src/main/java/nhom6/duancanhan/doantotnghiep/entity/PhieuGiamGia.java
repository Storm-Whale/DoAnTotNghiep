package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "ma")
    private String maPhieuGiamGia;
    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;
    @Column(name = "so_luong")
    private int soLuong;
    @Column(name = "dieu_kien")
    private BigDecimal dieuKien;
    @Column(name = "kieu_giam_gia")
    private int kieuGiamGia;
    @Column(name = "ngay_bat_dau")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBatDau;
    @Column(name = "ngay_ket_thuc")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;
    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;
    @Column(name = "gia_tri_max")
    private BigDecimal giaTriMax;
    @Column(name = "trang_thai")
    private int trangThai;
}
