package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PhieuGiamGiaResponse implements Serializable {

    private Integer id;

    private String maPhieuGiamGia;

    private String tenPhieuGiamGia;

    private int soLuong;

    private BigDecimal dieuKien;

    private int kieuGiamGia;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private BigDecimal giaTriGiam;

    private BigDecimal giaTriMax;

    private int trangThai;
}
