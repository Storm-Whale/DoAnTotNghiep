package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface NhanVienService {
    List<NhanVien> getAll();

    Page<NhanVien> phanTrang(int pageNo, int pageSize);

    NhanVien detailNhanVien(Integer id);

    void addNhanVien(NhanVien nhanVien);

    void updateNhanVien(NhanVien nhanVien);

    void deleteNhanVien(Integer id);

    Page<NhanVien> SearchandPhantrang (String keyword, Integer trangThai, int pageNo, int pageSize);

    NhanVien getNhanVienByIdTaiKhoan(Integer idTK);

    NhanVien findById(Integer id);
}
