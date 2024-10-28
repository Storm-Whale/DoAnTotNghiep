package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<TaiKhoan,Long> {
    TaiKhoan findByTenDangNhap(String tenDangNhap);
}
