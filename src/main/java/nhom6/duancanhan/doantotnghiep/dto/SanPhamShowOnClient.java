package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SanPhamShowOnClient {

    SanPhamResponse sanPhamResponse;

    BigDecimal gia;
}
