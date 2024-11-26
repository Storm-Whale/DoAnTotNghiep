package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SanPhamChiTietService {

    List<SanPhamChiTietResponse> getAllSanPhamChiTiet();

    SanPhamChiTietResponse getSanPhamChiTietById(Integer id);

    List<SanPhamChiTietResponse> getSanPhamChiTietByIdSP(Integer idSP);

    SanPhamChiTietResponse storeSanPhamChiTiet(SanPhamChiTietRequest sanPhamChiTietRequest);

    SanPhamChiTietResponse updateSanPhamChiTiet(Integer id, SanPhamChiTietRequest sanPhamChiTietRequest);

    Page<SanPhamChiTiet> timKiemSanPham(String keyword, Integer thuongHieuId, Integer chatLieuId, Integer tayAoId,
                                        Integer coAoId, Integer kichCoId, Integer mauSacId, Integer trangThai, int page, int size);

    void deleteSanPhamChiTiet(Integer id);

    void sortDeleteSanPhamChiTiet(Integer id);

    List<SanPhamChiTiet> findByIdSPCT(Integer id);

    List<SanPhamChiTiet> getMauSacId(Integer id);

    List<SanPhamChiTiet> getKichCoId(Integer id);
}
