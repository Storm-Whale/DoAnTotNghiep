package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kieu_co_ao")
public class KieuCoAo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Vui lòng nhập tên cổ áo!")
    @Size(max = 30, message = "Name must be 30 characters")
    @Column(name = "ten_co_ao")
    private String tenCoAo;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "coAo")
    private List<SanPham> sanPhams;
}