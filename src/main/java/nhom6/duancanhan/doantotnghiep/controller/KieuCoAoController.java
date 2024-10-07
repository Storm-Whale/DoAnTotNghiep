package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KieuCoAo;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
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
public class KieuCoAoController {
    @Autowired
    private KieuCoAoService kieuCoAoService;

    @GetMapping("/kieu-co-ao/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(kieuCoAoService.getAll());
    }

    @GetMapping("/kieu-co-ao/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<KieuCoAo> page = kieuCoAoService.phanTrang(pageNo, pageSize);
        List<KieuCoAo> listKCA = page.getContent();
        return ResponseEntity.ok(listKCA);
    }

    @GetMapping("/kieu-co-ao/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(kieuCoAoService.detail(id));
    }

    @PostMapping("/kieu-co-ao/add")
    public ResponseEntity<?> add(@RequestBody KieuCoAo kieuCoAo) {
        kieuCoAoService.addKieuCoAo(kieuCoAo);
        return ResponseEntity.ok(kieuCoAo);
    }

    @PutMapping("/kieu-co-ao/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody KieuCoAo kieuCoAo) {
        kieuCoAoService.updateKieuCoAo(id, kieuCoAo);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/kieu-co-ao/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        kieuCoAoService.deleteKieuCoAo(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }
}
