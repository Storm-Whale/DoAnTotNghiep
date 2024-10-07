package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepo;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HoaDonController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    HoaDonRepo repo;
    @GetMapping("/hoadon")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(hoaDonService.getAll());
    }

    @GetMapping("/hoadon2")
    public List<HoaDonDTO> getAll2() {
        return repo.getAll();
    }

    @GetMapping("/phantrang/{page}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "page") int pagee) {
        int pageSize = 3;  // Kích thước trang là 3
        Pageable pageable = PageRequest.of(pagee - 1, pageSize); // Tạo Pageable (Spring sử dụng trang 0-based)
        Page<HoaDonDTO> page = repo.phanTrang(pageable);  // Gọi phương thức phân trang của repo
        List<HoaDonDTO> listKH = page.getContent();  // Lấy danh sách nội dung từ trang hiện tại
        return ResponseEntity.ok(listKH);  // Trả về danh sách dưới dạng JSON
    }
    @PostMapping("/addHoaDon")
    public ResponseEntity<?> addHD(@RequestBody HoaDon hoaDon) {
        hoaDonService.addHoaDon(hoaDon);
        return ResponseEntity.ok(hoaDon);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        hoaDonService.deleteHoaDon(id);
        return ResponseEntity.ok("delete thanh cong");
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(hoaDonService.detail(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody HoaDon hoaDon) {
        hoaDonService.updateHoaDon(id, hoaDon);
        return ResponseEntity.ok("update thanh cong");
    }
}
