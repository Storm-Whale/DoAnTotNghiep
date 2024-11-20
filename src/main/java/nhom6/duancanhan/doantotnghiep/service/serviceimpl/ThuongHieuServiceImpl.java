package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.repository.ThuongHieuRepository;
import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
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
        return thuongHieuRepossitory.findById(id);
    }

    @Override
    public ThuongHieu addThuongHieu(ThuongHieu thuongHieu) {
        return thuongHieuRepossitory.save(thuongHieu);
    }

    @Override
    public void updateThuongHieu(Integer id, ThuongHieu thuongHieu) {
        thuongHieuRepossitory.save(thuongHieu);
    }

    @Override
    public void deleteThuongHieu(Integer id) {
        thuongHieuRepossitory.deleteById(id);
    }

    @Override
    public List<String> getAllTenThuongHieu() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> thuongHieuRepossitory.findAllTenThuongHieu()
                , "Lỗi khi lấy dữ liệu từ cơ sở dữ liệu"
        );
    }
}
