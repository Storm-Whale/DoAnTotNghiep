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
@Table(name = "kich_co")
public class KichCo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_kich_co")
    private String tenKichCo;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "kichCo")
    private List<SanPhamChiTiet> sanPhamChiTiets;
}