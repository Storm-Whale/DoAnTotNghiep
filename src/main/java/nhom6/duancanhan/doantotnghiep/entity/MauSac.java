package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mau_sac")
public class MauSac extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_mau_sac")
    private String tenMauSac;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "mauSac")
    private List<SanPhamChiTiet> sanPhamChiTiets;

}