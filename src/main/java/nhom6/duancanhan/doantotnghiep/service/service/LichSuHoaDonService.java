package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonRequest;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonResponse;

import java.util.List;

public interface LichSuHoaDonService {
    List<LichSuHoaDonResponse> getAll();
    LichSuHoaDonResponse getById(Integer id);
    LichSuHoaDonResponse create(LichSuHoaDonRequest lichSuHoaDonRequest);
    LichSuHoaDonResponse update(Integer id, LichSuHoaDonRequest lichSuHoaDonRequest);
    void delete(Integer id);
}
