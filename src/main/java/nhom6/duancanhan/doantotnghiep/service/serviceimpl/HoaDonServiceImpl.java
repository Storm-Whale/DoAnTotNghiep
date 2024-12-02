package nhom6.duancanhan.doantotnghiep.service.serviceimpl;


import jakarta.transaction.Transactional;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
//import nhom6.duancanhan.doantotnghiep.dto.ProductDetail;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public HoaDonServiceImpl(HoaDonRepository hoaDonRepository, HoaDonChiTietRepository hoaDonChiTietRepository, SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

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
        return Optional.empty();
    }


    @Override
    public void addHoaDon(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public void updateHoaDon(Integer id, HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

//    @Override
//    public Optional<HoaDon> detail(Integer id) {
//        Optional<HoaDon> hoaDon = hoaDonRepository.findById(id);
//        return Optional.of(hoaDon.get());
//    }


    @Override
    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }

//    @Override
//    public Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        return hoaDonRepository.findByTenNguoiNhanContaining(keyword, pageable);
//    }
@Override
public Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    return hoaDonRepository.findByKeywordInAllFields(keyword, pageable);
}



    //    @Override
//    public void cancelHoaDon(Integer id) {
//        HoaDon hoaDon = hoaDonRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với id: "+id));
//        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findHoaDonChiTietById(id);
//        hoaDonChiTietList.forEach(hoaDonChiTiet -> {
//            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//        });
//        hoaDonRepository.delete(hoaDon);
//    }
    @Transactional
    @Override
    public void cancelHoaDon(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với id: " + id));

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findHoaDonChiTietById(id);
        // Lặp qua từng chi tiết hóa đơn và cập nhật số lượng sản phẩm tồn
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            if (sanPhamChiTiet != null) {
                int newQuantity = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                sanPhamChiTiet.setSoLuong(newQuantity);
                System.out.println("Cập nhật sản phẩm: " + sanPhamChiTiet.getId() + " với số lượng mới: " + newQuantity);
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            } else {
                System.out.println("Sản phẩm không tồn tại.");
            }
        }
        // Xóa tất cả chi tiết hóa đơn
        hoaDonChiTietRepository.deleteAll(hoaDonChiTietList);
        hoaDonRepository.delete(hoaDon);
    }

    @Override
    public HoaDon findById(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }
    @Override
    public List<PhieuGiamGiaHoaDonDTO> getHoaDonByPhieuGiamGia(Integer phieuGiamGiaId) {
        return hoaDonRepository.findHoaDonByPhieuGiamGia(phieuGiamGiaId);
    }

    @Override
    public List<HoaDon> findByKhachHangId(Integer khachHangId) {
        return hoaDonRepository.findByKhachHangId(khachHangId);
    }

    @Override
    public List<HoaDon> findByNhanVienId(Integer nhanVienId) {
        return hoaDonRepository.findByNhanVienId(nhanVienId);
    }
}
