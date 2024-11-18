package nhom6.duancanhan.doantotnghiep.repository;

import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KieuTayAoRepository extends JpaRepository<KieuTayAo, Integer> {

    @Query(value = """
            select kta.tenTayAo from KieuTayAo kta
        """)
    List<String> findAllTenKieuTayAo();
}
