package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "san_pham_gio_hang")
public class SanPhamGioHang extends BaseEntity{
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

    @Column(name = "trang_thai")
    private Integer trangThai;

    public BigDecimal tongTien() {
        return sanPhamChiTiet.getGia().multiply(BigDecimal.valueOf(soLuong));
    }

}
