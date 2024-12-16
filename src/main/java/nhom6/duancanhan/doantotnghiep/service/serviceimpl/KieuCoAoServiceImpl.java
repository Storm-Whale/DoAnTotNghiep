package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.repository.KieuCoAoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KieuCoAoServiceImpl implements KieuCoAoService {

    private final KieuCoAoRepository kieuCoAoRepository;

    public KieuCoAoServiceImpl(KieuCoAoRepository kieuCoAoRepository) {
        this.kieuCoAoRepository = kieuCoAoRepository;
    }

    @Override
    public List<KieuCoAo> getAll() {
        return kieuCoAoRepository.findAll();
    }

    @Override
    public Page<KieuCoAo> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.kieuCoAoRepository.findAll(pageable);
    }

    @Override
    public Optional<KieuCoAo> detail(Integer id) {
        return kieuCoAoRepository.findById(id);
    }

    @Override
    public KieuCoAo addKieuCoAo(KieuCoAo kieuCoAo) {
        return kieuCoAoRepository.save(kieuCoAo);
    }

    @Override
    public void updateKieuCoAo(Integer id, KieuCoAo kieuCoAo) {
        kieuCoAoRepository.save(kieuCoAo);
    }

    @Override
    public void updateKieuCoAoById(Integer id, KieuCoAo kieuCoAo) {
        kieuCoAoRepository.save(kieuCoAo);
    }

    @Override
    public List<String> getAllTenKieuCoAo() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                kieuCoAoRepository::findAllTenKieuCoAo
                , "Lỗi khi lấy dữ liêu từ cơ sở dữ liệu"
        );
    }

    @Override
    public boolean existsByTenCoAo(String tenCoAo) {
        return kieuCoAoRepository.existsByTenCoAo(tenCoAo);
    }

    @Override
    public List<KieuCoAo> getKieuCoAoByTrangThai(int trangThai) {
        return null;
    }

    @Override
    public Page<KieuCoAo> phanTrangTheoTrangThai(int pageNo, int pageSize, int trangThai) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return kieuCoAoRepository.findByTrangThai(trangThai, pageable);
    }

    @Override
    public List<KieuCoAo> getKieuCoAoByTen(String tenCoAo) {
        return null;
    }

    @Override
    public Page<KieuCoAo> phanTrangTheoTen(String tenCoAo, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return kieuCoAoRepository.findKieuCoAoByTen(tenCoAo, pageable);
    }
    @Override
    public Page<KieuCoAo> timKiemVaPhanTrang(String tenCoAo, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kieuCoAoRepository.findByCriteria(tenCoAo, trangThai, pageable);
    }
    @Override
        public List<KieuCoAo> searchByTenCoAo(String tenCoAo) {
        return kieuCoAoRepository.findByTenCoAoContainingIgnoreCase(tenCoAo);
    }
}
