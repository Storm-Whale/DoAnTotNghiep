package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "san_pham_gio_hang")
public class SanPhamGioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_gio_hang")
    private GioHang gioHang;
    @ManyToOne
    @JoinColumn(name = "id_spct")
    private SanPhamChiTiet sanPhamChiTiet;
    @Column(name = "so_luong")
    private int soLuong;

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

    public BigDecimal tongTien() {
        return sanPhamChiTiet.getGia().multiply(BigDecimal.valueOf(soLuong));
    }

}
