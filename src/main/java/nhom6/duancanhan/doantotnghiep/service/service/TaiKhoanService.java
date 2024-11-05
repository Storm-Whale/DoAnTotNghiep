package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaiKhoanService {

    List<TaiKhoan> getAll();

    Page<TaiKhoan> phanTrang(int pageNo, int pageSize);

    Optional<TaiKhoan> detail(Integer id);

    void addTaiKhoan(TaiKhoan khachHang);

    void updateTaiKhoan(Integer id, TaiKhoan taiKhoan);

    void deleteTaiKhoan(Integer id);

    TaiKhoan findByTenDangNhap(String tenDangNhap);

    TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan);
    public TaiKhoan findByResetCode(String resetCode) ;

    boolean checkAccount(String username, String password);

    TaiKhoan findByTTKAndMK (String username, String password);
}
