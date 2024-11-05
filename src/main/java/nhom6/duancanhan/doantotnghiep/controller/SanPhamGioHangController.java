package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.util.ChangeNumberOfDetailProduct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/san-pham-gio-hang")
public class SanPhamGioHangController {

    private final SanPhamGioHangRepository sanPhamGioHangRepository;
    private final ChangeNumberOfDetailProduct changeNumberOfDetailProduct;

    //  TODO : Tăng Số Lượng Sản Phẩm Chi Tiết Trong Sản Phẩm Giỏ Hàng
    @PostMapping(value = "/plus-spgh/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void plusSPGH(@PathVariable int idspgh) {
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int oldSoLuong = sanPhamGioHang.getSoLuong();
        int idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
        sanPhamGioHang.setSoLuong(oldSoLuong + 1);
        sanPhamGioHangRepository.save(sanPhamGioHang);
        changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, 1, "-");
    }

    //  TODO : Giảm Số Lượng Sản Phẩm Chi Tiết Trong Sản Phẩm Giỏ Hàng
    @PostMapping(value = "/minus-spgh/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void minus(@PathVariable int idspgh) {
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int oldSoLuong = sanPhamGioHang.getSoLuong();
        int idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
        sanPhamGioHang.setSoLuong(oldSoLuong - 1);
        sanPhamGioHangRepository.save(sanPhamGioHang);
        changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, 1, "+");
    }

    //  TODO : Tăng Or Giảm Số Lượng Sản Phẩm Chi Tiết Trong Sản Phẩm Giỏ Hàng
    @PostMapping(value = "/update-number-of-product/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void updateNumberOfProduct(
            @PathVariable(name = "idspgh") int idspgh,
            @RequestBody Map<String, Integer> requestBody
    ) {
        int numberOfProduct = requestBody.get("quantity");
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int oldSoLuong = sanPhamGioHang.getSoLuong();
        int idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
        sanPhamGioHang.setSoLuong(numberOfProduct);
        sanPhamGioHangRepository.save(sanPhamGioHang);

        //  What For : So sánh để tăng or giảm số lượng trong sản phẩm chi tiết
        if (numberOfProduct > oldSoLuong) {
            int quantityDifference = numberOfProduct - oldSoLuong;
            changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, quantityDifference, "-");
        } else {
            int quantityDifference = oldSoLuong - numberOfProduct;
            changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, quantityDifference, "+");
        }
    }

    //  TODO : Xóa Sản Phẩm Chi Tiết Khỏi Sản Phẩm Gio Hàng
    @PostMapping(value = "/delete-spgh/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteSanPhamGioHang(@PathVariable(name = "idspgh") int idspgh) {
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int soluong = sanPhamGioHang.getSoLuong();
        Integer idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
        changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, soluong, "+");
    }

    //  * Hàm tìm kiếm sản phẩm giỏ hàng by id
    private SanPhamGioHang findSanPhamGioHangById(int idspgh) {
        return sanPhamGioHangRepository.findById(idspgh)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với giỏ hàng nào"));
    }
}
