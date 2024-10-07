package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class LichSuHoaDonResponse implements Serializable {
    private Integer id;
    private int idHoaDon;
    private String tieuDe;
    private int trangThai;
}
