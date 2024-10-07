package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;
import nhom6.duancanhan.doantotnghiep.entity.SanPhamGioHang;
import nhom6.duancanhan.doantotnghiep.mapper.SanPhamGioHangMapper;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamGioHangService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamGioHangImpl implements SanPhamGioHangService {
    private final SanPhamGioHangRepository sanPhamGioHangRepository;
    private final SanPhamGioHangMapper sanPhamGioHangMapper;
    @Override
    public List<SanPhamGioHangResponse> getAll() {
        return sanPhamGioHangRepository.findAll()
                .stream()
                .map(sanPhamGioHangMapper::toSPGioHangResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SanPhamGioHangResponse getById(Integer id) {
        return sanPhamGioHangRepository.findById(id)
                .map(sanPhamGioHangMapper::toSPGioHangResponse)
                .orElseThrow(() -> new EntityNotFoundException("Sp GH not found with id " + id));
    }

    @Override
    public SanPhamGioHangResponse create(SanPhamGioHangRequest sanPhamGioHangRequest) {
        SanPhamGioHang sanPhamGioHang = sanPhamGioHangMapper.toSPGioHang(sanPhamGioHangRequest);
        SanPhamGioHang savedSPGH = sanPhamGioHangRepository.save(sanPhamGioHang);
        return sanPhamGioHangMapper.toSPGioHangResponse(savedSPGH);
    }

    @Override
    public SanPhamGioHangResponse update(Integer id, SanPhamGioHangRequest sanPhamGioHangRequest) {
        SanPhamGioHang exiting = sanPhamGioHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sp GH not found with id: " + id));
        sanPhamGioHangMapper.updateSPGioHang(exiting.getId(), sanPhamGioHangRequest);
        SanPhamGioHang updateGH = sanPhamGioHangRepository.save(exiting);
        return sanPhamGioHangMapper.toSPGioHangResponse(updateGH);
    }
    @Override
    public void delete(Integer id) {
        SanPhamGioHang gioHang = sanPhamGioHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sp GH not found with id " + id));
        sanPhamGioHangRepository.delete(gioHang);
    }
}
