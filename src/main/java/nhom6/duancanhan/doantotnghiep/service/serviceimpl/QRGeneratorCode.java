package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamRepository;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
@Service
public class QRGeneratorCode {

    private final SanPhamRepository sanPhamRepository;
    private final SanPhamService sanPhamService;


    public QRGeneratorCode(SanPhamRepository sanPhamRepository, SanPhamService sanPhamService) {
        this.sanPhamRepository = sanPhamRepository;
        this.sanPhamService = sanPhamService;
    }
        private static final String QR_CODE_IMAGE_PATH = "D:/FALL_2024/DATN/DoAnTotNghiep/upload/";

        //create image qr cho sp mới được thêm vào csdl
        public void generateQRCodeForProduct(SanPhamRequest productRequest) {
            try {
                String qrCodeData = "product:" + productRequest.getTenSanPham();  // Hoặc thông tin sản phẩm bạn muốn mã hóa vào QR code
                String filePath = QR_CODE_IMAGE_PATH + productRequest.getTenSanPham() + ".png";  // Tên file QR code dựa trên tên sản phẩm

                // Tạo mã QR từ dữ liệu thành 1 bảng bit
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);

                // Lưu bảng bit thành mã QR vào file
                Path path = FileSystems.getDefault().getPath(filePath);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

                // Cập nhật URL mã QR vào sản phẩm
                productRequest.setQrCodeUrl(filePath);
                sanPhamService.storeSanPham(productRequest);

                System.out.println("Đã tạo mã QR cho sản phẩm: " + productRequest.getTenSanPham() + " tại " + filePath);
            } catch (Exception e) {
                System.err.println("Lỗi khi tạo QR cho sản phẩm: " + productRequest.getTenSanPham());
                e.printStackTrace();
            }
        }
}
