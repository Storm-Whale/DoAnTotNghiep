package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan,Integer> {
    TaiKhoan findByTenDangNhap(String tenDangNhap);

//    Optional<TaiKhoan> findByResetCode(String resetCode);

    boolean existsByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);

    TaiKhoan findTaiKhoanByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);
}
