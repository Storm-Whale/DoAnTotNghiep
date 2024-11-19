package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "khach_hang")
public class KhachHang extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten")
    @NotEmpty(message = "Tên không được để trống")
    @Size(max = 30, message = "Name must be 30 characters")
    private String ten;


    @Column(name = "ngay_sinh")
    @NotNull(message = "Ngày sinh không trống")
    @Past(message = "Ngày sinh của bạn không pải ngày hiện tại và tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;


    @Column(name = "gioi_tinh")
    @NotNull(message = "Giới tính không trống")
    @Min(value = 0, message = "Invalid gender value")
    @Max(value = 1, message = "Invalid gender value")
    private Integer gioiTinh;


    @Column(name = "sdt")
    @Pattern(regexp = "^[0-9]{10}$", message = "SDT phải là 10 số và không được trống")
    private String soDienThoai;

    @Column(name = "email")
    @NotBlank(message = "Email không trống")
    @Email(message = "Vui lòng cung cấp địa chỉ email hợp lệ")
    private String email;


    @Column(name = "anh_url")
    private String anhUrl;


    @JoinColumn(name = "id_tai_khoan")
    @ManyToOne
    private TaiKhoan taiKhoan;

    @Column(name = "trang_thai")
    @NotNull(message = "Trạng thái không trống")
    @Min(value = 0, message = "Invalid gender value")
    @Max(value = 1, message = "Invalid gender value")
    private Integer trangThai;
}
