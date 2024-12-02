package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.NhanVienRepository;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NhanVien> getAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public Page<NhanVien> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.nhanVienRepository.findAll(pageable);
    }

    @Override
    public NhanVien detailNhanVien(Integer id) {
        return nhanVienRepository.findById(id).orElseThrow(
                (() -> new DataNotFoundException("Không tìm thấy khách hàng với id : " + id))
        );
    }

    @Override
    public void addNhanVien(NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
    }

    @Override
    public void updateNhanVien(NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
    }

    @Override
    public void deleteNhanVien(Integer id) {
        nhanVienRepository.deleteById(id);
    }

    @Override
    public Page<NhanVien> SearchandPhantrang(String keyword, Integer trangThai, int pageNo, int pageSize) {
      Pageable pageable = PageRequest.of(pageNo,pageSize);
      return nhanVienRepository.findByKeywordAndTrangThai(keyword,trangThai,pageable);
    }

    @Override
    public NhanVien getNhanVienByIdTaiKhoan(Integer idTK) {
        return nhanVienRepository.findByTaiKhoanId(idTK);
    }

    @Override
    public NhanVien findById(Integer id) {
        return nhanVienRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khách hàng với ID: " + id));
    }

    @Override
    public boolean isSdtExist(String sdt) {
        return nhanVienRepository.existsBySdt(sdt);
    }

}
