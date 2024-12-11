package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.dto.TaiKhoanDTO;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.repository.ForgotRepository;
import nhom6.duancanhan.doantotnghiep.repository.TaiKhoanRepository;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private ForgotRepository forgotRepository;

    @Override
    public List<TaiKhoan> getAll() {
        return taiKhoanRepository.findAll();
    }

    @Override
    public Page<TaiKhoan> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.taiKhoanRepository.findAll(pageable);
    }

    @Override
    public Optional<TaiKhoan> detail(Integer id) {
        Optional<TaiKhoan> taiKhoan = taiKhoanRepository.findById(id);
        return Optional.of(taiKhoan.get());
    }

    @Override
    public void addTaiKhoan(TaiKhoan taiKhoan) {
           taiKhoanRepository.save(taiKhoan);
    }

    @Override
    public void updateTaiKhoan(Integer id, TaiKhoan taiKhoan) {
        taiKhoanRepository.save(taiKhoan);
    }

    @Override
    public void deleteTaiKhoan(Integer id) {
         taiKhoanRepository.deleteById(id);
    }

    @Override
    public TaiKhoan findByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepository.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }


//    @Override
//    public TaiKhoan findByResetCode(String resetCode) {
//        return taiKhoanRepository.findByResetCode(resetCode);
//    }

    @Override
    public boolean checkAccount(String username, String password) {
        return taiKhoanRepository.existsByTenDangNhapAndMatKhau(username, password);
    }

    @Override
    public TaiKhoan findByTTKAndMK(String username, String password) {
        return taiKhoanRepository.findTaiKhoanByTenDangNhapAndMatKhau(username, password);
    }

    @Override
    public TaiKhoanDTO saveTaiKhoan(TaiKhoanDTO taiKhoan) {
        return forgotRepository.save(taiKhoan);
    }

    @Override
    public TaiKhoan findById(Integer id) {
        return taiKhoanRepository.findById(id).orElse(null);
    }
}
