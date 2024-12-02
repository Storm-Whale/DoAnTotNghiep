package nhom6.duancanhan.doantotnghiep.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "gia")
    private BigDecimal gia;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;

    public BigDecimal tongTien() {
        return sanPhamChiTiet.getGia().multiply(BigDecimal.valueOf(soLuong));
    }

    public String getTenSanPham() {
        return sanPhamChiTiet.getSanPham().getTenSanPham();
    }

}
