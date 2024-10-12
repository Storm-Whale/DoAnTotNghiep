package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.repository.NhanVienRepository;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public Optional<NhanVien> detail(Integer id) {
        Optional<NhanVien> nhanVien = nhanVienRepository.findById(id);
        return Optional.of(nhanVien.get());
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
    public List<NhanVien> findSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be blank");
        }
        return  nhanVienRepository.searchNhanVien(keyword);
    }


}
