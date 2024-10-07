package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.GioHangReponse;
import nhom6.duancanhan.doantotnghiep.dto.GioHangRequest;
import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import nhom6.duancanhan.doantotnghiep.mapper.GioHangMapper;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.GioHangService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GioHangServiceImpl implements GioHangService {
    private final GioHangRepository gioHangRepository;
    private final GioHangMapper gioHangMapper;

//    public GioHangServiceImpl(GioHangRepository gioHangRepository, GioHangMapper gioHangMapper) {
//        this.gioHangRepository = gioHangRepository;
//        this.gioHangMapper = gioHangMapper;
//    }

    @Override
    public List<GioHangReponse> getAll() {
        return gioHangRepository.findAll()
                .stream()
                .map(gioHangMapper::toGioHangResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GioHangReponse getById(Integer id) {
        return gioHangRepository.findById(id)
                .map(gioHangMapper::toGioHangResponse)
                .orElseThrow(() -> new EntityNotFoundException("Gio hang not found with id " + id));
    }

    @Override
    public GioHangReponse create(GioHangRequest gioHangRequest) {
        GioHang gioHang = gioHangMapper.toGioHang(gioHangRequest);
        GioHang savedGH = gioHangRepository.save(gioHang);
        return gioHangMapper.toGioHangResponse(savedGH);
    }

    @Override
    public GioHangReponse update(Integer id, GioHangRequest gioHangRequest) {
        GioHang exiting = gioHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gio hang not found with id: " + id));
        gioHangMapper.updateGioHangRequest(exiting.getId(), gioHangRequest);
        GioHang updateGH = gioHangRepository.save(exiting);
        return gioHangMapper.toGioHangResponse(updateGH);
    }

    @Override
    public void delete(Integer id) {
        GioHang gioHang = gioHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gio hang not found with id " + id));
        gioHangRepository.delete(gioHang);
    }
}
