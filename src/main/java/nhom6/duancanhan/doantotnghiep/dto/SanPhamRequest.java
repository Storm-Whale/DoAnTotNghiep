package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSanPham;

    private String anhUrl;

    @NotNull(message = "Thương hiệu không được để trống")
    private Integer idThuongHieu;

    @NotNull(message = "Chất liệu không được để trống")
    private Integer idChatLieu;

    @NotNull(message = "Tay áo không được để trống")
    private Integer idTayAo;

    @NotNull(message = "Cổ áo không được để trống")
    private Integer idCoAo;

    @NotNull(message = "Trạng thái không được để trống")
    @Min(value = 0, message = "Trạng thái phải lớn hơn hoặc bằng 0")
    @Max(value = 1, message = "Trạng thái phải nhỏ hơn hoặc bằng 1")
    private Integer trangThai;

    private String moTa;

    @NotNull(message = "Ảnh sản phẩm không được để trống")
    private MultipartFile anhSanPham;

    String qrCodeUrl;
}
