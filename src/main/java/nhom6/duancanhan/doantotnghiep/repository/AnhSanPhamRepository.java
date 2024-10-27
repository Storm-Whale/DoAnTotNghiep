package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.AnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {

    List<AnhSanPham> getBySanPhamChiTietId(Integer sanPhamChiTietId);

    @Modifying
    @Query("DELETE FROM AnhSanPham asp WHERE asp.sanPhamChiTiet.id = :sanPhamChiTietId")
    void deleteBySanPhamChiTietId(@Param("sanPhamChiTietId") Integer sanPhamChiTietId);
}
