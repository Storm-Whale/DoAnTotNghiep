package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;


    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public Page<KhachHang> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.khachHangRepository.findAll(pageable);
    }

    @Override
    public Optional<KhachHang> detail(Integer id) {
        Optional<KhachHang> khachHang = khachHangRepository.findById(id);
        return Optional.of(khachHang.get());

    }

    @Override
    public void addKhachHang(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }

    @Override
    public void updateKhachHang(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }

    @Override
    public void deleteKhachHang(Integer id) {
        khachHangRepository.deleteById(id);
    }

    @Override
    public Page<KhachHang> SearchandPhantrang(String keyword, Integer trangThai, int pageNo, int pageSize) {
        String keywordSearch = (keyword != null && !keyword.isEmpty()) ? "%" + keyword + "%" : null;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return khachHangRepository.searchKhachHang(keyword, trangThai, pageable);
    }

    @Override
    public KhachHang findByIdTaiKhoan(int idTaiKhoan) {
        return khachHangRepository.findByIdTaiKhoan(idTaiKhoan);
    }

    @Override
    public KhachHang saveKhachHang(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }


    @Override
    public KhachHang findBySoDienThoaiKhachHang(String soDienThoai) {
       List<KhachHang> khachHangs = khachHangRepository.findBySoDienThoai(soDienThoai);
        return khachHangs.isEmpty() ? null : khachHangs.get(0);
    }


}
