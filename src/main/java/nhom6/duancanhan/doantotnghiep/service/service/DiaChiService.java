package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DiaChiService {
    List<DiaChi> getAll();

    Page<DiaChi> phanTrang(int pageNo, int pageSize);

    Optional<DiaChi> detail(Integer id);

    void addDiaChi(DiaChi diaChi);

    void updateDiaChi(Integer id, DiaChi diaChi);

    void deleteDiaChi(Integer id);
}
