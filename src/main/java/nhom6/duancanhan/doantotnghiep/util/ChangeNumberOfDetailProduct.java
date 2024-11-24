package nhom6.duancanhan.doantotnghiep.util;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeNumberOfDetailProduct {

    private final SanPhamChiTietRepository sanPhamChiTietRepository;
//    TODO: Hàm Thay Đổi Số Lượng Sản Phẩm Chi Tiết
    public void updateProductDetailQuantity(Integer idSPCT, Integer soLuong, String operation) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSPCT)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm chi tiết với id : " + idSPCT));
        Integer soLuongInDetailProduct = sanPhamChiTiet.getSoLuong();
        switch (operation) {
            case "+":
                sanPhamChiTiet.setSoLuong(soLuongInDetailProduct + soLuong);
                break;
            case "-":
                if (soLuongInDetailProduct < soLuong) {
                    throw new IllegalArgumentException("Số lượng trong kho không đủ");
                }
                sanPhamChiTiet.setSoLuong(soLuongInDetailProduct - soLuong);
                if (soLuongInDetailProduct - soLuong == 0) {
                    sanPhamChiTiet.setTrangThai(0);
                }
                break;
            default:
                throw new IllegalArgumentException("Không tồn tại phương thức này");
        }
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
}
