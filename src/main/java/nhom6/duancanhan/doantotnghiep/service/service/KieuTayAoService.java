package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KieuTayAoService {
    List<KieuTayAo> getAll();

    Page<KieuTayAo> phanTrang(int pageNo, int pageSize);

    Optional<KieuTayAo> detail(Integer id);

    KieuTayAo addKieuTayAo(KieuTayAo kieuTayAo);

    void updateKieuTayAo(Integer id, KieuTayAo kieuTayAo);

    void updateKieuTayAoById(Integer id, KieuTayAo kieuTayAo);

    void deleteKieuTayAo(Integer id);

    List<KieuTayAo> getKieuTayAoByTrangThai(int trangThai);

    List<KieuTayAo> getKieuTayAoByTen(String tenTayAo);

    Page<KieuTayAo> phanTrangTheoTrangThai(int trangThai, int pageNo, int pageSize);

    Page<KieuTayAo> phanTrangTheoTen(String tenTayAo, int pageNo, int pageSize);
    List<String> getAllKieuTayAo();
}
