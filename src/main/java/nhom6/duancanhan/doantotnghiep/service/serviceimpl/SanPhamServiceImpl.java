package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamShowOnClient;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.mapper.SanPhamMapper;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import nhom6.duancanhan.doantotnghiep.util.DatabaseOperationHandler;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamMapper sanPhamMapper;
    private final SanPhamRepository sanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final KieuTayAoRepository kieuTayAoRepository;
    private final KieuCoAoRepository kieuCoAoRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @Override
    public List<SanPhamResponse> getAllSanPham() {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamRepository.findAll().stream()
                        .map(sanPhamMapper::toSanPhamResponse)
                        .toList(),
                "Lỗi khi lấy thông tin sản phẩm từ cơ sở dữ liệu"
        );
    }

    //Get List sp byId KieuCoAo
    @Override
    public List<SanPhamResponse> getSanPhamByKieuCoAoId(Integer id) {
        List<SanPham> sanPhams = sanPhamRepository.findByIdCoAo(id);
        return sanPhams.stream()
                .map(sanPhamMapper::toSanPhamResponse)
                .collect(Collectors.toList());
    }

    //Get List sp byId KieuTayAo
    @Override
    public List<SanPhamResponse> getSanPhamByKieuTayAoId(Integer id) {
        List<SanPham> sanPhams = sanPhamRepository.findByIdTayAo(id);
        return sanPhams.stream()
                .map(sanPhamMapper::toSanPhamResponse)
                .collect(Collectors.toList());
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

    @Override
    public Page<SanPhamResponse> timKiemSanPham(String keyword, Integer status, Integer thuongHieuId, Integer chatLieuId, Integer tayAoId, Integer coAoId, int page, int size) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            Pageable pageable = PageRequest.of(page, size);
            Page<SanPham> sanPhamPage;

            // Tạo điều kiện tìm kiếm
            String keywordSearch = (keyword != null && !keyword.isEmpty()) ? "%" + keyword + "%" : null;

            // Tìm kiếm theo nhiều tiêu chí
            sanPhamPage = sanPhamRepository.searchProducts(keywordSearch, status, thuongHieuId, chatLieuId, tayAoId, coAoId, pageable);

            // Ánh xạ kết quả từ entity sang DTO
            return sanPhamPage.map(sanPhamMapper::toSanPhamResponse);
        }, "Lỗi khi lấy thông tin từ cơ sở dữ liệu");
    }

    @Override
    public List<SanPhamShowOnClient> getAllSanPhamShowOnClient(String method) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
            List<SanPhamResponse> sanPhamResponses = new ArrayList<>(getAllSanPham());

            if ("get-random".equals(method)) {
                Collections.shuffle(sanPhamResponses);
            } else if (!"get-all".equals(method)) {
                return null;
            }

            List<SanPhamShowOnClient> sanPhamShowOnClients = new ArrayList<>();
            for (SanPhamResponse sanPhamResponse : sanPhamResponses) {
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findFirstBySanPhamId(sanPhamResponse.getId());
                if (sanPhamChiTiet != null) {
                    sanPhamShowOnClients.add(
                            SanPhamShowOnClient.builder()
                                    .sanPhamResponse(sanPhamResponse)
                                    .gia(sanPhamChiTiet.getGia())
                                    .build()
                    );
                }
            }

            if ("get-random".equals(method)) {
                while (sanPhamShowOnClients.size() < 8) {
                    for (SanPhamShowOnClient item : new ArrayList<>(sanPhamShowOnClients)) {
                        if (sanPhamShowOnClients.size() < 8) {
                            sanPhamShowOnClients.add(item);
                        } else {
                            break;
                        }
                    }
                }

                if (sanPhamShowOnClients.size() > 8) {
                    sanPhamShowOnClients = sanPhamShowOnClients.subList(0, 8);
                }
            }

            return sanPhamShowOnClients;
        }, "Lỗi khi lấy thông tin từ cơ sở dữ liệu");
    }

    @Override
    public SanPhamShowOnClient getSanPhamShowOnClientById(Integer id) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
                    SanPham sanPham = findSanPhamById(id);
                    return SanPhamShowOnClient.builder()
                            .sanPhamResponse(sanPhamMapper.toSanPhamResponse(sanPham))
                            .build();
                }, "Lỗi khi lấy thông tin sản phẩm từ cơ sở dữ liệu"
        );
    }

    @Override
    public Integer getIdSpFromTenSP(String tenSanPham) {
        return DatabaseOperationHandler.handleDatabaseOperation(
                () -> sanPhamRepository.findByTenSanPham(tenSanPham)
                , "Lỗi khi lấy thông tin sản phẩm từ cơ sở dữ liệu"
        );
    }

    @Override
    public List<SanPhamShowOnClient> searchSanPham(String tenThuongHieu, String tenChatLieu, String tenTayAo, String tenCoAo, String sort) {
        return DatabaseOperationHandler.handleDatabaseOperation(() -> {
                    List<SanPhamShowOnClient> listSanPhamShowOnClients = new ArrayList<>();
                    List<SanPhamResponse> listSanPham = sanPhamRepository.searchSanPham(tenThuongHieu, tenChatLieu, tenCoAo, tenTayAo)
                            .stream().map(sanPhamMapper::toSanPhamResponse).toList();
                    for (SanPhamResponse sanPhamResponse : listSanPham) {
                        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findFirstBySanPhamId(sanPhamResponse.getId());
                        if (sanPhamChiTiet != null) {
                            listSanPhamShowOnClients.add(
                                    SanPhamShowOnClient.builder()
                                            .sanPhamResponse(sanPhamResponse)
                                            .gia(sanPhamChiTiet.getGia())
                                            .build()
                            );
                        }
                    }
                    switch (sort) {
                        case "price-asc":
                            listSanPhamShowOnClients.sort(Comparator.comparing(SanPhamShowOnClient::getGia));
                            break;
                        case "price-desc":
                            listSanPhamShowOnClients.sort(Comparator.comparing(SanPhamShowOnClient::getGia).reversed());
                            break;
                        case "newest":
                            listSanPhamShowOnClients.sort((sp1, sp2)
                                    -> sp2.getSanPhamResponse().getNgayTao().compareTo(sp1.getSanPhamResponse().getNgayTao()));
                            break;
                    }
                    return listSanPhamShowOnClients;
                }, "Lỗi khi lấy thông tin từ cơ sở dữ liệu"
        );
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
