package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/product-details")
public class SanPhamChiTietController {

    private final SanPhamChiTietService sanPhamChiTietService;

    @GetMapping(value = "/index")
    public ResponseEntity<List<SanPhamChiTietResponse>> index() {
        return new ResponseEntity<>(sanPhamChiTietService.getAllSanPhamChiTiet(), HttpStatus.OK);
    }

    @GetMapping(value = "/find_by_id/{id}")
    public ResponseEntity<SanPhamChiTietResponse> findById(@PathVariable(name = "id") Integer id) {
        SanPhamChiTietResponse product_detail = sanPhamChiTietService.getSanPhamChiTietById(id);
        return new ResponseEntity<>(product_detail, HttpStatus.OK);
    }

    @PostMapping(value = "/store")
    public ResponseEntity<?> store(
            @Valid @RequestBody SanPhamChiTietRequest sanPhamChiTietRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamChiTietResponse productDetailResponse = sanPhamChiTietService.storeSanPhamChiTiet(sanPhamChiTietRequest);
        return new ResponseEntity<>(productDetailResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name = "id") Integer id,
            @Valid @RequestBody SanPhamChiTietRequest sanPhamChiTietRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamChiTietResponse productDetailResponse = sanPhamChiTietService.updateSanPhamChiTiet(id, sanPhamChiTietRequest);
        return new ResponseEntity<>(productDetailResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        sanPhamChiTietService.deleteSanPhamChiTiet(id);
        return ResponseEntity.ok("Xóa sản phẩm chi tiết thành công với id: " + id);
    }
}