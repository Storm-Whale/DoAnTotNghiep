package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.repository.MauSacRepository;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
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
        Optional<MauSac> mauSac = mauSacRepository.findById(id);
        return Optional.of(mauSac.get());
    }

    @Override
    public void addMauSac(MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    @Override
    public void updateMauSac(Integer id, MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    @Override
    public void deleteMauSac(Integer id) {
        mauSacRepository.deleteById(id);
    }
}
