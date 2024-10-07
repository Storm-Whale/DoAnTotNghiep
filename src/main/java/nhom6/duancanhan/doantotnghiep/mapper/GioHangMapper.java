package nhom6.duancanhan.doantotnghiep.mapper;

import nhom6.duancanhan.doantotnghiep.dto.GioHangReponse;
import nhom6.duancanhan.doantotnghiep.dto.GioHangRequest;
import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GioHangMapper {
    @Mapping(source = "khachHang.id", target = "idKhachHang")
    GioHangReponse toGioHangResponse(GioHang gioHang);
    GioHang toGioHang(GioHangRequest gioHangRequest);
    void updateGioHangRequest(Integer gioHangRequest, @MappingTarget GioHangRequest gioHang);

}
