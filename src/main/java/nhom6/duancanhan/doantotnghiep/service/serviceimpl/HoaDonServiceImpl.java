package nhom6.duancanhan.doantotnghiep.service.serviceimpl;


import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class HoaDonServiceImpl implements HoaDonService {

   @Autowired
   private HoaDonRepository hoaDonRepository;
    @Override
    public List<HoaDon> getAll() {
        return hoaDonRepository.findAll();
    }
    @Override
    public Page<HoaDon> phanTrang(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return this.hoaDonRepository.findAll(pageable);
    }

    @Override
    public Optional<HoaDon> detail(Integer id) {
        Optional<HoaDon> hoaDon = hoaDonRepository.findById(id);
        return Optional.of(hoaDon.get());
    }

    @Override
    public void addHoaDon(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public void updateHoaDon(Integer id, HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }
}
