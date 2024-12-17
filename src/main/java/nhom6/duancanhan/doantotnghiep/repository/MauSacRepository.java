package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {

    @Query(value = """
        select ms.id from MauSac ms where ms.tenMauSac = :tenMauSac
    """)
    Integer findByTenMauSac(@Param("tenMauSac") String tenMauSac);

    boolean existsByTenMauSac(String tenMauSac);

    List<MauSac> findAllByTrangThai(Integer trangThai);
}
