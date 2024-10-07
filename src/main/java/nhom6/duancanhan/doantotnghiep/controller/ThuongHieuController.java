package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
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
public class ThuongHieuController {
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/thuong-hieu/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(thuongHieuService.getAll());
    }

    @GetMapping("/thuong-hieu/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<ThuongHieu> page = thuongHieuService.phanTrang(pageNo,pageSize);
        List<ThuongHieu> listTH = page.getContent();
        return ResponseEntity.ok(listTH);
    }

    @GetMapping("/thuong-hieu/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(thuongHieuService.detail(id));
    }

    @PostMapping("/thuong-hieu/add")
    public ResponseEntity<?> add(@RequestBody ThuongHieu thuongHieu) {
        thuongHieuService.addThuongHieu(thuongHieu);
        return ResponseEntity.ok(thuongHieu);
    }

    @PutMapping("/thuong-hieu/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody ThuongHieu thuongHieu) {
        thuongHieuService.updateThuongHieu(id, thuongHieu);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/thuong-hieu/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        thuongHieuService.deleteThuongHieu(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }
}
