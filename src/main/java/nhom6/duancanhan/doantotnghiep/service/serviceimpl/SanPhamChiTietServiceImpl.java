package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.entity.SanPham;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamChiTiet;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.mapper.SanPhamChiTietMapper;
import nhom6.duancanhan.doantotnghiep.repository.KichCoRepository;
import nhom6.duancanhan.doantotnghiep.repository.MauSacRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamRepository;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SanPhamChiTietServiceImpl implements SanPhamChiTietService {

    private final SanPhamChiTietMapper sanPhamChiTietMapper;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final MauSacRepository mauSacRepository;
    private final KichCoRepository kichCoRepository;

    @Override
    public List<SanPhamChiTietResponse> getAllSanPhamChiTiet() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamChiTietRepository.findAll().stream()
                        .map(sanPhamChiTietMapper::toSanPhamChiTietResponse)
                        .toList(),
                "Lỗi khi lấy thông tin sản phẩm chi tiết từ cơ sở dữ liệu"
        );
    }

    @Override
    public SanPhamChiTietResponse getSanPhamChiTietById(Integer id) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamChiTietRepository.findById(id)
                        .map(sanPhamChiTietMapper::toSanPhamChiTietResponse)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm chi tiết với id: " + id)),
                "Lỗi khi lấy thông tin sản phẩm chi tiết từ cơ sở dữ liệu"
        );
    }

    @Override
    public List<SanPhamChiTietResponse> getSanPhamChiTietByIdSP(Integer idSP) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamChiTietRepository.findSanPhamChiTietByIdSanPham(idSP).stream()
                        .map(sanPhamChiTietMapper::toSanPhamChiTietResponse)
                        .toList(),
                "Lỗi khi lấy thông tin sản phẩm chi tiết từ cơ sở dữ liệu"
        );
    }

    @Override
    public SanPhamChiTietResponse storeSanPhamChiTiet(SanPhamChiTietRequest sanPhamChiTietRequest) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPhamChiTiet sanPhamChiTiet = mapSanPhamChiTietRequestToEntity(sanPhamChiTietRequest);
            return sanPhamChiTietMapper.toSanPhamChiTietResponse(sanPhamChiTietRepository.save(sanPhamChiTiet));
        }, "Lỗi khi thêm sản phẩm chi tiết từ cơ sở dũ liệu");
    }

    @Override
    public SanPhamChiTietResponse updateSanPhamChiTiet(Integer id, SanPhamChiTietRequest sanPhamChiTietRequest) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPhamChiTiet existingSanPhamChiTiet = findSanPhamChiTietById(id);
            updateSanPhamChiTietFromRequest(sanPhamChiTietRequest, existingSanPhamChiTiet);
            return sanPhamChiTietMapper.toSanPhamChiTietResponse(sanPhamChiTietRepository.save(existingSanPhamChiTiet));
        }, "Lỗi khi thay đỗi thông tin sản phẩm chi tiết từ cơ sở dũ liệu");
    }

    @Override
    public void deleteSanPhamChiTiet(Integer id) {
        DatabaseOperationHandler.handleDatabaseOperation(() -> {
            validateSPCTExists(id);
            sanPhamRepository.deleteById(id);
            return null;
        }, "Lỗi khi xóa sản phẩm từ cơ sở dữ liệu");
    }

    @Override
    public void sortDeleteSanPhamChiTiet(Integer id) {
        DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPhamChiTiet sanPhamChiTiet = findSanPhamChiTietById(id);
            sanPhamChiTiet.setTrangThai(0);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            return null;
        },"Lỗi khi xóa sản phẩm từ cơ sở dữ liệu");
    }

    private void validateSPCTExists(Integer id) {
        if (!sanPhamChiTietRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm chi tiết với id: " + id);
        }
    }

    private SanPhamChiTiet mapSanPhamChiTietRequestToEntity(SanPhamChiTietRequest sanPhamChiTietRequest) {
        SanPham sanPham = findSanPhamById(sanPhamChiTietRequest.getIdSanPham());
        KichCo kichCo = findKichCoById(sanPhamChiTietRequest.getIdKichCo());
        MauSac mauSac = findMauSacById(sanPhamChiTietRequest.getIdMauSac());

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietMapper.toSanPhamChiTiet(sanPhamChiTietRequest);
        sanPhamChiTiet.setKichCo(kichCo);
        sanPhamChiTiet.setMauSac(mauSac);
        sanPhamChiTiet.setSanPham(sanPham);
        return sanPhamChiTiet;
    }

    private void updateSanPhamChiTietFromRequest(SanPhamChiTietRequest sanPhamChiTietRequest, SanPhamChiTiet sanPhamChiTiet) {
        SanPham sanPham = findSanPhamById(sanPhamChiTietRequest.getIdSanPham());
        KichCo kichCo = findKichCoById(sanPhamChiTietRequest.getIdKichCo());
        MauSac mauSac = findMauSacById(sanPhamChiTietRequest.getIdMauSac());

        sanPhamChiTietMapper.updateSPCTFromSCPTRequest(sanPhamChiTietRequest, sanPhamChiTiet);
        sanPhamChiTiet.setKichCo(kichCo);
        sanPhamChiTiet.setMauSac(mauSac);
        sanPhamChiTiet.setSanPham(sanPham);
    }

    private SanPhamChiTiet findSanPhamChiTietById(Integer id) {
        return sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm chi tiết với id: " + id));
    }

    private SanPham findSanPhamById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
    }

    private KichCo findKichCoById(Integer id) {
        return kichCoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kích cỡ với id: " + id));
    }

    private MauSac findMauSacById(Integer id) {
        return mauSacRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu sắc với id: " + id));
    }
}
