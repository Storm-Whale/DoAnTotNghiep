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

    List<SanPham> findAllByTrangThai(Integer trangThai);

    boolean existsSanPhamByTenSanPham(String tenSanPham);

    boolean existsSanPhamByTenSanPhamAndIdNot(String tenSanPham, Integer id);

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

    @Query("""
                select sp.id from SanPham sp where sp.tenSanPham = :tenSP
            """)
    Integer findByTenSanPham(@Param("tenSP") String tenSanPham);

    @Query("""
            select sp from SanPham sp where
                (:tenThuongHieu is null or :tenThuongHieu = '' or sp.thuongHieu.tenThuongHieu like :tenThuongHieu) and
                (:tenChatLieu is null or :tenChatLieu = '' or sp.chatLieu.tenChatLieu like :tenChatLieu) and
                (:tenKieuCoAo is null or :tenKieuCoAo = '' or sp.coAo.tenCoAo like :tenKieuCoAo) and
                (:tenKieuTayAo is null or :tenKieuTayAo = '' or sp.tayAo.tenTayAo like :tenKieuTayAo)
            """)
    List<SanPham> searchSanPham(
            @Param("tenThuongHieu") String tenThuongHieu,
            @Param("tenChatLieu") String tenChatLieu,
            @Param("tenKieuCoAo") String tenKieuCoAo,
            @Param("tenKieuTayAo") String tenKieuTayAo
    );

    @Query("SELECT s FROM SanPham s WHERE s.thuongHieu.id = :thuongHieuId")
    List<SanPham> findByIdThuongHieu(@Param("thuongHieuId") Integer thuongHieuId);


    @Query("SELECT s FROM SanPham s WHERE s.chatLieu.id = :chatLieuId")
    List<SanPham> findByIdChatLieu(@Param("chatLieuId") Integer chatLieuId);

}
