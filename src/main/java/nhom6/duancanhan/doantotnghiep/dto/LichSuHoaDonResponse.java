package nhom6.duancanhan.doantotnghiep.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LichSuHoaDonResponse implements Serializable {
    private Integer id;
    private int idHoaDon;
    private String tieuDe;
    private int trangThai;
}
