package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
    @NotBlank(message = "Mã phiếu giảm giá không được để trống")
    @Column(name = "ma")
    private String maPhieuGiamGia;
    @NotBlank(message = "Tên phiếu giảm giá không được để trống")
    @Size(max = 50, message = "Tên phiếu giảm giá không được vượt quá 50 ký tự")
    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @Column(name = "so_luong")
    private int soLuong;
    @NotNull(message = "Điều kiện giảm không được để trống")
    @Min(value = 100000, message = "Điều kiện giảm phải lớn hơn hoặc bằng 100,000đ")
    @Column(name = "dieu_kien")
    private BigDecimal dieuKien;

    @Column(name = "kieu_giam_gia")
    private int kieuGiamGia;
    @NotNull(message = "Ngày bắt đầu không được để trống")
//    @FutureOrPresent(message = "Ngày bắt đầu không được là ngày trong quá khứ")
    @Column(name = "ngay_bat_dau")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBatDau;
    @NotNull(message = "Ngày kết thúc không được để trống")
//    @FutureOrPresent(message = "Ngày kết thúc không được là ngày trong quá khứ")
    @Column(name = "ngay_ket_thuc")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;
    @NotNull(message = "Giá trị giảm không được để trống")
    @Min(value = 0, message = "Giá trị giảm phải lớn hơn hoặc bằng 0")
    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;
    @NotNull(message = "Giá trị tối đa không được để trống")
    @Min(value = 10000, message = "Giá trị tối đa phải lớn hơn hoặc bằng 10,000đ")
    @Column(name = "gia_tri_max")
    private BigDecimal giaTriMax;

    @Column(name = "trang_thai")
    private int trangThai;
}
