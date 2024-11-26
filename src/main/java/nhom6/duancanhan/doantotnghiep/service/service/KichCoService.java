package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KichCoService {
    List<KichCo> getAll();

    Page<KichCo> phanTrang(int pageNo, int pageSize);

    Optional<KichCo> detail(Integer id);

    KichCo addKichCo(KichCo kichCo);

    void updateKichCo(Integer id, KichCo kichCo);

    void updateKichCoById(Integer id, KichCo kichCo);
}
