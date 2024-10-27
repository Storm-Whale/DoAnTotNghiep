package nhom6.duancanhan.doantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "ngay_tao")
    LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    LocalDateTime  ngaySua;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngaySua = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngaySua = LocalDateTime.now();
    }
}
