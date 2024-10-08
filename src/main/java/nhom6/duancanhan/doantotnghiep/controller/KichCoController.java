package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.KichCo;
import nhom6.duancanhan.doantotnghiep.entity.MauSac;
import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
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
public class KichCoController {
    @Autowired
    private KichCoService kichCoService;

    @GetMapping("/kich-co/hien-thi")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(kichCoService.getAll());
    }

    @GetMapping("/kich-co/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<KichCo> page = kichCoService.phanTrang(pageNo,pageSize);
        List<KichCo> listKC = page.getContent();
        return ResponseEntity.ok(listKC);
    }

    @GetMapping("/kich-co/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(kichCoService.detail(id));
    }

    @PostMapping("/kich-co/add")
    public ResponseEntity<?> add(@RequestBody KichCo kichCo) {
        kichCoService.addKichCo(kichCo);
        return ResponseEntity.ok(kichCo);
    }

    @PutMapping("/kich-co/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody KichCo kichCo) {
        kichCoService.updateKichCo(id, kichCo);
        return ResponseEntity.ok("Update Thanh Cong");
    }

    @DeleteMapping("/kich-co/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        kichCoService.deleteKichCo(id);
        return ResponseEntity.ok("Delete Thanh Cong");
    }

}
