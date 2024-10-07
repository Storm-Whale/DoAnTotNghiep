package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KieuTayAo;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
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
public class MauSacController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/mau-sac/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(mauSacService.getAll());
    }

    @GetMapping("/mau-sac/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<MauSac> page = mauSacService.phanTrang(pageNo,pageSize);
        List<MauSac> listMS = page.getContent();
        return ResponseEntity.ok(listMS);
    }

    @GetMapping("/mau-sac/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(mauSacService.detail(id));
    }

    @PostMapping("/mau-sac/add")
    public ResponseEntity<?> add(@RequestBody MauSac mauSac) {
        mauSacService.addMauSac(mauSac);
        return ResponseEntity.ok(mauSac);
    }

    @PutMapping("/mau-sac/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody MauSac mauSac) {
        mauSacService.updateMauSac(id, mauSac);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/mau-sac/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        mauSacService.deleteMauSac(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }
}
