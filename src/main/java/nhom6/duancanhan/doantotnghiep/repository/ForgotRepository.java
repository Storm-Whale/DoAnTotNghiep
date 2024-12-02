package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotRepository extends JpaRepository<TaiKhoanDTO,Integer> {

    TaiKhoanDTO findByTenDangNhap(String tenDangNhap);

    @Query("SELECT t FROM TaiKhoanDTO t JOIN KhachHang k ON k.taiKhoan.id = t.id WHERE k.email = :email")
    Optional<TaiKhoanDTO> findByEmail(@Param("email") String email);

    Optional<TaiKhoanDTO> findByResetCode(String resetCode);
}
