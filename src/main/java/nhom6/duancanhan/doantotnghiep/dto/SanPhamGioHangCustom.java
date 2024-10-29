package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamGioHangCustom {

    SanPhamGioHang sanPhamGioHang;

    String anhUrl;
}
