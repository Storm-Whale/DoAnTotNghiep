package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhuongThucThanhToanResponse implements Serializable {
    private Integer id;
    private String phuongThucThanhToan;
    private int trangThai;
}
