package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamChiTietResponse {

    Integer id;

    String tenSanPham;

    String tenKichCo;

    String tenMauSac;

    BigDecimal gia;

    Integer soLuong;

    Integer trangThai;
}
