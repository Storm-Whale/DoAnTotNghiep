package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.repository.MauSacRepository;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    private MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }

    @Override
    public Page<MauSac> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.mauSacRepository.findAll(pageable);
    }

    @Override
    public Optional<MauSac> detail(Integer id) {
        return mauSacRepository.findById(id);
    }

    @Override
    public MauSac addMauSac(MauSac mauSac) {
        return mauSacRepository.save(mauSac);
    }

    @Override
    public void updateMauSac(Integer id, MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    @Override
    public void updateMauSacById(Integer id, MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    @Override
    public boolean existsByTenMauSac(String tenMauSac) {
        return mauSacRepository.existsByTenMauSac(tenMauSac);
    }

    @Override
    public List<MauSac> getMauSacByTrangThai(int trangThai) {
        return DatabaseOperationHandler.handleDatabaseOperation(() ->
                        mauSacRepository.findAllByTrangThai(trangThai)
                , "Lỗi khi lấy dữ liêu từ cơ sở dữ liệu");
    }
}
