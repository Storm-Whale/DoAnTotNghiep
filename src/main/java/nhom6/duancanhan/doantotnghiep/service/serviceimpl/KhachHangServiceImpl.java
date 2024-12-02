package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public KhachHang detail(Integer id) {
        return khachHangRepository.findById(id).orElseThrow(
                (() -> new DataNotFoundException("Không tìm thấy khách hàng với id : " + id))
        );
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
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return khachHangRepository.findByKeywordAndTrangThai(keyword, trangThai, pageable);
    }

    @Override
    public KhachHang findByIdTaiKhoan(int idTaiKhoan) {
        return khachHangRepository.findByTaiKhoanId(idTaiKhoan);
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

    @Override
    public KhachHang findById(Integer id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách hàng với ID: " + id));
    }

    @Override
    public boolean isSoDienThoaiExist(String soDienThoai) {
            return khachHangRepository.existsBySoDienThoai(soDienThoai);

    }
}
