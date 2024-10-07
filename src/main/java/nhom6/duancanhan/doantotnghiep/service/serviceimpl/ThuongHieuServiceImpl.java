package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.repository.DiaChiRepossitory;
import nhom6.duancanhan.doantotnghiep.repository.ThuongHieuRepository;
import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {
    @Autowired
    private ThuongHieuRepository thuongHieuRepossitory;

    @Override
    public List<ThuongHieu> getAll() {
        return thuongHieuRepossitory.findAll();
    }

    @Override
    public Page<ThuongHieu> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.thuongHieuRepossitory.findAll(pageable);
    }

    @Override
    public Optional<ThuongHieu> detail(Integer id) {
        Optional<ThuongHieu> thuongHieu = thuongHieuRepossitory.findById(id);
        return Optional.of(thuongHieu.get());
    }

    @Override
    public void addThuongHieu(ThuongHieu thuongHieu) {
        thuongHieuRepossitory.save(thuongHieu);
    }

    @Override
    public void updateThuongHieu(Integer id, ThuongHieu thuongHieu) {
        thuongHieuRepossitory.save(thuongHieu);
    }

    @Override
    public void deleteThuongHieu(Integer id) {
        thuongHieuRepossitory.deleteById(id);
    }
}
