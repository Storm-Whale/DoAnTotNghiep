package nhom6.duancanhan.doantotnghiep.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class UploadImage {

    //   TODO : Check xem có phải img không
    public boolean isImageFile(MultipartFile multipartFile) {
        // Kiểm tra xem tệp có phải là ảnh hay không
        String contentType = multipartFile.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    //   TODO : Thêm ảnh Sản Phẩm
    public String saveImage(MultipartFile image) throws IOException {
        // Kiểm tra xem tệp có phải là ảnh không và tên tệp có null không
        if (!isImageFile(image) || image.getOriginalFilename() == null) {
            throw new IOException("Bạn chỉ có thể tải ảnh");
        }

        // Đặt thư mục lưu trữ ảnh
        String directory = "upload";

        // Lấy tên gốc của ảnh
        String fileName = image.getOriginalFilename();

        // Kiểm tra tên tệp có hợp lệ không (không null hoặc trống)
        if (fileName != null && !fileName.isEmpty()) {
            // Làm sạch tên tệp để tránh vấn đề về đường dẫn (chẳng hạn như ký tự đặc biệt)
            fileName = StringUtils.cleanPath(fileName);
        } else {
            throw new IOException("Tên ảnh là null");
        }

        // Tạo tên tệp duy nhất bằng UUID để tránh trùng lặp
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        // Tạo đường dẫn tệp để lưu ảnh vào thư mục đích
        Path filePath = Paths.get(directory, uniqueFileName);

        // Kiểm tra nếu thư mục chứa ảnh chưa tồn tại, tạo mới
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Sao chép tệp ảnh từ input stream vào thư mục đích, thay thế nếu tệp đã tồn tại
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về tên ảnh duy nhất
        return uniqueFileName;
    }

    //   TODO : Xóa ảnh sản phẩm cũ
    public void deleteOldImage(String oldImageUrl) throws IOException {
        String filePathString = "upload" + oldImageUrl;
        Path oldImagePath = Paths.get(filePathString);
        if (Files.exists(oldImagePath)) {
            Files.delete(oldImagePath);
        }
    }
}
