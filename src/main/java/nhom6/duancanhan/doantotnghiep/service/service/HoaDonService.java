package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface HoaDonService {
    List<HoaDon> getAll();

    Page<HoaDon> phanTrang(int pageNo, int pageSize);

    Optional<HoaDon> detail(Integer id);

    void addHoaDon(HoaDon hoaDon);

    void updateHoaDon(Integer id, HoaDon hoaDon);

    void deleteHoaDon(Integer id);

}
