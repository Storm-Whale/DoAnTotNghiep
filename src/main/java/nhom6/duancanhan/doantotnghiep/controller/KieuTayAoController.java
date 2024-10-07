package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.ChatLieu;
import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
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
public class KieuTayAoController {
    @Autowired
    private KieuTayAoService kieuTayAoService;

    @GetMapping("/kieu-tay-ao/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(kieuTayAoService.getAll());
    }

    @GetMapping("/kieu-tay-ao/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<KieuTayAo> page = kieuTayAoService.phanTrang(pageNo,pageSize);
        List<KieuTayAo> listKTA = page.getContent();
        return ResponseEntity.ok(listKTA);
    }

    @GetMapping("/kieu-tay-ao/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(kieuTayAoService.detail(id));
    }

    @PostMapping("/kieu-tay-ao/add")
    public ResponseEntity<?> add(@RequestBody KieuTayAo kieuTayAo) {
        kieuTayAoService.addKieuTayAo(kieuTayAo);
        return ResponseEntity.ok(kieuTayAo);
    }

    @PutMapping("/kieu-tay-ao/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody KieuTayAo kieuTayAo) {
        kieuTayAoService.updateKieuTayAo(id, kieuTayAo);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/kieu-tay-ao/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        kieuTayAoService.deleteKieuTayAo(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }
}
