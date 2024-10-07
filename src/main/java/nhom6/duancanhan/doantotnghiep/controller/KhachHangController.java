package nhom6.duancanhan.doantotnghiep.controller;


import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/khach-hang")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(khachHangService.getAll());
    }

    @GetMapping("/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<KhachHang> page = khachHangService.phanTrang(pageNo,pageSize);
        List<KhachHang> listKH = page.getContent();
        return ResponseEntity.ok(listKH);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(khachHangService.detail(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody KhachHang khachHang) {
        khachHangService.addKhachHang(khachHang);
        return ResponseEntity.ok(khachHang);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody KhachHang khachHang) {
        khachHangService.updateKhachHang(id, khachHang);
        return ResponseEntity.ok("update thanh cong");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        khachHangService.deleteKhachHang(id);
        return ResponseEntity.ok("delete thanh cong");
    }

}
