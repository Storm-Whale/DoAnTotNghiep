package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "dia_chi")
public class DiaChi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "id_khach_hang")
    @ManyToOne
    private KhachHang khachHang;

    @Column(name = "ten_khach_hang")
    private String tenKhachHang;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "xa")
    private String xa;

    @Column(name = "huyen")
    private String huyen;

    @Column(name = "thanh_pho")
    private String thanhPho;

    @Column(name = "dia_chi_chi_tiet")
    private String diaChiChiTiet;

    @Column(name = "trang_thai")
    private Integer trangThai;
}
