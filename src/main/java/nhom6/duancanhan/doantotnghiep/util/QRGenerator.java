package nhom6.duancanhan.doantotnghiep.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRGenerator {
    public static void generateQRCode(String data, String filePath) throws Exception{
        int width = 300; // Chiều rộng của mã QR
        int height = 300; // Chiều cao của mã QR
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
