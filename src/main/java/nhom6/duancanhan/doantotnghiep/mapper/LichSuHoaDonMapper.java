package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonRequest;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonResponse;
import nhom6.duancanhan.doantotnghiep.entity.LichSuHoaDon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LichSuHoaDonMapper {
    @Mapping(source = "hoaDon.id", target = "idHoaDon")
    LichSuHoaDonResponse toLSHoaDonResponse(LichSuHoaDon lichSuHoaDon);
    LichSuHoaDon toLSHoaDon(LichSuHoaDonRequest lichSuHoaDonRequest);
    void updateLSHoaDon(Integer lichSuHoaDonRequest, @MappingTarget LichSuHoaDonRequest lichSuHoaDon);
}
