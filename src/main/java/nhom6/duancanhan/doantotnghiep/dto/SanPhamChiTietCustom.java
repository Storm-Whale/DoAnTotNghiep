package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SanPhamChiTietCustom {

    private Integer idSP;

    private List<SanPhamChiTietMauSacDTO> colorDetails;
}
