package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaService phieuGiamGiaService;
    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(phieuGiamGiaService.getAll());
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(phieuGiamGiaService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaService.create(phieuGiamGia);
        return ResponseEntity.ok(phieuGiamGia);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody PhieuGiamGia phieuGiamGia) {
        phieuGiamGiaService.update(id, phieuGiamGia);
        return ResponseEntity.ok("update success");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        phieuGiamGiaService.delete(id);
        return ResponseEntity.ok("delete success");
    }
}
