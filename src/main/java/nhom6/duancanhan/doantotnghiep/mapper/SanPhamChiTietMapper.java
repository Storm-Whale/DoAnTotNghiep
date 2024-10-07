package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SanPhamChiTietMapper {

    @Mappings({
            @Mapping(source = "sanPham.tenSanPham", target = "tenSanPham"),
            @Mapping(source = "kichCo.tenKichCo", target = "tenKichCo"),
            @Mapping(source = "mauSac.tenMauSac", target = "tenMauSac")
    })
    SanPhamChiTietResponse toSanPhamChiTietResponse(SanPhamChiTiet sanPhamChiTiet);

    SanPhamChiTiet toSanPhamChiTiet(SanPhamChiTietRequest sanPhamChiTietRequest);

    void updateSPCTFromSCPTRequest(SanPhamChiTietRequest sanPhamChiTietRequest, @MappingTarget SanPhamChiTiet sanPhamChiTiet);
}
