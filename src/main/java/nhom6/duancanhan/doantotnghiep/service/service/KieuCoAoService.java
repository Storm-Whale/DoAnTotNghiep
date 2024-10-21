package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KieuCoAoService {
    List<KieuCoAo> getAll();

    Page<KieuCoAo> phanTrang(int pageNo, int pageSize);

    Optional<KieuCoAo> detail(Integer id);

    void addKieuCoAo(KieuCoAo kieuTayAo);

    void updateKieuCoAo(Integer id, KieuCoAo kieuCoAo);

    void updateKieuCoAoById(Integer id, KieuCoAo kieuCoAo);
}
