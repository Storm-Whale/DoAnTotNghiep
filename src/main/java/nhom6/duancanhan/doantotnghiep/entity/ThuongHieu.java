package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Tên thương hiệu không được để trống!")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Tên thương hiệu không được chứa ký tự đặc biệt!")
    @Pattern(regexp = "^[^\\s].*[^\\s]$", message = "Tên thương hiệu không được chứa khoảng trắng ở đầu hoặc cuối!")
    @Size(max = 19, message = "Name must be 20 characters")
    @Column(name = "ten_thuong_hieu",unique = true)
    private String tenThuongHieu;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "thuongHieu")
    private List<SanPham> sanPhams;
}