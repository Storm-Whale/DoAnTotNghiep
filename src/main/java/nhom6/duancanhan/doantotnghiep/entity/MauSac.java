package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
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
@Table(name = "mau_sac")
public class MauSac extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Tên màu sắc không được để trống!")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Tên màu sắc không được chứa ký tự đặc biệt!")
    @Pattern(regexp = "^[^\\s].*[^\\s]$", message = "Tên màu sắc không được chứa khoảng trắng ở đầu hoặc cuối!")
    @Size(max = 19, message = "Name must be 20 characters")
    @Column(name = "ten_mau_sac",unique = true)
    private String tenMauSac;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "mauSac")
    private List<SanPhamChiTiet> sanPhamChiTiets;

}