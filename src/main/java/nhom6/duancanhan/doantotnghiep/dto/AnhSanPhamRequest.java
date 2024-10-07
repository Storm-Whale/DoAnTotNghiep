package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnhSanPhamRequest {

    private String anhUrl;

    private Integer idSanPhamChiTiet;

    private Integer trangThai;
}