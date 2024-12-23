package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.*;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.util.UploadImage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/product-details")
public class SanPhamChiTietController {

    private final UploadImage uploadImage;
    private final MauSacService mauSacService;
    private final KichCoService kichCoService;
    private final SanPhamService sanPhamService;
    private final AnhSanPhamService anhSanPhamService;
    private final SanPhamChiTietService sanPhamChiTietService;

    //    TODO : Hiển thị trang create
    @GetMapping("/create/{idsp}")
    public String create(@PathVariable(name = "idsp") Integer idsp, Model model) {
        addSanPhamChiTietModelAttributes(model, new SanPhamChiTietRequest());
        model.addAttribute("products", sanPhamService.getSanPhamById(idsp));
        return "/admin/sanphamchitiet/create";
    }

    //    TODO : Thêm sản phẩm chi tiết
    @PostMapping(value = "/store")
    @ResponseStatus(HttpStatus.CREATED)
    public void store(@ModelAttribute SanPhamChiTietCustom dto) {
        try {
            // Lưu sản phẩm chi tiết và hình ảnh tương ứng
            for (SanPhamChiTietMauSacDTO colorDetail : dto.getColorDetails()) {
                // Validate color detail
                if (colorDetail.getIdKichCos() == null || colorDetail.getIdKichCos().isEmpty()) {
                    continue;
                }
                // Lưu cho từng kích thước của màu
                for (Integer idKichCo : colorDetail.getIdKichCos()) {
                    storeSanPhamChiTietWithImages(
                            dto.getIdSP(), colorDetail.getIdMauSac(), idKichCo,
                            colorDetail.getSoLuong(), colorDetail.getGia(), colorDetail.getImages()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    TODO : Hiển thị trang edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        return checkValidate(id, model);
    }

    //    TODO : Update sản phẩm chi tiết
    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProductDetails(
            @PathVariable(value = "id") Integer id, @RequestParam(value = "id_sp") Integer idSp, @RequestParam(value = "id_mau_sac") Integer idMauSac,
            @RequestParam(value = "id_kich_co") Integer idKichCo, @RequestParam(value = "so_luong") Integer soLuong, @RequestParam(value = "gia") Double gia,
            @RequestParam(value = "trang_thai") Integer trangThai, @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "imageIds", required = false) List<Integer> imageIds
    ) throws IOException {
        // Tạo DTO cho chi tiết sản phẩm
        SanPhamChiTietRequest sanPhamChiTietRequest = buildSanPhamChiTietRequest(idSp, idMauSac, idKichCo, soLuong, gia);
        sanPhamChiTietRequest.setTrangThai(trangThai);

        // Cập nhật chi tiết sản phẩm
        SanPhamChiTietResponse sanPhamChiTietResponse = sanPhamChiTietService.updateSanPhamChiTiet(id, sanPhamChiTietRequest);
        // Kiểm tra nếu có imageIds để xóa ảnh cũ
        if (imageIds != null && !imageIds.isEmpty()) {
            // Duyệt qua từng imageId để xử lý ảnh cũ
            for (int i = 0; i < imageIds.size(); i++) {
                int imageId = imageIds.get(i); // Lấy imageId từ danh sách imageIds
                AnhSanPhamResponse anhSanPhamResponse = anhSanPhamService.getAnhSanPhamById(imageId);
                // Nếu URL ảnh không null, xóa ảnh cũ và cập nhật ảnh mới
                if (anhSanPhamResponse != null && anhSanPhamResponse.getAnhUrl() != null) {
                    String oldImageUrl = anhSanPhamResponse.getAnhUrl();
                    uploadImage.deleteOldImage(oldImageUrl); // Xóa ảnh cũ

                    // Tải ảnh mới từ danh sách images
                    if (i < images.length) {
                        MultipartFile newImage = images[i]; // Lấy ảnh mới từ images

                        // Lưu ảnh mới và lấy URL ảnh
                        String newImageUrl = uploadImage.saveImage(newImage);

                        // Tạo AnhSanPhamRequest mới để cập nhật ảnh
                        AnhSanPhamRequest anhSanPhamRequest = AnhSanPhamRequest.builder()
                                .idSanPhamChiTiet(sanPhamChiTietResponse.getId())
                                .anhUrl(newImageUrl).trangThai(1)
                                .build();

                        // Cập nhật ảnh mới vào cơ sở dữ liệu
                        anhSanPhamService.updateAnhSanPham(imageId ,anhSanPhamRequest);
                    }
                }
            }
        }
    }


    //    TODO : Chuyển trạng thái sản phẩm chi tiết
    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        int idSP = sanPhamChiTietService.sortDeleteSanPhamChiTiet(id);
        return "redirect:/admin/products/find_by_id/" + idSP;
    }

    private void addSanPhamChiTietModelAttributes(Model model, SanPhamChiTietRequest sanPhamChiTietRequest) {
        model.addAttribute("product_detail", sanPhamChiTietRequest);
        model.addAttribute("mau_sacs", mauSacService.getMauSacByTrangThai(1));
        model.addAttribute("kich_cos", kichCoService.getKichCoByTrangThai(1));
    }

    private SanPhamChiTietRequest buildSanPhamChiTietRequest(Integer idSP, Integer idMauSac, Integer idKichCo, Integer soLuong, Double gia) {
        return SanPhamChiTietRequest.builder()
                .idSanPham(idSP).idMauSac(idMauSac).idKichCo(idKichCo).soLuong(soLuong)
                .gia(BigDecimal.valueOf(gia)).trangThai(1)
                .build();
    }

    private void storeSanPhamChiTietWithImages(Integer idSP, Integer idMauSac, Integer idKichCo, Integer soLuong, Double gia, MultipartFile[] images) {
        try {
            // Tạo và lưu chi tiết sản phẩm
            SanPhamChiTietRequest request = SanPhamChiTietRequest.builder()
                    .idSanPham(idSP).idMauSac(idMauSac).idKichCo(idKichCo)
                    .soLuong(soLuong).gia(BigDecimal.valueOf(gia)).trangThai(1)
                    .build();

            SanPhamChiTietResponse response = sanPhamChiTietService.storeSanPhamChiTiet(request);

            // Lưu ảnh
            if (images != null) {
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        saveImageForSanPhamChiTiet(response.getId(), image);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu chi tiết sản phẩm", e);
        }
    }

    private void saveImageForSanPhamChiTiet(Integer sanPhamChiTietId, MultipartFile image) {
        try {
            String imageUrl = uploadImage.saveImage(image);
            AnhSanPhamRequest anhRequest = AnhSanPhamRequest.builder()
                    .idSanPhamChiTiet(sanPhamChiTietId)
                    .anhUrl(imageUrl).trangThai(1)
                    .build();
            anhSanPhamService.storeAnhSanPham(anhRequest);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh sản phẩm", e);
        }
    }

    private String checkValidate(int id, Model model) {
        SanPhamChiTietResponse sanPhamChiTietResponse = sanPhamChiTietService.getSanPhamChiTietById(id);
        addSanPhamChiTietModelAttributes(model, new SanPhamChiTietRequest());
        model.addAttribute("idSP", sanPhamService.getIdSpFromTenSP(sanPhamChiTietResponse.getTenSanPham()));
        model.addAttribute("old_product_details", sanPhamChiTietResponse);
        model.addAttribute("listAnh", anhSanPhamService.getAnhSanPhamByIdSPCT(id));
        return "/admin/sanphamchitiet/update";
    }
}