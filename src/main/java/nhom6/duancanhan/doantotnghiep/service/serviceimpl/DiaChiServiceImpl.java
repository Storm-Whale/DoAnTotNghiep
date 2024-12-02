package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.DiaChiRepository;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaChiServiceImpl implements DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepossitory;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    public List<DiaChi> getAll() {
        return diaChiRepossitory.findAll();
    }

    @Override
    public Page<DiaChi> phanTrang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.diaChiRepossitory.findAll(pageable);
    }

    @Override
    public Optional<DiaChi> detail(Integer id) {
        Optional<DiaChi> diaChi = diaChiRepossitory.findById(id);
        return Optional.of(diaChi.get());
    }

    @Override
    public void addDiaChi(DiaChi diaChi) {
        diaChiRepossitory.save(diaChi);
    }

    @Override
    public void updateDiaChi(Integer id, DiaChi diaChi) {
        diaChiRepossitory.save(diaChi);
    }

    @Override
    public void deleteDiaChi(Integer id) {
        diaChiRepossitory.deleteById(id);
    }

    @Override
    public DiaChi getDiaChiById(Integer id) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> diaChiRepossitory.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy địa chỉ với id : " + id))
                , "Lấy dữ liệu địa chỉ từ cơ sở dữ liệu bị lỗi"
        );
    }

    @Override
    public List<DiaChi> getDiaChiByIdKhachHang(Integer idKhachHang, Integer trangThai) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> diaChiRepossitory.findByKhachHangId(idKhachHang, trangThai)
                , "Lấy dữ liệu địa chỉ từ cơ sở dữ liệu bị lỗi"
        );
    }

    @Override
    public DiaChi newDiaChi(String diaChi, String tenNguoiNhan, String soDienThoai, Integer idKhachHang) {
        KhachHang khachHang = idKhachHang == null ? null : khachHangRepository.findById(idKhachHang).orElseThrow(
                () -> new DataNotFoundException("Không tìm thấy khách hàng với đi : "  + idKhachHang)
        );
        return diaChiRepossitory.save(DiaChi.builder()
                .diaChiChiTiet(diaChi)
                .soDienThoai(soDienThoai)
                .tenKhachHang(tenNguoiNhan)
                .khachHang(khachHang)
                .build());
    }
}
