package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.PhuongThucThanhToan;

import java.util.List;
import java.util.Optional;

public interface PhuongThucThanhToanService {
    List<PhuongThucThanhToan> getAll();
    Optional<PhuongThucThanhToan> getById(Integer id);
    void create(PhuongThucThanhToan phuongThucThanhToan);
    void update(Integer id, PhuongThucThanhToan phuongThucThanhToan);
    void delete(Integer id);
}
