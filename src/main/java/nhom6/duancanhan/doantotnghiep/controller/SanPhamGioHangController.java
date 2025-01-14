package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/san-pham-gio-hang")
public class SanPhamGioHangController {

    private final SanPhamGioHangRepository sanPhamGioHangRepository;

    //  TODO : Tăng Số Lượng Sản Phẩm Chi Tiết Trong Sản Phẩm Giỏ Hàng
    @PostMapping(value = "/plus-spgh/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void plusSPGH(@PathVariable int idspgh) {
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int oldSoLuong = sanPhamGioHang.getSoLuong();
        int idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
        sanPhamGioHang.setSoLuong(oldSoLuong + 1);
        sanPhamGioHangRepository.save(sanPhamGioHang);
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
    }

    //  TODO : Xóa Sản Phẩm Chi Tiết Khỏi Sản Phẩm Gio Hàng
    @PostMapping(value = "/delete-spgh/{idspgh}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteSanPhamGioHang(@PathVariable(name = "idspgh") int idspgh) {
        SanPhamGioHang sanPhamGioHang = findSanPhamGioHangById(idspgh);
        int soluong = sanPhamGioHang.getSoLuong();
        Integer idspct = sanPhamGioHang.getSanPhamChiTiet().getId();
    }

    //  * Hàm tìm kiếm sản phẩm giỏ hàng by id
    private SanPhamGioHang findSanPhamGioHangById(int idspgh) {
        return sanPhamGioHangRepository.findById(idspgh)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với giỏ hàng nào"));
    }
}
