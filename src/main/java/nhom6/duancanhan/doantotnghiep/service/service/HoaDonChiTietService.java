package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietService {
    List<HoaDonChiTiet> getAll();

    Page<HoaDonChiTiet> phanTrang(int pageNo, int pageSize);

    Optional<HoaDonChiTiet> detail(Integer id);

    void addHoaDon(HoaDonChiTiet hoaDon);

    void updateHoaDon(Integer id, HoaDonChiTiet hoaDon);

    void deleteHoaDon(Integer id);
}
