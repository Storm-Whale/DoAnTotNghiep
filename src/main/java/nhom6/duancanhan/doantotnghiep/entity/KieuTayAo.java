package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kieu_tay_ao")
public class KieuTayAo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_tay_ao")
    private String tenTayAo;

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
    @OneToMany(mappedBy = "tayAo")
    private List<SanPham> sanPhams;
}