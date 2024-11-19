package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.repository.KieuCoAoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
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

//    @Override
//    public Page<KieuCoAo> getAll(int page, int size) {
//        return null;
//    }

    @Override
    public Page<KieuCoAo> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.kieuCoAoRepository.findAll(pageable);
    }

    @Override
    public Optional<KieuCoAo> detail(Integer id) {
        return  kieuCoAoRepository.findById(id);
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
}
