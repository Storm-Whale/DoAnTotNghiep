package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface KhachHangService {

     List<KhachHang> getAll();

     Page<KhachHang> phanTrang(int pageNo, int pageSize);

     Optional<KhachHang> detail(Integer id);

     void addKhachHang(KhachHang khachHang);

     void updateKhachHang(KhachHang khachHang);

     void deleteKhachHang(Integer id);

     Page<KhachHang> SearchandPhantrang (String keyword, Integer trangThai, int pageNo, int pageSize);

}
