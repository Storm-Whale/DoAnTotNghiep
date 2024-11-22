package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class PhieuGiamGiaHoaDonDTO {
    private Integer hoaDonId;
    private LocalDateTime ngaySua;
    private String tenPhieuGiamGia;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private BigDecimal giaTriGiam;
    private BigDecimal tongTien;
    private Integer kieuGiamGia;
}
