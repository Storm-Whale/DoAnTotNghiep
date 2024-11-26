package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaResponse;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.mapper.PhieuGiamGiaMapper;
import nhom6.duancanhan.doantotnghiep.repository.PhieuGiamGiaRepository;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuGiamGiaImpl implements PhieuGiamGiaService {

    private final PhieuGiamGiaMapper phieuGiamGiaMapper;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Override
    public List<PhieuGiamGia> getAll() {
        return phieuGiamGiaRepository.findAll();
    }

    @Override
    public Page<PhieuGiamGia> findByCriteria(String keyword, Date ngayBatDau, Date ngayKetThuc,
                                        Integer kieuGiamGia, Integer trangThai, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo , pageSize);
        return phieuGiamGiaRepository.findByCriteria(keyword, ngayBatDau, ngayKetThuc,
                kieuGiamGia, trangThai, pageable);
    }

    @Override
    public Optional<PhieuGiamGia> getById(Integer id) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaRepository.findById(id);
        return Optional.of(phieuGiamGia.get());
    }

    @Override
    public void create(PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public void update(Integer id, PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaRepository.save(phieuGiamGia);
    }

    @Override
    public void delete(Integer id) {
        phieuGiamGiaRepository.deleteById(id);
    }
    @Override
    public void updateStatus(Integer id, PhieuGiamGia phieuGiamGia) {
        Optional<PhieuGiamGia> existingPhieuGiamGia = phieuGiamGiaRepository.findById(id);
        if (existingPhieuGiamGia.isPresent()) {
            PhieuGiamGia updatedPhieuGiamGia = existingPhieuGiamGia.get();
            updatedPhieuGiamGia.setTrangThai(phieuGiamGia.getTrangThai()); // Cập nhật trạng thái
            phieuGiamGiaRepository.save(updatedPhieuGiamGia); // Lưu lại
        }
    }


    @Override
    public BigDecimal applyDiscount(String maPhieuGiamGia, BigDecimal tongTien) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGia)
                .orElseThrow(() -> new IllegalArgumentException("Mã giảm giá không tồn tại."));
        // Kiểm tra điều kiện áp dụng
        if (tongTien.compareTo(phieuGiamGia.getDieuKien()) >= 0
                && phieuGiamGia.getTrangThai() == 1
                && phieuGiamGia.getSoLuong() > 0) {
            BigDecimal giaTriGiam = phieuGiamGia.getGiaTriGiam();
            if (phieuGiamGia.getKieuGiamGia() == 1) { // Giảm theo phần trăm
                BigDecimal tienGiam = tongTien.multiply(giaTriGiam).divide(new BigDecimal(100));
                return tongTien.subtract(tienGiam.min(phieuGiamGia.getGiaTriMax()));
            } else { // Giảm theo số tiền cố định
                return tongTien.subtract(giaTriGiam.min(phieuGiamGia.getGiaTriMax()));
            }
        } else {
            throw new IllegalArgumentException("Hóa đơn không đủ điều kiện áp dụng mã giảm giá.");
        }
    }

    @Override
    public PhieuGiamGia getByMaPhieuGiamGia(String maPhieuGiamGia) {
        return phieuGiamGiaRepository.findByMaPhieuGiamGia(maPhieuGiamGia)
                .orElseThrow(() -> new DataNotFoundException("Mã giảm giá không tồn tại."));
    }

    @Override
    public List<PhieuGiamGiaResponse> getPGGByTrangThai(Integer trangThai) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> phieuGiamGiaRepository.findPhieuGiamGiaByTrangThai(trangThai).stream()
                        .map(phieuGiamGiaMapper::toPhieuGiamGiaResponse)
                        .toList()
                ,"Lỗi khi lấy thông tin phiếu giảm giá từ cơ sở dữ liệu"
        );
    }
}
