package nhom6.duancanhan.doantotnghiep.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamChiTietRequest {

    Integer idSanPham;

    Integer idKichCo;

    Integer idMauSac;

    BigDecimal gia;

    Integer soLuong;

    Integer trangThai;
}
