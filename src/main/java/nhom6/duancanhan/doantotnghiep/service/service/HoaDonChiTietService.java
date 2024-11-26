package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietService {
    List<HoaDonChiTiet> getAll();

    Page<HoaDonChiTiet> phanTrang(int pageNo, int pageSize);

    Optional<HoaDonChiTiet> detail(Integer id);

    void addHoaDon(HoaDonChiTiet hoaDon);

    void updateHoaDon(Integer id, HoaDonChiTiet hoaDon);

    void deleteHoaDon(Integer id);

    public List<HoaDonChiTiet> getHoaDonChiTietByHoaDonId(Integer hoaDonId);

    public List<HoaDonChiTiet> findByHoaDonId(Integer hoaDonId);

    List<HoaDonChiTiet> findByKhachHangId(Integer khachHangId);
}
