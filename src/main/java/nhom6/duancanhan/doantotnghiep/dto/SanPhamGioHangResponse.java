package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SanPhamGioHangResponse implements Serializable {
    private Integer id;
    private int idGioHang;
    private String tenSanPhamChiTiet;
    private int soLuong;
}
