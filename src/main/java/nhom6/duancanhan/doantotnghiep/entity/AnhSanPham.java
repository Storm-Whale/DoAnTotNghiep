package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "anh_san_pham")
public class AnhSanPham extends BaseEntity{

    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "anh_url")
    private String anhUrl;

    @ManyToOne
    @JoinColumn(name = "id_spct")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "trang_thai")
    private Integer trangThai;
}