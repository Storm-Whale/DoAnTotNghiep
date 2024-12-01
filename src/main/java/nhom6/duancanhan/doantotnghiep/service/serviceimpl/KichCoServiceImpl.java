package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.repository.KichCoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
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
}
