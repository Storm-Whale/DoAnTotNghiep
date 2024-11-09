package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KhachHangService {

     List<KhachHang> getAll();

     Page<KhachHang> phanTrang(int pageNo, int pageSize);

     KhachHang detail(Integer id);

     void addKhachHang(KhachHang khachHang);

     void updateKhachHang(KhachHang khachHang);

     void deleteKhachHang(Integer id);

     Page<KhachHang> SearchandPhantrang (String keyword, Integer trangThai, int pageNo, int pageSize);

     KhachHang findByIdTaiKhoan(int idTaiKhoan) ;

     KhachHang saveKhachHang(KhachHang khachHang);

     KhachHang findBySoDienThoaiKhachHang (String soDienThoai);
}
