package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamRequest {

    @NotBlank(message = "Không được để trống")
    String tenSanPham;

    String anhUrl;

    Integer idThuongHieu;

    Integer idChatLieu;

    Integer idTayAo;

    Integer idCoAo;

    Integer trangThai;

    String moTa;

    MultipartFile anhSanPham;
    String qrCodeUrl;
}
