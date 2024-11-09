package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaResponse;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PhieuGiamGiaMapper {

    PhieuGiamGiaResponse toPhieuGiamGiaResponse(PhieuGiamGia phieuGiamGia);
}
