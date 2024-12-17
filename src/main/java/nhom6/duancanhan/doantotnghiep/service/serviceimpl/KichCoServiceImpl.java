package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.repository.KichCoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KichCoServiceImpl implements KichCoService {
    @Autowired
    private final KichCoRepository kichCoRepository;

    public KichCoServiceImpl(KichCoRepository kichCoRepository) {
        this.kichCoRepository = kichCoRepository;
    }

    @Override
    public List<KichCo> getAll() {
        return kichCoRepository.findAll();
    }

    @Override
    public Page<KichCo> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.kichCoRepository.findAll(pageable);
    }

    @Override
    public Optional<KichCo> detail(Integer id) {
        return kichCoRepository.findById(id);
    }

    @Override
    public KichCo addKichCo(KichCo kichCo) {
        return kichCoRepository.save(kichCo);
    }

    @Override
    public void updateKichCo(Integer id, KichCo kichCo) {
        kichCoRepository.save(kichCo);
    }

    @Override
    public void updateKichCoById(Integer id, KichCo kichCo) {
        kichCoRepository.save(kichCo);
    }

    @Override
    public Page<KichCo> timKiemVaPhanTrang(String tenKichCo, Integer trangThai, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return kichCoRepository.findByCriteria(tenKichCo, trangThai, pageable);
    }

    @Override
    public Page<KichCo> phanTrangTheoTrangThai(Integer trangThai, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return kichCoRepository.findByTrangThai(trangThai, pageable);

    }

    @Override
    public Page<KichCo> phanTrangTheoTen(String tenKichCo, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return kichCoRepository.findKichCoByTen(tenKichCo, pageable);
    }

    @Override
    public boolean existsByTenKichCo(String tenKichCo) {
        return kichCoRepository.existsByTenKichCo(tenKichCo);
    }

    @Override
    public List<KichCo> getKichCoByTrangThai(int trangThai) {
        return DatabaseOperationHandler.handleDatabaseOperation(() ->
                        kichCoRepository.findAllBytrangThai(trangThai)
                , "Lỗi khi lấy dữ liêu từ cơ sở dữ liệu");
    }
}
