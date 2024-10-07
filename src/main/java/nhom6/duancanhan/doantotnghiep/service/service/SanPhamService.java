package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamService {

    List<SanPhamResponse> getAllSanPham();

    SanPhamResponse getSanPhamById(Integer id);

    SanPhamResponse storeSanPham(SanPhamRequest sanPhamRequest);

    SanPhamResponse updateSanPham(Integer id, SanPhamRequest sanPhamRequest);

    void deleteSanPham(Integer id);
}
