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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten")
    @NotBlank(message = "Tên không trống")
    @Size(max = 50, message = "Ten không được vượt quá 50 ký tự")
    private String  ten;

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
//    @NotBlank(message = "SDT không trống")
    @Size(max = 50, message = "TDT không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[0-9]{10}$", message = "SDT phải là 10 số và không được trống")
    private String sdt;

    @Column(name = "email")
    @NotBlank(message = "Email không trống")
    @Email(message = "Vui lòng cung cấp địa chỉ email hợp lệ")
    @Size(max = 50, message = "Email không được vượt quá 50 ký tự")
    private String email;

    @Column(name = "anh_url")
    private String anhUrl;


    @JoinColumn(name = "id_tai_khoan")
    @ManyToOne
    private TaiKhoan taiKhoan;

    @JoinColumn(name = "id_vai_tro")
    @ManyToOne
    private VaiTro vaiTro;

    @Column(name = "trang_thai")
    @NotNull(message = "Trạng thái không trống")
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

    @Transient
    private MultipartFile anhUrlFile;
}
