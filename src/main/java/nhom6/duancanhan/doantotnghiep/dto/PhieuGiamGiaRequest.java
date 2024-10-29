package nhom6.duancanhan.doantotnghiep.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PhieuGiamGiaRequest implements Serializable {
    @NotNull(message = "Vui lòng nhập mã phiều giảm giá")
    @NotEmpty(message = "Vui lòng nhập mã phiều giảm giá")
    @NotBlank(message = "Vui lòng nhập mã phiều giảm giá")
    private String maPhieuGiamGia;
    @NotNull(message = "Vui lòng nhập tên phiếu giảm giá")
    @NotEmpty(message = "Vui lòng nhập tên phiếu giảm giá")
    @NotBlank(message = "Vui lòng nhập tên phiếu giảm giá")
    private String tenPhieuGiamGia;
    @NotNull(message = "Vui lòng nhập số lượng")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private int soLuong;
    @NotNull(message = "Vui lòng nhập điều kiện")
    @Min(value = 0, message = "Vui lòng nhập điều kiện hợp lệ")
    private int dieuKien;
    @NotNull(message = "Vui lòng nhập kiểu giảm giá")
    @Min(value = 0, message = "Vui lòng nhập kiểu giảm giá hợp lệ")
    private int kieuGiamGia;
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    private Date ngayBatDau;
    @Future(message = "Ngày kết thúc phải là tương lai")
    private Date ngayKetThuc;
    @NotNull(message = "Vui lòng nhập giá trị min")
    @DecimalMin(value = "0.0", message = "Giá trị tối thiểu phải lớn hơn hoặc bằng 0")
    private double giaTriMin;
    @NotNull(message = "Vui lòng nhập giá trị max")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá trị tối đa phải lớn hơn hoặc bằng 0")
    private double giaTriMax;
    @Min(value = 0, message = "Vui lòng chọn trạng thái")
    private int trangThai;
}
