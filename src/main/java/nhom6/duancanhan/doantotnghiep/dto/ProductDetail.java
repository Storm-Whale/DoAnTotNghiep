package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
    private String tenSanPham;
    private String tenKichCo;
    private String tenMauSac;
    private int soLuong;
    private BigDecimal gia;
}
