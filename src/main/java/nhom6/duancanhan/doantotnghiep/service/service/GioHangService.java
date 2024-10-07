package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.GioHangReponse;
import nhom6.duancanhan.doantotnghiep.dto.GioHangRequest;

import java.util.List;

public interface GioHangService {
    List<GioHangReponse> getAll();
    GioHangReponse getById(Integer id);
    GioHangReponse create(GioHangRequest gioHangRequest);
    GioHangReponse update(Integer id, GioHangRequest gioHangRequest);
    void delete(Integer id);
}
