package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
//    @Column(name = "phuong_thuc")
//    private String phuongThuc;
    @Column(name = "trang_thai")
    private int trangThai;
    @Column(name = "phuong_thuc_thanh_toan")
    private String tenPhuongThuc;
}
