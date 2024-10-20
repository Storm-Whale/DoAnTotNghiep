package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kieu_co_ao")
public class KieuCoAo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @NotBlank(message = "Vui lòng nhập tên cổ áo!")
    @Size(max = 30, message = "Name must be 30 characters")
    @Column(name = "ten_co_ao")
    private String tenCoAo;

    @Column(name = "trang_thai")
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

    @OneToMany(mappedBy = "coAo")
    private List<SanPham> sanPhams;
}