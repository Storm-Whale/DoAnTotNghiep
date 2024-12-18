package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Override
    public List<HoaDonChiTiet> getAll() {
        return hoaDonChiTietRepository.findAll();
    }

    @Override
    public Page<HoaDonChiTiet> phanTrang(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return this.hoaDonChiTietRepository.findAll(pageable);
    }

    @Override
    public Optional<HoaDonChiTiet> detail(Integer id) {
        Optional<HoaDonChiTiet> hoaDonct = hoaDonChiTietRepository.findById(id);
        return Optional.of(hoaDonct.get());
    }

    @Override
    public void addHoaDon(HoaDonChiTiet hoaDonct) {
        hoaDonChiTietRepository.save(hoaDonct);
    }

    @Override
    public void updateHoaDon(Integer id, HoaDonChiTiet hoaDonct) {
        hoaDonChiTietRepository.save(hoaDonct);
    }

    @Override
    public void deleteHoaDon(Integer id) {
        hoaDonChiTietRepository.deleteById(id);
    }

    @Override
    public List<HoaDonChiTiet> getHoaDonChiTietByHoaDonId(Integer hoaDonId) {
        return hoaDonChiTietRepository.findByHoaDonId(hoaDonId);
    }

    @Override
    public List<HoaDonChiTiet> findByHoaDonId(Integer hoaDonId) {
        return hoaDonChiTietRepository.findByHoaDonId(hoaDonId);
    }

    @Override
    public List<HoaDonChiTiet> findByKhachHangId(Integer khachHangId) {
        return hoaDonChiTietRepository.findByKhachHangId(khachHangId);
    }

    @Override
    public HoaDonChiTiet findById(Integer id) {
        return hoaDonChiTietRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy hoá đơn với id : " + id));
    }
@Override
public List<HoaDonChiTiet> findAllByHoaDonId(Integer hoaDonId) {
    return hoaDonChiTietRepository.findAllByHoaDonId(hoaDonId);
}


}
