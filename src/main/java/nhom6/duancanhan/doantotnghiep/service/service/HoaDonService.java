package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
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

    List<HoaDon> findByKhachHangId(Integer khachHangId);

    List<HoaDon> findByNhanVienId(Integer nhanVienId);
    public Page<HoaDon> findHoaDonByStatus(String status, int pageNo, int pageSize);
    public Page<HoaDon> findHoaDonByLoaiHoaDon(String loaiHoaDon, int pageNo, int pageSize);
    public Page<HoaDon> searchHoaDon(String keyword, int pageNo, int pageSize);
    public Page<HoaDonDTO> phanTrang2(int page, int pageSize);

}
