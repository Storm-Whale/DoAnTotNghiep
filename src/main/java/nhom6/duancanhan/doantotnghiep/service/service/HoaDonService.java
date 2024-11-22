package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.dto.ProductDetail;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface HoaDonService {

    List<HoaDon> getAll();

    Page<HoaDon> phanTrang(int pageNo, int pageSize);

    Optional<HoaDon> detail(Integer id);


    void addHoaDon(HoaDon hoaDon);

    void updateHoaDon(Integer id, HoaDon hoaDon);

    void deleteHoaDon(Integer id);

    Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize);

    void cancelHoaDon(Integer id);
    public HoaDon findById(Integer id);

    List<PhieuGiamGiaHoaDonDTO> getHoaDonByPhieuGiamGia(Integer phieuGiamGiaId);
}
