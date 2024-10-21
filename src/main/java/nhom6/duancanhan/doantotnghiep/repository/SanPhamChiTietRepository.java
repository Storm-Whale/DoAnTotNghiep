package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT spct FROM SanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "JOIN spct.kichCo kc " +
            "JOIN spct.mauSac ms " +
            "WHERE (:tenSanPham IS NULL OR sp.tenSanPham LIKE %:tenSanPham%) " +
            "AND (:kichCo IS NULL OR kc.tenKichCo = :kichCo) " +
            "AND (:mauSac IS NULL OR ms.tenMauSac = :mauSac) " +
            "AND (:trangThai IS NULL OR spct.trangThai = :trangThai)")
    Page<SanPhamChiTiet> findByCriteria(
            @Param("tenSanPham") String tenSanPham,
            @Param("kichCo") Integer kichCo,
            @Param("mauSac") Integer mauSac,
            @Param("trangThai") Integer trangThai,
            Pageable pageable
    );
}
