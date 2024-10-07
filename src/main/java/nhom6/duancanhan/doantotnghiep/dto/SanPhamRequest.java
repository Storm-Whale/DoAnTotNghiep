package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamRequest {

    String tenSanPham;

    String anhUrl;

    Integer idThuongHieu;

    Integer idChatLieu;

    Integer idTayAo;

    Integer idCoAo;

    Integer trangThai;
}
