package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.PhuongThucThanhToan;
import nhom6.duancanhan.doantotnghiep.repository.PhuongThucThanhToanRepository;
import nhom6.duancanhan.doantotnghiep.service.service.PhuongThucThanhToanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhuongThucThanhToanServiceImpl implements PhuongThucThanhToanService {
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Override
    public List<PhuongThucThanhToan> getAll() {
        return phuongThucThanhToanRepository.findAll();
    }

    @Override
    public Optional<PhuongThucThanhToan> getById(Integer id) {
        Optional<PhuongThucThanhToan> phuongThucThanhToan = phuongThucThanhToanRepository.findById(id);
        return phuongThucThanhToan;
    }

    @Override
    public void create(PhuongThucThanhToan phuongThucThanhToan) {
        phuongThucThanhToanRepository.save(phuongThucThanhToan);
    }

    @Override
    public void update(Integer id, PhuongThucThanhToan phuongThucThanhToan) {
        phuongThucThanhToanRepository.save(phuongThucThanhToan);
    }

    @Override
    public void delete(Integer id) {
        phuongThucThanhToanRepository.deleteById(id);
    }
}
