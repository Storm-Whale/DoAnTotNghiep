package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDTO {
    private Integer id;

    private String tenDangNhap;

    private String mat_khau;

    private Integer trangThai;
}
