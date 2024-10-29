package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
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
@RequestMapping("/dia-chi/")
public class DiaChiController {
    @Autowired
    private DiaChiService diaChiService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(diaChiService.getAll());
    }

    @GetMapping("/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<DiaChi> page = diaChiService.phanTrang(pageNo,pageSize);
        List<DiaChi> listNV = page.getContent();
        return ResponseEntity.ok(listNV);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(diaChiService.detail(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody DiaChi diaChi) {
        diaChiService.addDiaChi(diaChi);
        return ResponseEntity.ok(diaChi);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody DiaChi diaChi) {
        diaChiService.updateDiaChi(id, diaChi);
        return ResponseEntity.ok("update thanh cong");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        diaChiService.deleteDiaChi(id);
        return ResponseEntity.ok("delete thanh cong");
    }
}
