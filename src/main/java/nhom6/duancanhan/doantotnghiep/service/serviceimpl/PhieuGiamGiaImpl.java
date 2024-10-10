package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.repository.PhieuGiamGiaRepository;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuGiamGiaImpl implements PhieuGiamGiaService {
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Override
    public List<PhieuGiamGia> getAll() {
        return phieuGiamGiaRepository.findAll();
    }

    @Override
    public Page<PhieuGiamGia> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.phieuGiamGiaRepository.findAll(pageable);
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
}
