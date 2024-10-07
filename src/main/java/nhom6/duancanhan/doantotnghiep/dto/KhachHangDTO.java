package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangDTO {
    private Integer id;

    private String ten;

    private LocalDate ngaySinh;

    private Integer gioiTinh;

    private String soDienThoai;

    private String email;

    private String anhUrl;

    private Integer idTaiKhoan;

    private Integer trangThai;

}
