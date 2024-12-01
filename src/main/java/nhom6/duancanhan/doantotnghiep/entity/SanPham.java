package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ten_san_pham", length = 20)
    private String tenSanPham;

    @Column(name = "anh_url")
    private String anhUrl;

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "id_chat_lieu")
    private ChatLieu chatLieu;

    @ManyToOne
    @JoinColumn(name = "id_tay_ao")
    private KieuTayAo tayAo;

    @ManyToOne
    @JoinColumn(name = "id_co_ao")
    private KieuCoAo coAo;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;
}