package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamChiTietRequest {

    Integer idSanPham;

    Integer idKichCo;

    Integer idMauSac;

    BigDecimal gia;

    Integer soLuong;

    Integer trangThai;
}
