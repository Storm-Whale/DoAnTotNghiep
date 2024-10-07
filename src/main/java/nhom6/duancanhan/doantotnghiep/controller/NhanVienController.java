package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nhan-vien/")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(nhanVienService.getAll());
    }

    @GetMapping("/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<NhanVien> page = nhanVienService.phanTrang(pageNo,pageSize);
        List<NhanVien> listNV = page.getContent();
        return ResponseEntity.ok(listNV);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(nhanVienService.detail(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody NhanVien nhanVien) {
        nhanVienService.addNhanVien(nhanVien);
        return ResponseEntity.ok(nhanVien);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody NhanVien nhanVien) {
        nhanVienService.updateNhanVien(id, nhanVien);
        return ResponseEntity.ok("update thanh cong");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        nhanVienService.deleteNhanVien(id);
        return ResponseEntity.ok("delete thanh cong");
    }
}
