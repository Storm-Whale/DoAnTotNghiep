package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SanPhamMapper {

    @Mappings({
            @Mapping(source = "thuongHieu.tenThuongHieu", target = "tenThuongHieu"),
            @Mapping(source = "chatLieu.tenChatLieu", target = "tenChatLieu"),
            @Mapping(source = "tayAo.tenTayAo", target = "tenTayAo"),
            @Mapping(source = "coAo.tenCoAo", target = "tenCoAo"),
            @Mapping(source = "ngayTao", target = "ngayTao")
    })
    SanPhamResponse toSanPhamResponse(SanPham sanPham);

    SanPham toSanPham(SanPhamRequest sanPhamRequest);

    void updateSanPhamFromSanPhamRequest(SanPhamRequest sanPhamRequest, @MappingTarget SanPham sanPham);
}
