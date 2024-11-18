package nhom6.duancanhan.doantotnghiep.service.serviceimpl;


import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;

import nhom6.duancanhan.doantotnghiep.repository.KieuTayAoRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class KieuTayAoServiceImpl implements KieuTayAoService {

    @Autowired
    private KieuTayAoRepository kieuTayAoRepository;

    @Override
    public List<KieuTayAo> getAll() {
        return kieuTayAoRepository.findAll();
    }

    @Override
    public Page<KieuTayAo> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.kieuTayAoRepository.findAll(pageable);
    }

    @Override
    public Optional<KieuTayAo> detail(Integer id) {
        return kieuTayAoRepository.findById(id);
    }

    @Override
    public KieuTayAo addKieuTayAo(KieuTayAo kieuTayAo) {
        return kieuTayAoRepository.save(kieuTayAo);
    }

    @Override
    public void updateKieuTayAo(Integer id, KieuTayAo kieuTayAo) {
        kieuTayAoRepository.save(kieuTayAo);
    }

    @Override
    public void updateKieuTayAoById(Integer id, KieuTayAo kieuTayAo) {
        kieuTayAoRepository.save(kieuTayAo);
    }

    @Override
    public void deleteKieuTayAo(Integer id) {
        kieuTayAoRepository.deleteById(id);
    }

    @Override
    public List<String> getAllKieuTayAo() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> kieuTayAoRepository.findAllTenKieuTayAo()
                , "Lỗi khi lấy dữ liệu từ cơ sở dữ liệu"
        );
    }
}
