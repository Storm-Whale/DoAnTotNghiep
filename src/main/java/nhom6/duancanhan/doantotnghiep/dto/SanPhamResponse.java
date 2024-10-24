package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamResponse {

    Integer id;

    String tenSanPham;

    String anhUrl;

    String tenThuongHieu;

    String tenChatLieu;

    String tenTayAo;

    String tenCoAo;

    Integer trangThai;

    Date ngayTao;
}
