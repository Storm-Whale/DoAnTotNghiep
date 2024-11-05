package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import org.springframework.data.domain.Page;


import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;

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
    ////
    ///
//    PageDTO<List<HoaDonDTO>> phanTrang(PageRequestDTO pageRequestDTO);
//    HoaDon themHoaDon(HoaDonDTO hoaDonDTO);
//    HoaDonDTO converToDto(HoaDon hoaDon);
//    HoaDon converToEntity(HoaDonDTO hoaDonDTO);
    void cancelHoaDon(Integer id);
}
