package nhom6.duancanhan.doantotnghiep.mapper;


import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SanPhamGioHangMapper {
    @Mapping(source = "gioHang.id", target = "idGioHang")
    SanPhamGioHangResponse toSPGioHangResponse(SanPhamGioHang sanPhamGioHang);
    SanPhamGioHang toSPGioHang(SanPhamGioHangRequest sanPhamGioHangRequest);
    void updateSPGioHang(Integer sanPhamGioHangRequest, @MappingTarget SanPhamGioHangRequest sanPhamGioHang);
}
