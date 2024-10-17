package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

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

    @Column(name= "ngay_tao")
    LocalDate ngayTao;

    @Column(name = "ngay_sua")
    LocalDate ngaySua;

    @PrePersist
    protected void onCreateCreate() {
        ngayTao = LocalDate.now();
        ngaySua = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdateUpdate() {
        ngaySua = LocalDate.now();
    }

}
