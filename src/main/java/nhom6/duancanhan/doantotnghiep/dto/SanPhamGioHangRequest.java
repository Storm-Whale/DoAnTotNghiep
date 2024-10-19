package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;

import java.io.Serializable;

@Data
public class SanPhamGioHangRequest implements Serializable {

    private GioHang gioHang;

    private SanPhamChiTiet sanPhamChiTiet;

    @NotNull(message = "Vui lòng nhập số lượng")
    @Min(value = 1,message = "Số lượng phải lớn hơn 0")
    private int soLuong;
}
