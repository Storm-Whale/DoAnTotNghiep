package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.AnhSanPham;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.mapper.AnhSanPhamMapper;
import nhom6.duancanhan.doantotnghiep.repository.AnhSanPhamRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnhSanPhamServiceImpl implements AnhSanPhamService {

    private final SanPhamChiTietRepository productDetailRepository;
    private final AnhSanPhamRepository productImageRepository;
    private final AnhSanPhamMapper productImageMapper;

    @Override
    public AnhSanPhamResponse storeAnhSanPham(AnhSanPhamRequest anhSanPhamRequest) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPhamChiTiet productDetail = productDetailRepository.findById(anhSanPhamRequest.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new DataNotFoundException("Không thể tìm thấy sản phẩm chi tiết với id: "
                            + anhSanPhamRequest.getIdSanPhamChiTiet()));
            AnhSanPham productImage = productImageMapper.toAnhSanPham(anhSanPhamRequest);
            productImage.setSanPhamChiTiet(productDetail);
            return productImageMapper.toAnhSanPhamResponse(productImageRepository.save(productImage));
        }, "Lỗi khi thêm ảnh sản phẩm từ cơ sở dũ liệu");
    }
}
