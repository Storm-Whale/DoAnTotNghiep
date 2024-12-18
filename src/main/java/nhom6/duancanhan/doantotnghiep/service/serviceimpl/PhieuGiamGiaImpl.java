package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
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
        // Nếu trạng thái là 2 (tạm dừng), xử lý riêng
        if (trangThai != null && trangThai == 2) {
            return phieuGiamGiaRepository.findByTrangThaiTemporaryStopped(keyword, ngayBatDau,
                    ngayKetThuc, kieuGiamGia, pageable);
        }
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
            // Chuyển đổi trạng thái ngược lại
            int currentStatus = updatedPhieuGiamGia.getTrangThai();
            updatedPhieuGiamGia.setTrangThai(currentStatus == 1 ? 0 : 1);
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
    @Override
    public PhieuGiamGia findById(Integer id) {
        return phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));
    }
    //auto generate ma
    @Override
    public String generateMaPhieuGiamGia() {
        PhieuGiamGia lastPhieu = phieuGiamGiaRepository.findTopByOrderByIdDesc();
        if (lastPhieu == null) {
            return "PGG001";
        }
        // Tách số từ mã cuối cùng
        String lastMa = lastPhieu.getMaPhieuGiamGia();
        // Nếu mã không bắt đầu bằng PGG hoặc không có số
        if (!lastMa.startsWith("PGG")) {
            return "PGG001";
        }
        // Tìm số cuối cùng trong mã
        String soThuTu = lastMa.replaceAll("[^0-9]", "");
        int soMoi;
        try {
            // Nếu không có số, bắt đầu từ 1
            if (soThuTu.isEmpty()) {
                soMoi = 1;
            } else {
                // Chuyển đổi số và tăng lên
                soMoi = Integer.parseInt(soThuTu) + 1;
            }
        } catch (NumberFormatException e) {
            soMoi = 1;
        }
        return String.format("PGG%03d", soMoi);
    }
}
