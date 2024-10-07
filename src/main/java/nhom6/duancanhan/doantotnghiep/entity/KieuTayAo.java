package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kieu_tay_ao")
public class KieuTayAo {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_tay_ao")
    private String tenTayAo;

    @Column(name = "trang_thai")
    private Integer trangThai;

}