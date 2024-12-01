package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ThuongHieuService {
    List<ThuongHieu> getAll();

    Page<ThuongHieu> phanTrang(int pageNo, int pageSize);

    Optional<ThuongHieu> detail(Integer id);

    ThuongHieu addThuongHieu(ThuongHieu thuongHieu);

    void updateThuongHieu(Integer id, ThuongHieu thuongHieu);

    void updateThuongHieuById(Integer id, ThuongHieu thuongHieu);


    List<String> getAllTenThuongHieu();
}
