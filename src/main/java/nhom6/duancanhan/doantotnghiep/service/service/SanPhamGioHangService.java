package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;

import java.util.List;

public interface SanPhamGioHangService {

    List<SanPhamGioHangResponse> getAll();

    SanPhamGioHangResponse getById(Integer id);

    SanPhamGioHangResponse create(SanPhamGioHangRequest sanPhamGioHangRequest);

    SanPhamGioHangResponse update(Integer id, SanPhamGioHangRequest sanPhamGioHangRequest);

    void delete(Integer id);
}
