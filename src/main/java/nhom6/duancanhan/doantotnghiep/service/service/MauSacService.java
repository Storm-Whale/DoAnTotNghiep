package nhom6.duancanhan.doantotnghiep.service.service;


import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MauSacService {

    List<MauSac> getAll();

    Page<MauSac> phanTrang(int pageNo, int pageSize);

    Optional<MauSac> detail(Integer id);

    MauSac addMauSac(MauSac mauSac);

    void updateMauSac(Integer id, MauSac mauSac);

    void updateMauSacById(Integer id, MauSac mauSac);

}
