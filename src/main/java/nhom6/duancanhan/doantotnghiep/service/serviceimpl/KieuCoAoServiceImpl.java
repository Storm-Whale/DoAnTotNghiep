package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.repository.KieuCoAoRepository;
import nhom6.duancanhan.doantotnghiep.repository.KieuTayAoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class KieuCoAoServiceImpl implements KieuCoAoService {
    @Autowired
    private KieuCoAoRepository kieuCoAoRepository;

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
        Optional<KieuCoAo> kieuCoAo = kieuCoAoRepository.findById(id);
        return Optional.of(kieuCoAo.get());
    }

    @Override
    public void addKieuCoAo(KieuCoAo kieuCoAo) {
        kieuCoAoRepository.save(kieuCoAo);
    }

    @Override
    public void updateKieuCoAo(Integer id, KieuCoAo kieuCoAo) {
        kieuCoAoRepository.save(kieuCoAo);
    }

    @Override
    public void deleteKieuCoAo(Integer id) {
        kieuCoAoRepository.deleteById(id);
    }
}
