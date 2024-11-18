package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamShowOnClient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SanPhamService {

    List<SanPhamResponse> getAllSanPham();

    List<SanPhamResponse> getSanPhamByKieuCoAoId(Integer id); //getSP byKieuCoAoId

    List<SanPhamResponse> getSanPhamByKieuTayAoId(Integer id); //getSP byKieuCoAoId

    SanPhamResponse getSanPhamById(Integer id);

    SanPhamResponse storeSanPham(SanPhamRequest sanPhamRequest);

    SanPhamResponse updateSanPham(Integer id, SanPhamRequest sanPhamRequest);

    void deleteSanPham(Integer id);

    void sortDeleteSanPham(Integer id);

    Page<SanPhamResponse> timKiemSanPham(String keyword, Integer status, Integer thuongHieuId, Integer chatLieuId, Integer tayAoId, Integer coAoId, int page, int size);

    List<SanPhamShowOnClient> getAllSanPhamShowOnClient(String method);

    SanPhamShowOnClient getSanPhamShowOnClientById(Integer id);

    Integer getIdSpFromTenSP(String tenSanPham);

    List<SanPhamShowOnClient> searchSanPham(String tenThuongHieu, String tenChatLieu, String tenTayAo, String tenCoAo, String sort);
}
