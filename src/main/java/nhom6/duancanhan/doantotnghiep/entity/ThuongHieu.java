package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "thuong_hieu")
public class ThuongHieu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_thuong_hieu")
    private String tenThuongHieu;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "thuongHieu")
    private List<SanPham> sanPhams;
}