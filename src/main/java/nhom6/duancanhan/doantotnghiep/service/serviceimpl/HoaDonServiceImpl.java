package nhom6.duancanhan.doantotnghiep.service.serviceimpl;


import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImpl implements HoaDonService {

   @Autowired
   private HoaDonRepository hoaDonRepository;
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
    public void addHoaDon(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public void updateHoaDon(Integer id, HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public Optional<HoaDon> detail(Integer id) {
        Optional<HoaDon> hoaDon = hoaDonRepository.findById(id);
        return Optional.of(hoaDon.get());
    }

    @Override
    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    public Page<HoaDon> timKiem(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findByTenNguoiNhanContaining(keyword, pageable);
    }

//    @Override
//    public PageDTO<List<HoaDonDTO>> phanTrang(PageRequestDTO pageRequestDTO) {
//        pageRequestDTO.setPage(pageRequestDTO.getPage() == null ? 0 : pageRequestDTO.getPage());
//        pageRequestDTO.setSize(pageRequestDTO.getSize() == null ? 5 : pageRequestDTO.getSize());
//
//        Page<HoaDon> pageEntity = hoaDonRepository.findAll(
//                PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize())
//        );
//
//        // Kiểm tra xem có dữ liệu nào trong pageEntity không
//        System.out.println("Total elements in HoaDon: " + pageEntity.getTotalElements());
//
//        List<HoaDonDTO> listDto = pageEntity.get().map(this::converToDto).collect(Collectors.toList());
//
//        // Kiểm tra danh sách DTO
//        System.out.println("Total DTOs: " + listDto.size());
//
//        return PageDTO.<List<HoaDonDTO>>builder()
//                .data(listDto)
//                .totalElements(pageEntity.getTotalElements())
//                .totalPages(pageEntity.getTotalPages())
//                .build();
//    }
//    @Override
//    public HoaDon themHoaDon(HoaDonDTO hoaDonDTO) {
//        HoaDon hoaDon = converToEntity(hoaDonDTO);
//        return hoaDonRepository.save(hoaDon);
//    }
//
//    @Override
//    public HoaDonDTO converToDto(HoaDon hoaDon) {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper.map(hoaDon, HoaDonDTO.class);
//    }
//
//    @Override
//    public HoaDon converToEntity(HoaDonDTO hoaDonDTO) {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper.map(hoaDonDTO, HoaDon.class);
//    }
}
