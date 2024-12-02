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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import org.mapstruct.Mapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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
    @NotEmpty(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
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
    @Size(max = 100, message = "SDT không được vượt quá 100 ký tự")
    private String soDienThoai;

    @Column(name = "email")
    @NotBlank(message = "Email không trống")
    @Email(message = "Vui lòng cung cấp địa chỉ email hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;


    @Column(name = "anh_url")
//    @NotBlank(message = "Ảnh không được để trống")
    private String anhUrl;
//          private String anhUrl;


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

    @Transient
    private MultipartFile anhUrlFile;
}
