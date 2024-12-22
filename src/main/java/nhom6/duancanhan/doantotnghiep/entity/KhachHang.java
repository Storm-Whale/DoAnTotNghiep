package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "khach_hang")
public class KhachHang extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten")
    private String ten;


    @Column(name = "ngay_sinh")
    @Temporal(TemporalType.DATE)
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

    @Transient
    private MultipartFile anhUrlFile;
}
