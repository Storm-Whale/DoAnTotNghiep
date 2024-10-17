package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.DATE)
    Date ngayTao;

    @Column(name = "ngay_sua")
    @Temporal(TemporalType.DATE)
    Date ngaySua;

    @PrePersist
    protected void onCreate() {
        ngayTao = new Date();
        ngaySua = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        ngaySua = new Date();
    }
}
