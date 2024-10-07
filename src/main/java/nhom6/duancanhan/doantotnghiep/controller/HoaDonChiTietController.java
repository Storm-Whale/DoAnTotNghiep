package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepo;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class HoaDonChiTietController {
    @Autowired
    HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @GetMapping("/hoadon")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(hoaDonChiTietService.getAll());
    }

    @GetMapping("/phan-trang/{page}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "page") int pagee) {
        int pageSize = 3;
        Page<HoaDonChiTiet> page = hoaDonChiTietService.phanTrang(pagee,pageSize);
        List<HoaDonChiTiet> listHDCT = page.getContent();
        return ResponseEntity.ok(listHDCT);
    }


    @PostMapping("/addHoaDonct")
    public ResponseEntity<?> addHD(@RequestBody HoaDonChiTiet hoaDon) {
        hoaDonChiTietService.addHoaDon(hoaDon);
        return ResponseEntity.ok(hoaDon);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        hoaDonChiTietService.deleteHoaDon(id);
        return ResponseEntity.ok("delete thanh cong");
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(hoaDonChiTietService.detail(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody HoaDonChiTiet hoaDon) {
        hoaDonChiTietService.updateHoaDon(id, hoaDon);
        return ResponseEntity.ok("update thanh cong");
    }
}
