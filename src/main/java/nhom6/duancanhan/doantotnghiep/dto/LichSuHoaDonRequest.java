package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;

import java.io.Serializable;

@Data
public class LichSuHoaDonRequest implements Serializable {
    private HoaDon hoaDon;
    @NotNull(message = "Vui lòng nhập tiêu đề hóa đơn")
    @NotEmpty(message = "Vui lòng nhập tiêu đề hóa đơn")
    private String tieuDe;
    @Min(value = 0, message = "Vui lòng chọn trạng thái")
    private int trangThai;
}
