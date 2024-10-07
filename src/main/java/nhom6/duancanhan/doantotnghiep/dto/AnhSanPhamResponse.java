package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnhSanPhamResponse {

    private Integer id;

    private String anhUrl;

    private Integer idSanPhamChiTiet;

    private Integer trangThai;
}