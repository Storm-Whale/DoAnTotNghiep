package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.dto.KhachHangDTO;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ForgotKHRepository extends JpaRepository<KhachHangDTO,Integer> {
    @Query(
            "SELECT nv FROM KhachHang nv WHERE (:keyword IS NULL OR " +
                    "lower(nv.ten) LIKE lower(concat('%', :keyword, '%')) OR " + "nv.soDienThoai LIKE concat('%', :keyword, '%') OR " +
                    "nv.email LIKE concat('%', :keyword, '%')) " + "AND (:trangThai IS NULL OR nv.trangThai = :trangThai)"
    )
    Page<KhachHang> searchKhachHang(@Param(("keyword")) String keyword, @Param(("trangThai")) Integer trangThai, Pageable pageable);

    KhachHang findByTaiKhoanId(int idTaiKhoan);

    List<KhachHang> findBySoDienThoai(String soDienThoai);

    Optional<KhachHangDTO> findByEmail(String email);
}
