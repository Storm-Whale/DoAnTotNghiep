package nhom6.duancanhan.doantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTietMauSacDTO {

    private Integer idMauSac;

    private List<Integer> idKichCos;

    private Integer soLuong;

    private Double gia;

    private MultipartFile[] images;
}
