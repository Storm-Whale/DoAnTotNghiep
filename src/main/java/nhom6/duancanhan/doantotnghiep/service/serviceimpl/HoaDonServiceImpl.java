package nhom6.duancanhan.doantotnghiep.service.serviceimpl;


import jakarta.transaction.Transactional;
import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;


//import nhom6.duancanhan.doantotnghiep.dto.ProductDetail;


import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<HoaDon> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize); // Bắt đầu từ trang 0
        return hoaDonRepository.findAll(pageable);
    }


//    @Override
//    public Page<HoaDon> phanTrang(int page, int pageSize) {
//        Pageable pageable = PageRequest.of(page - 1, pageSize);
//        return this.hoaDonRepository.findAll(pageable);
//    }
//    public Page<HoaDon> phanTrang(PageRequest pageRequest) {
//        return hoaDonRepository.findAll(pageRequest);
//    }


    // Phương thức phân trang cho Tab 2 (ví dụ: phân trang theo trạng thái 5)
    public Page<HoaDon> phanTrangTaiQuay(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);  // Tạo Pageable từ page và pageSize
        return hoaDonRepository.findByTrangThai(5, pageable);  // Tìm hóa đơn có trạng thái 5
    }



//    @Override
//    public Page<HoaDonDTO> phanTrang2(int page, int pageSize) {
//        Pageable pageable = PageRequest.of(page - 1, pageSize);
//        return this.hoaDonRepo.findAll(pageable);
//    }


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

    @Override
    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    public Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findByKeywordInAllFields(keyword, pageable);
    }

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
        return hoaDonRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy hoá đơn với id : " + id));
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
    @Override
    public Page<HoaDon> findHoaDonByStatus(String status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if ("all".equals(status)) {
            // Nếu trạng thái là "all", trả về tất cả hóa đơn
            return hoaDonRepository.findAll(pageable);
        } else {
            // Nếu không phải "all", lọc theo trạng thái
            int trangThai = Integer.parseInt(status);
            return hoaDonRepository.findByTrangThai(trangThai, pageable);
        }
    }
    @Override
    public Page<HoaDon> findHoaDonByLoaiHoaDon(String loaiHoaDon, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if ("all".equals(loaiHoaDon)) {
            return hoaDonRepository.findAll(pageable);
        } else {
            return hoaDonRepository.findByLoaiHoaDon(loaiHoaDon, pageable);
        }
    }
    @Override
    public Page<HoaDon> searchHoaDon(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        // Nếu không có từ khóa tìm kiếm, trả về tất cả các hóa đơn
        if (keyword.isEmpty()) {
            return hoaDonRepository.findAll(pageable);
        } else {
            // Tìm kiếm hóa đơn theo tên khách hàng hoặc số điện thoại
            return hoaDonRepository.searchByKeyword(keyword, pageable);
        }
    }
    @Override
    public Page<HoaDon> getHoaDonByTrangThai(Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayTao").descending());
        return hoaDonRepository.findByTrangThai(trangThai, pageable);
    }

    @Override
    public Page<HoaDon> findHoaDonByTenPhuongThuc(String tenPhuongThuc, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if ("all".equals(tenPhuongThuc)) {
            return hoaDonRepository.findAll(pageable);
        } else {
            return hoaDonRepository.findByTenPhuongThuc(tenPhuongThuc, pageable);
        }
    }
    public Page<HoaDon> getHoaDons(int trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.findByTrangThai(trangThai, pageable);
    }

    public Page<HoaDon> getHoaDonsByTrangThai(int trangThai, int page, int size) {
        return hoaDonRepository.findByTrangThai(trangThai, PageRequest.of(page, size));
    }

    public Page<HoaDon> getAllHoaDons(int page, int size) {
        return hoaDonRepository.findAll(PageRequest.of(page, size));
    }

    public Page<HoaDon> getHoaDonsByStatus(int trangThai, int page, int size) {
        if (trangThai == 0) {
            return getAllHoaDons(page, size);  // Trạng thái 0 lấy tất cả hóa đơn
        }
        return getHoaDonsByTrangThai(trangThai, page, size);
    }
    public Page<HoaDon> phanTrangTheoTrangThai(int pageNo, int pageSize, int trangThai) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findByTrangThai(trangThai, pageable);
    }

    public List<HoaDon> getByTrangThai(int trangThai) {
        return hoaDonRepository.findByTrangThai(trangThai); // Gọi phương thức từ repository
    }
    public Page<HoaDon> getAllWithPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findAll(pageable);
    }

    public Page<HoaDon> getByTrangThaiWithPagination(int trangThai, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findByTrangThai(trangThai, pageable);
    }


}
