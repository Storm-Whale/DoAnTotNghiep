package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import nhom6.duancanhan.doantotnghiep.util.ValidationErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    @GetMapping(value = "/index")
    public ResponseEntity<List<SanPhamResponse>> index() {
        List<SanPhamResponse> products = sanPhamService.getAllSanPham();
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/find_by_id/{id}")
    public ResponseEntity<SanPhamResponse> findById(@PathVariable(name = "id") Integer id) {
        SanPhamResponse product = sanPhamService.getSanPhamById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/store")
    public ResponseEntity<?> store(@Valid @RequestBody SanPhamRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamResponse productResponse = sanPhamService.storeSanPham(productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name = "id") Integer id,
            @Valid @RequestBody SanPhamRequest productRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationErrorHandler.handleValidationErrors(bindingResult);
        }
        SanPhamResponse productResponse = sanPhamService.updateSanPham(id, productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công với id: " + id);
    }
}