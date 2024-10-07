package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.AnhSanPham;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/product-images")
public class AnhSanPhamController {

    private final AnhSanPhamService anhSanPhamService;
    private final SanPhamChiTietService sanPhamChiTietService;

    @PostMapping(value = "/upload-image/{productDetailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> store(
            @PathVariable(name = "productDetailId") Integer productDetailId,
            @RequestParam(name = "image") List<MultipartFile> imageFiles
    ) throws IOException {
        SanPhamChiTietResponse existingProductDetail = sanPhamChiTietService.getSanPhamChiTietById(productDetailId);
        List<AnhSanPhamResponse> listProductImage = new ArrayList<>();
        if (imageFiles == null || imageFiles.isEmpty()) {
            return new ResponseEntity<>("No image files provided", HttpStatus.BAD_REQUEST);
        }
        if (imageFiles.size() > AnhSanPham.MAXIMUM_IMAGES_PER_PRODUCT) {
            return new ResponseEntity<>("You can only upload a maximum of 5 images", HttpStatus.BAD_REQUEST);
        }
        for (MultipartFile file : imageFiles) {
            if (file != null && file.getSize() > 0) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return new ResponseEntity<>("File is too large! Maximum size is 10MB",
                            HttpStatus.PAYLOAD_TOO_LARGE);
                }
                if (!isImageFile(file)) {
                    return new ResponseEntity<>("File must be an image", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                }
                String fileName = storeFile(file);
                listProductImage.add(anhSanPhamService.storeAnhSanPham(
                        AnhSanPhamRequest.builder()
                                .idSanPhamChiTiet(existingProductDetail.getId())
                                .anhUrl(fileName)
                                .trangThai(0)
                                .build()));
            }
        }
        return new ResponseEntity<>(listProductImage, HttpStatus.OK);
    }

    private boolean isImageFile(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new RuntimeException("You can only upload image file");
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {// +
            filename = StringUtils.cleanPath(filename);
        } else {
            throw new RuntimeException("Filename is null");
        }

        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        Path uploadDir = Paths.get("upload");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}