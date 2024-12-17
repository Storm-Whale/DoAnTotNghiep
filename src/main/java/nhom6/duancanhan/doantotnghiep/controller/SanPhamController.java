package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.util.QRGeneratorCode;
import nhom6.duancanhan.doantotnghiep.util.UploadImage;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class SanPhamController {

    private final SanPhamService sanPhamService;
    private final ThuongHieuService thuongHieuService;
    private final ChatLieuService chatLieuService;
    private final KieuCoAoService kieuCoAoService;
    private final KieuTayAoService kieuTayAoService;
    private final SanPhamChiTietService sanPhamChiTietService;
    private final UploadImage uploadImage;
    private final QRGeneratorCode qrGeneratorCode;

    //  TODO : INDEX Sản Phẩm
    @GetMapping("/index")
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "thuongHieuId", required = false) Integer thuongHieuId, @RequestParam(name = "chatLieuId", required = false) Integer chatLieuId,
            @RequestParam(name = "tayAoId", required = false) Integer tayAoId, @RequestParam(name = "coAoId", required = false) Integer coAoId,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size, @PageableDefault(size = 5) Pageable pageable,
            Model model, @ModelAttribute("sp_moi") SanPhamResponse spMoi
    ) {
        // Sử dụng size từ @RequestParam để phân trang
        Page<SanPhamResponse> products = sanPhamService.timKiemSanPham(
                keyword, status, thuongHieuId, chatLieuId, tayAoId, coAoId, pageable.getPageNumber(), size
        );

        model.addAttribute("products", products.getContent());

        if (spMoi != null) {
            model.addAttribute("sp_moi", spMoi);
        }

        // Tính toán startPage và endPage
        int currentPage = products.getNumber();
        int totalPages = products.getTotalPages();
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        // Điều chỉnh nếu dải trang không đủ 5 trang
        if (endPage - startPage < 4) {
            if (startPage == 0) {
                endPage = Math.min(4, totalPages - 1);
            } else if (endPage == totalPages - 1) {
                startPage = Math.max(0, totalPages - 5);
            }
        }

        log.info("Status : {}", status);

        // Truyền các giá trị vào model
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("chatLieuId", chatLieuId);
        model.addAttribute("tayAoId", tayAoId);
        model.addAttribute("coAoId", coAoId);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);

        addSanPhamModelAttributes(model);

        return "/admin/sanpham/index";
    }

    //  TODO : Trang thêm sản phẩm
    @GetMapping("/create")
    public String create(Model model) {
        addSanPhamModelAttributes(model, new SanPhamRequest());
        return "/admin/sanpham/create";
    }

    //   TODO : Tìm tìm kiếm theo id sản phẩm
    @GetMapping("/find_by_id/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        SanPhamResponse product = sanPhamService.getSanPhamById(id);
        model.addAttribute("product", product);
        model.addAttribute("spcts", sanPhamChiTietService.getSanPhamChiTietByIdSP(id));
        return "admin/sanpham/detail";
    }

    //   TODO : Thêm Sản Phẩm
    @PostMapping("/store")
    public String store(
            @Valid @ModelAttribute("product") SanPhamRequest productRequest, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (sanPhamService.existTenSanPham(productRequest.getTenSanPham())) {
            model.addAttribute("trungTenSanPham", "Bạn đã có sản phẩm với tên này");
            return "/admin/sanpham/create";
        }

        if (!productRequest.getTenSanPham().matches("^[a-zA-Z0-9\\s]*$")) {  // Check for special characters
            model.addAttribute("invalidName", "Tên sản phẩm không được chứa ký tự đặc biệt");
            return "/admin/sanpham/create";
        }

        if (productRequest.getTenSanPham().length() > 100) {
            model.addAttribute("tenSanPhamDai", "Tên sản phẩm của bạn quá dài");
            return "/admin/sanpham/create";
        }

        if (productRequest.getTenSanPham().matches("^\\d.*")) {
            model.addAttribute("batdaubangso", "Không được bắt đầu bằng số");
            return "/admin/sanpham/create";
        }

        if (productRequest.getTenSanPham().startsWith(" ")) {
            model.addAttribute("loiDeCachHaiDau", "Không bắt đầu bằng dấu cách");
            return "/admin/sanpham/create";
        }

        if (productRequest.getAnhSanPham() != null && productRequest.getAnhSanPham().isEmpty()) {
            model.addAttribute("chuaTaiFile", "Bạn chưa có tải ảnh lên");
            return "/admin/sanpham/create";
        }

        long maxFileSize = 2 * 1024 * 1024;
        if (maxFileSize < productRequest.getAnhSanPham().getSize()) {
            model.addAttribute("loiSizeAnh", "Kích thước của ảnh không được vượt quá 2MB");
            return "/admin/sanpham/create";
        }

        if (!uploadImage.isImageFile(productRequest.getAnhSanPham())) {
            model.addAttribute("loiTypeAnh", "Chỉ có thể tải file ảnh");
            return "/admin/sanpham/create";
        }

        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
            addSanPhamModelAttributes(model, productRequest);
            return "/admin/sanpham/create";
        }

        if (productRequest.getAnhSanPham() != null && !productRequest.getAnhSanPham().isEmpty()) {
            try {
                String imageUrl = uploadImage.saveImage(productRequest.getAnhSanPham());
                productRequest.setAnhUrl(imageUrl);
            } catch (IOException e) {
                bindingResult.rejectValue("anhSanPham", "error.product", "Không thể tải ảnh lên");
                addSanPhamModelAttributes(model, productRequest);
                return "/admin/sanpham/create";
            }
        }
        String tenSP = productRequest.getTenSanPham();

        String[] words = tenSP.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        productRequest.setTenSanPham(result.toString().trim());
        productRequest.setTrangThai(1);
        SanPhamResponse sanPhamResponse = sanPhamService.storeSanPham(productRequest);
        qrGeneratorCode.generateQRCodeForProduct(productRequest);
        redirectAttributes.addFlashAttribute("sp_moi", sanPhamResponse);
        System.out.println("TO String" + sanPhamResponse.toString());
        return "redirect:/admin/products/index";
    }

    //   TODO : Trang edit Sản Phẩm
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product_old", sanPhamService.getSanPhamById(id));
        addSanPhamModelAttributes(model, new SanPhamRequest());
        return "/admin/sanpham/update";
    }

    //   TODO : Update sản phẩm
    @PostMapping(value = "/update/{id}")
    public String update(
            @PathVariable(name = "id") Integer id, @RequestParam(value = "ten_sp", required = false) String tenSP,
            @RequestParam(value = "id_thuong_hieu", required = false) Integer idThuongHieu, @RequestParam(value = "id_chat_lieu", required = false) Integer idChatLieu,
            @RequestParam(value = "id_co_ao", required = false) Integer idCoAo, @RequestParam(value = "id_tay_ao", required = false) Integer idTayAo,
            @RequestParam(value = "anh_san_pham", required = false) MultipartFile anhSanPham, @RequestParam(value = "trang_thai", required = false) Integer trangThai,
            Model model, RedirectAttributes redirectAttributes
    ) {
        boolean hasErrors = false;

        // Kiểm tra tên sản phẩm không rỗng
        if (tenSP == null || tenSP.trim().isEmpty()) {
            model.addAttribute("productNameError", "Tên sản phẩm không được để trống");
            hasErrors = true;
        } else {
            // Kiểm tra trùng tên sản phẩm
            if (sanPhamService.existTenSanPham(tenSP, id)) {
                model.addAttribute("productNameError", "Bạn đã có sản phẩm với tên này");
                hasErrors = true;
            }

            // Kiểm tra dấu cách ở đầu hoặc cuối
            if (tenSP.startsWith(" ") || tenSP.endsWith(" ")) {
                model.addAttribute("productNameError", "Tên sản phẩm không được có dấu cách ở đầu hoặc cuối");
                hasErrors = true;
            }

            if (tenSP.length() > 100) {
                model.addAttribute("productNameError", "Tên sản phẩm của bạn quá dài");
                hasErrors = true;
            }

            if (tenSP.matches("^\\d.*")) {
                model.addAttribute("productNameError", "Không được bắt đầu bằng số");
                hasErrors = true;
            }

            if (!tenSP.matches("^[a-zA-Z0-9\\s]*$")) {  // Check for special characters
                model.addAttribute("productNameError", "Tên sản phẩm không được chứa ký tự đặc biệt");
                hasErrors = true;
            }
        }

        // Kiểm tra các thuộc tính khác không được null
        if (idThuongHieu == null) {
            model.addAttribute("loiThuongHieu", "Thương hiệu không được để trống");
            hasErrors = true;
        }

        if (idChatLieu == null) {
            model.addAttribute("loiChatLieu", "Chất liệu không được để trống");
            hasErrors = true;
        }

        if (idCoAo == null) {
            model.addAttribute("loiCoAo", "Kiểu cổ không được để trống");
            hasErrors = true;
        }

        if (idTayAo == null) {
            model.addAttribute("loiTayAo", "Kiểu tay áo không được để trống");
            hasErrors = true;
        }

        if (trangThai == null) {
            model.addAttribute("loiTrangThai", "Trạng thái không được để trống");
            hasErrors = true;
        }


        long maxFileSize = 2 * 1024 * 1024;
        if (anhSanPham != null && !anhSanPham.isEmpty() && maxFileSize < anhSanPham.getSize()) {
            model.addAttribute("loiSizeAnh", "Kích thước của ảnh không được vượt quá 2MB");
            hasErrors = true;
        }

        if (anhSanPham != null && !anhSanPham.isEmpty() && !uploadImage.isImageFile(anhSanPham)) {
            model.addAttribute("loiTypeAnh", "Chỉ có thể tải file ảnh");
            hasErrors = true;
        }

        // Nếu có lỗi, trả về trang chỉnh sửa và hiển thị lỗi
        if (hasErrors) {
            // Thêm thông tin sản phẩm cũ vào Model
            model.addAttribute("product_old", sanPhamService.getSanPhamById(id));
            addSanPhamModelAttributes(model, new SanPhamRequest());
            return "/admin/sanpham/update";  // Quay lại trang chỉnh sửa với lỗi
        }

        // Xử lý logic cập nhật sản phẩm
        SanPhamResponse existingProduct = sanPhamService.getSanPhamById(id);
        String oldImageUrl = existingProduct.getAnhUrl();

        String[] words = tenSP.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        SanPhamRequest sanPhamRequest = SanPhamRequest.builder()
                .tenSanPham(result.toString().trim()).idChatLieu(idChatLieu).idThuongHieu(idThuongHieu).idCoAo(idCoAo)
                .idTayAo(idTayAo).trangThai(trangThai).build();

        if (!anhSanPham.isEmpty()) {
            try {
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    uploadImage.deleteOldImage(oldImageUrl);
                }
                String newImageUrl = uploadImage.saveImage(anhSanPham);
                sanPhamRequest.setAnhUrl(newImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sanPhamRequest.setAnhUrl(oldImageUrl);
        }

        SanPhamResponse sanPhamResponse = sanPhamService.updateSanPham(id, sanPhamRequest);
        redirectAttributes.addFlashAttribute("sp_moi", sanPhamResponse);
        return "redirect:/admin/products/index";
    }

    //   TODO : Cập Nhật Trạng Thái Sản Phẩm
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        sanPhamService.sortDeleteSanPham(id);
        return "redirect:/admin/products/index";

    }

    private void
    addSanPhamModelAttributes(Model model, SanPhamRequest sanPhamRequest) {
        model.addAttribute("product", sanPhamRequest);
        model.addAttribute("thuongHieus", thuongHieuService.getAllThuongHieuByTrangThai(1));
        model.addAttribute("chatLieus", chatLieuService.getAllChatLieuByTrangThai(1));
        model.addAttribute("kieuCoAos", kieuCoAoService.getAllKieuCoAoByTrangThai(1));
        model.addAttribute("kieuTayAos", kieuTayAoService.getAllKieuTayAoByKieuTayAo(1));
    }

    private void addSanPhamModelAttributes(Model model) {
        model.addAttribute("thuongHieus", thuongHieuService.getAllThuongHieuByTrangThai(1));
        model.addAttribute("chatLieus", chatLieuService.getAllChatLieuByTrangThai(1));
        model.addAttribute("kieuCoAos", kieuCoAoService.getAllKieuCoAoByTrangThai(1));
        model.addAttribute("kieuTayAos", kieuTayAoService.getAllKieuTayAoByKieuTayAo(1));
    }

    //  TODO: CREATE IMAGE QR FOR SP
    @GetMapping("/generate-qrcodes")
    public String generateQRCodesForAllProducts(RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.generateQRCodeForAllProducts();
            redirectAttributes.addFlashAttribute("message", "Tạo mã QR cho tất cả sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo mã QR: " + e.getMessage());
        }
        return "redirect:/admin/products/index";
    }

}
