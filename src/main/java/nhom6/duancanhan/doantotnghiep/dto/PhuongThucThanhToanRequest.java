package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Data
public class PhuongThucThanhToanRequest implements Serializable {
    private String phuongThucThanhToan;
    @Min(value = 0, message = "Vui lòng chọn trạng thái")
    private int trangThai;
}
