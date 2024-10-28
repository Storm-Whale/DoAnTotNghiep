package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.repository.LoginRepository;
import nhom6.duancanhan.doantotnghiep.service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final LoginRepository taiKhoanRepository;

    @Autowired
    public LoginServiceImpl(LoginRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public TaiKhoan findByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepository.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public void addTaiKhoan(TaiKhoan user) {
        taiKhoanRepository.save(user); // Lưu người dùng vào cơ sở dữ liệu
    }

    @Override
    public List<TaiKhoan> getAll() {
        return taiKhoanRepository.findAll(); // Lấy tất cả người dùng
    }
}
