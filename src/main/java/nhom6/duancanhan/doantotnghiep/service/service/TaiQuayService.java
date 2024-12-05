package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.TaiQuay;
import nhom6.duancanhan.doantotnghiep.repository.TaiQuayRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaiQuayService {
    Page<TaiQuay> phanTrang(int pageNo, int pageSize);
}
