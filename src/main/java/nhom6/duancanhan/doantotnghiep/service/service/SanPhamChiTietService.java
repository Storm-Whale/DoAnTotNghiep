package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanPhamChiTietService {

    List<SanPhamChiTietResponse> getAllSanPhamChiTiet();

    SanPhamChiTietResponse getSanPhamChiTietById(Integer id);

    List<SanPhamChiTietResponse> getSanPhamChiTietByIdSP(Integer idSP);

    SanPhamChiTietResponse storeSanPhamChiTiet(SanPhamChiTietRequest sanPhamChiTietRequest);

    SanPhamChiTietResponse updateSanPhamChiTiet(Integer id, SanPhamChiTietRequest sanPhamChiTietRequest);

    void deleteSanPhamChiTiet(Integer id);

    void sortDeleteSanPhamChiTiet(Integer id);
}
