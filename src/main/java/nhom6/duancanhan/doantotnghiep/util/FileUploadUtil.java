package nhom6.duancanhan.doantotnghiep.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        // Lưu file
        File saveFile = new File(uploadPath, fileName);
        file.transferTo(saveFile);
    }
}
