package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.AnhSanPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AnhSanPhamMapper {

    @Mapping(source = "sanPhamChiTiet.id", target = "idSanPhamChiTiet")
    AnhSanPhamResponse toAnhSanPhamResponse(AnhSanPham anhSanPham);

    AnhSanPham toAnhSanPham(AnhSanPhamRequest anhSanPhamRequest);
}
