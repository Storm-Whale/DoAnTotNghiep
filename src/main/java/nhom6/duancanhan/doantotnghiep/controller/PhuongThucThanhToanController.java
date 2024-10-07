package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.PhuongThucThanhToan;
import nhom6.duancanhan.doantotnghiep.service.service.PhuongThucThanhToanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/phuong-thuc-thanh-toan")
public class PhuongThucThanhToanController {
    private final PhuongThucThanhToanService phuongThucThanhToanService;
    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(phuongThucThanhToanService.getAll());
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(phuongThucThanhToanService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody PhuongThucThanhToan phuongThucThanhToan) {
        phuongThucThanhToanService.create(phuongThucThanhToan);
        return ResponseEntity.ok(phuongThucThanhToan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody PhuongThucThanhToan phuongThucThanhToan) {
        phuongThucThanhToanService.update(id, phuongThucThanhToan);
        return ResponseEntity.ok("update success");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        phuongThucThanhToanService.delete(id);
        return ResponseEntity.ok("delete success");
    }
}
