package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.repository.DiaChiRepossitory;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaChiServiceImpl implements DiaChiService {

    @Autowired
    private DiaChiRepossitory diaChiRepossitory;
    @Override
    public List<DiaChi> getAll() {
        return diaChiRepossitory.findAll();
    }

    @Override
    public Page<DiaChi> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.diaChiRepossitory.findAll(pageable);
    }

    @Override
    public Optional<DiaChi> detail(Integer id) {
        Optional<DiaChi> diaChi = diaChiRepossitory.findById(id);
        return Optional.of(diaChi.get());
    }

    @Override
    public void addDiaChi(DiaChi diaChi) {
        diaChiRepossitory.save(diaChi);
    }

    @Override
    public void updateDiaChi(Integer id, DiaChi diaChi) {
        diaChiRepossitory.save(diaChi);
    }

    @Override
    public void deleteDiaChi(Integer id) {
      diaChiRepossitory.deleteById(id);
    }
}
