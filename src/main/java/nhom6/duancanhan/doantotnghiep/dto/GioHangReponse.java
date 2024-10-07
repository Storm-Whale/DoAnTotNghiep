package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;

import java.io.Serializable;

@Data
public class GioHangReponse implements Serializable {
    private Integer id;
    private int idKhachHang;
}
