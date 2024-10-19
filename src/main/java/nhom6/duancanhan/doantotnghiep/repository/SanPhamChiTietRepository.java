package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query("""
            select spct from SanPhamChiTiet as spct where spct.sanPham.id = ?1
            """)
    List<SanPhamChiTiet> findSanPhamChiTietByIdSanPham(Integer idSP);

    @Query("""
       select spct from SanPhamChiTiet as spct where spct.sanPham.id = :idSP order by spct.gia asc limit 1
       """)
    SanPhamChiTiet findFirstBySanPhamId(@Param("idSP") Integer idSP);

    SanPhamChiTiet findSanPhamChiTietBySanPhamIdAndKichCoIdAndMauSacId(Integer idSanPham, Integer idKichCo, Integer idMauSac);
}
