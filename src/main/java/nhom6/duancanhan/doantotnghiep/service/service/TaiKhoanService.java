package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanService {

    List<TaiKhoan> getAll();

    Page<TaiKhoan> phanTrang(int pageNo, int pageSize);

    Optional<TaiKhoan> detail(Integer id);

    void addTaiKhoan(TaiKhoan khachHang);

    void updateTaiKhoan(Integer id, TaiKhoan taiKhoan);

    void deleteTaiKhoan(Integer id);

    TaiKhoan findByTenDangNhap(String tenDangNhap);

    TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan);


    boolean checkAccount(String username, String password);

    TaiKhoan findByTTKAndMK (String username, String password);
//    Optional<TaiKhoan> findByResetCode(String resetCode);
    TaiKhoanDTO saveTaiKhoan(TaiKhoanDTO taiKhoan);

    TaiKhoan findById (Integer id);
}
