package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDonChiTietResponse {

    int id;

    int idHoaDon;

    int idSpct;

    int soLuong;

    BigDecimal gia;
}
