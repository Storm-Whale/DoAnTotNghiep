package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface HoaDonService {

    List<HoaDon> getAll();

    Page<HoaDon> phanTrang(int pageNo, int pageSize);
//public Page<HoaDon> phanTrang(PageRequest pageRequest);

    Optional<HoaDon> detail(Integer id);


    HoaDon addHoaDon(HoaDon hoaDon);

    void updateHoaDon(Integer id, HoaDon hoaDon);

    void deleteHoaDon(Integer id);

    Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize);

    void cancelHoaDon(Integer id);

    HoaDon findById(Integer id);

    List<PhieuGiamGiaHoaDonDTO> getHoaDonByPhieuGiamGia(Integer phieuGiamGiaId);

    List<HoaDon> findByKhachHangId(Integer khachHangId);

    List<HoaDon> findByNhanVienId(Integer nhanVienId);
    public Page<HoaDon> findHoaDonByStatus(String status, int pageNo, int pageSize);
    public Page<HoaDon> findHoaDonByLoaiHoaDon(String loaiHoaDon, int pageNo, int pageSize);
    public Page<HoaDon> searchHoaDon(String keyword, int pageNo, int pageSize);

    public Page<HoaDon> getHoaDonByTrangThai(Integer trangThai, int page, int size);
    public Page<HoaDon> phanTrangTaiQuay(int page, int pageSize);
    public Page<HoaDon> findHoaDonByTenPhuongThuc(String tenPhuongThuc, int pageNo, int pageSize);
    public Page<HoaDon> phanTrangTheoTrangThai(int pageNo, int pageSize, int trangThai) ;
    public List<HoaDon> getByTrangThai(int trangThai);
    public Page<HoaDon> getAllWithPagination(int pageNo, int pageSize);
    public Page<HoaDon> getByTrangThaiWithPagination(int trangThai, int pageNo, int pageSize);

    Page<HoaDon> findByTrangThai(Integer trangThai, int page, int size);

}
