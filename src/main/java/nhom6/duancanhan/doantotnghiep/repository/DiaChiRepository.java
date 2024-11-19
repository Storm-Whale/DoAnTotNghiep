package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Integer> {

    List<DiaChi> findByKhachHangId(Integer khachHangId);

    Optional<DiaChi> findByDiaChiChiTiet(String diaChi);
}
