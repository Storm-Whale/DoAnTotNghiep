package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KieuCoAoService {

    List<KieuCoAo> getAll();

    Page<KieuCoAo> phanTrang(int pageNo, int pageSize);

    Optional<KieuCoAo> detail(Integer id);

    KieuCoAo addKieuCoAo(KieuCoAo kieuTayAo);

    void updateKieuCoAo(Integer id, KieuCoAo kieuCoAo);

    void updateKieuCoAoById(Integer id, KieuCoAo kieuCoAo);

    Page<KieuCoAo> phanTrangTheoTrangThai(int trangThai, int pageNo, int pageSize);

    Page<KieuCoAo> phanTrangTheoTen(String tenCoAo, int pageNo, int pageSize);

    Page<KieuCoAo> timKiemVaPhanTrang(String tenCoAo, Integer trangThai, int page, int size);

    List<String> getAllTenKieuCoAo();
<<<<<<< HEAD

    List<KieuCoAo> getAllKieuCoAoByTrangThai(int trangThai);
=======
    boolean existsByTenCoAo(String tenCoAo);
>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d
}
