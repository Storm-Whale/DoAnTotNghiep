package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SanPhamGioHangResponse implements Serializable {
    private Integer id;
    private int idGioHang;
    private String tenSanPhamChiTiet;
    private int soLuong;
}
