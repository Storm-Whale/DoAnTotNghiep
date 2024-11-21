package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonRequest;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonResponse;
import nhom6.duancanhan.doantotnghiep.entity.LichSuHoaDon;
import nhom6.duancanhan.doantotnghiep.mapper.LichSuHoaDonMapper;
import nhom6.duancanhan.doantotnghiep.repository.LichSuHoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.LichSuHoaDonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LichSuHoaDonImpl implements LichSuHoaDonService {

    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final LichSuHoaDonMapper lichSuHoaDonMapper;


    @Override
    public List<LichSuHoaDonResponse> getAll() {
        return lichSuHoaDonRepository.findAll()
                .stream()
                .map(lichSuHoaDonMapper::toLSHoaDonResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LichSuHoaDonResponse getById(Integer id) {
        return lichSuHoaDonRepository.findById(id)
                .map(lichSuHoaDonMapper::toLSHoaDonResponse)
                .orElseThrow(() -> new EntityNotFoundException("LS HD not found with id " + id));
    }

    @Override
    public LichSuHoaDonResponse create(LichSuHoaDonRequest lichSuHoaDonRequest) {
        LichSuHoaDon lsHoaDon = lichSuHoaDonMapper.toLSHoaDon(lichSuHoaDonRequest);
        LichSuHoaDon savedLSHD = lichSuHoaDonRepository.save(lsHoaDon);
        return lichSuHoaDonMapper.toLSHoaDonResponse(savedLSHD);
    }

    @Override
    public LichSuHoaDonResponse update(Integer id, LichSuHoaDonRequest lichSuHoaDonRequest) {
        LichSuHoaDon exiting = lichSuHoaDonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LS HD not found with id: " + id));
        lichSuHoaDonMapper.updateLSHoaDon(exiting.getId(), lichSuHoaDonRequest);
        LichSuHoaDon updateGH = lichSuHoaDonRepository.save(exiting);
        return lichSuHoaDonMapper.toLSHoaDonResponse(updateGH);
    }

    @Override
    public void delete(Integer id) {
        LichSuHoaDon gioHang = lichSuHoaDonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LS HD not found with id " + id));
        lichSuHoaDonRepository.delete(gioHang);
    }
}
