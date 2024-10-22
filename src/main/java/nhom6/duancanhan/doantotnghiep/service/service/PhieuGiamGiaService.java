package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaService {
    List<PhieuGiamGia> getAll();
    Page<PhieuGiamGia> findByCriteria(String keyword, Date ngayBatDau,
                                 Date ngayKetThuc, Integer kieuGiamGia, Integer trangThai, int pageNo, int pageSize);
    Optional<PhieuGiamGia> getById(Integer id);
    void create(PhieuGiamGia phieuGiamGia);
    void update(Integer id, PhieuGiamGia phieuGiamGia);
    void delete(Integer id);
}
