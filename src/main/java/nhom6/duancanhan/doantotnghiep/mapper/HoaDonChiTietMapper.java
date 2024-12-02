package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface HoaDonChiTietMapper {

    @Mappings({
            @Mapping(source = "hoaDon.id", target = "idHoaDon"),
            @Mapping(source = "sanPhamChiTiet.id", target = "idSpct")
    })
    HoaDonChiTietResponse toHoaDonChiTietResponse (HoaDonChiTiet hoaDonChiTiet);
}
