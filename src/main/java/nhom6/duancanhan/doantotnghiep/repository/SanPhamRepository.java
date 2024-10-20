package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    boolean existsSanPhamByTenSanPham(String tenSanPham);

    boolean existsSanPhamByTenSanPhamAndIdNot(String tenSanPham, Integer id);

//    Page<SanPham> findByTenSanPhamContainingAndTrangThai(String tenSanPham, Integer trangThai, Pageable pageable);
//
//    Page<SanPham> findByTenSanPhamContaining(String tenSanPham, Pageable pageable);
//
//    Page<SanPham> findByTrangThai(Integer trangThai, Pageable pageable);

    // Tìm kiếm theo nhiều tiêu chí: keyword, status, thuongHieu, chatLieu, tayAo, coAo
    @Query("SELECT sp FROM SanPham sp WHERE (:keyword IS NULL OR sp.tenSanPham LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:status IS NULL OR sp.trangThai = :status) " +
            "AND (:thuongHieuId IS NULL OR sp.thuongHieu.id = :thuongHieuId) " +
            "AND (:chatLieuId IS NULL OR sp.chatLieu.id = :chatLieuId) " +
            "AND (:tayAoId IS NULL OR sp.tayAo.id = :tayAoId) " +
            "AND (:coAoId IS NULL OR sp.coAo.id = :coAoId)")
    Page<SanPham> searchProducts(
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("thuongHieuId") Integer thuongHieuId,
            @Param("chatLieuId") Integer chatLieuId,
            @Param("tayAoId") Integer tayAoId,
            @Param("coAoId") Integer coAoId,
            Pageable pageable
    );
    @Query("SELECT s FROM SanPham s WHERE s.coAo.id = :kieuCoAoId")
    List<SanPham> findByIdCoAo(@Param("kieuCoAoId") Integer kieuCoAoId);
    @Query("SELECT s FROM SanPham s WHERE s.tayAo.id = :kieuTayAoId")
    List<SanPham> findByIdTayAo(@Param("kieuTayAoId") Integer kieuTayAoId);
}
