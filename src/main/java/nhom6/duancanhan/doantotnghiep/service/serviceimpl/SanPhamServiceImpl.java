package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.mapper.SanPhamMapper;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamMapper sanPhamMapper;
    private final SanPhamRepository sanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final KieuTayAoRepository kieuTayAoRepository;
    private final KieuCoAoRepository kieuCoAoRepository;

    @Override
    public List<SanPhamResponse> getAllSanPham() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamRepository.findAll().stream()
                        .map(sanPhamMapper::toSanPhamResponse)
                        .toList(),
                "Lỗi khi lấy thông tin sản phẩm từ cơ sở dữ liệu"
        );
    }

    @Override
    public SanPhamResponse getSanPhamById(Integer id) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamRepository.findById(id)
                        .map(sanPhamMapper::toSanPhamResponse)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id)),
                "Lỗi khi lấy thông tin sản phẩm từ cơ sở dữ liệu"
        );
    }

    @Override
    public SanPhamResponse storeSanPham(SanPhamRequest sanPhamRequest) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            validateDuplicateProductName(sanPhamRequest.getTenSanPham());

            SanPham sanPham = mapSanPhamRequestToEntity(sanPhamRequest);
            return sanPhamMapper.toSanPhamResponse(sanPhamRepository.save(sanPham));
        }, "Lỗi khi thêm sản phẩm từ cơ sở dữ liệu");
    }

    @Override
    public SanPhamResponse updateSanPham(Integer id, SanPhamRequest sanPhamRequest) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPham existingSanPham = findSanPhamById(id);
            validateDuplicateProductNameExceptCurrent(sanPhamRequest.getTenSanPham(), existingSanPham.getId());

            updateSanPhamFromRequest(sanPhamRequest, existingSanPham);
            return sanPhamMapper.toSanPhamResponse(sanPhamRepository.save(existingSanPham));
        }, "Lỗi khi thay đổi thông tin sản phẩm từ cơ sở dữ liệu");
    }

    @Override
    public void deleteSanPham(Integer id) {
        DatabaseOperationHandler.handleDatabaseOperation(() -> {
            validateSanPhamExists(id);
            sanPhamRepository.deleteById(id);
            return null;
        }, "Lỗi khi xóa sản phẩm từ cơ sở dữ liệu");
    }

    @Override
    public void sortDeleteSanPham(Integer id) {
        DatabaseOperationHandler.handleDatabaseOperation(() -> {
            SanPham existingSanPham = findSanPhamById(id);
            existingSanPham.setTrangThai(0);
            sanPhamRepository.save(existingSanPham);
            return null;
        }, "Lỗi khi xóa sản phẩm từ cơ sở dữ liệu");
    }

    private void validateDuplicateProductName(String tenSanPham) {
        if (sanPhamRepository.existsSanPhamByTenSanPham(tenSanPham)) {
            throw new DuplicateKeyException("Đã có sản phẩm với tên: " + tenSanPham);
        }
    }

    private void validateDuplicateProductNameExceptCurrent(String tenSanPham, Integer sanPhamId) {
        if (sanPhamRepository.existsSanPhamByTenSanPhamAndIdNot(tenSanPham, sanPhamId)) {
            throw new DuplicateKeyException("Đã có sản phẩm với tên: " + tenSanPham);
        }
    }

    private void validateSanPhamExists(Integer id) {
        if (!sanPhamRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id);
        }
    }

    private SanPham mapSanPhamRequestToEntity(SanPhamRequest sanPhamRequest) {
        ThuongHieu thuongHieu = findThuongHieuById(sanPhamRequest.getIdThuongHieu());
        ChatLieu chatLieu = findChatLieuById(sanPhamRequest.getIdChatLieu());
        KieuTayAo kieuTayAo = findKieuTayAoById(sanPhamRequest.getIdTayAo());
        KieuCoAo kieuCoAo = findKieuCoAoById(sanPhamRequest.getIdCoAo());

        SanPham sanPham = sanPhamMapper.toSanPham(sanPhamRequest);
        sanPham.setThuongHieu(thuongHieu);
        sanPham.setChatLieu(chatLieu);
        sanPham.setTayAo(kieuTayAo);
        sanPham.setCoAo(kieuCoAo);
        return sanPham;
    }

    private void updateSanPhamFromRequest(SanPhamRequest sanPhamRequest, SanPham sanPham) {
        ThuongHieu thuongHieu = findThuongHieuById(sanPhamRequest.getIdThuongHieu());
        ChatLieu chatLieu = findChatLieuById(sanPhamRequest.getIdChatLieu());
        KieuTayAo kieuTayAo = findKieuTayAoById(sanPhamRequest.getIdTayAo());
        KieuCoAo kieuCoAo = findKieuCoAoById(sanPhamRequest.getIdCoAo());

        sanPhamMapper.updateSanPhamFromSanPhamRequest(sanPhamRequest, sanPham);
        sanPham.setThuongHieu(thuongHieu);
        sanPham.setChatLieu(chatLieu);
        sanPham.setTayAo(kieuTayAo);
        sanPham.setCoAo(kieuCoAo);
    }

    private SanPham findSanPhamById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
    }

    private ThuongHieu findThuongHieuById(Integer idThuongHieu) {
        return thuongHieuRepository.findById(idThuongHieu)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy thương hiệu với id: " + idThuongHieu));
    }

    private ChatLieu findChatLieuById(Integer idChatLieu) {
        return chatLieuRepository.findById(idChatLieu)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chất liệu với id: " + idChatLieu));
    }

    private KieuTayAo findKieuTayAoById(Integer idTayAo) {
        return kieuTayAoRepository.findById(idTayAo)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kiểu tay áo với id: " + idTayAo));
    }

    private KieuCoAo findKieuCoAoById(Integer idCoAo) {
        return kieuCoAoRepository.findById(idCoAo)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kiểu cổ áo với id: " + idCoAo));
    }
}
