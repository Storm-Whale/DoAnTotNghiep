package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.ThuongHieu;
import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/thuong-hieu")
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

    @PostMapping(value = "/quick-add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickAdd(@RequestParam(name = "ten") String ten) {
        try {
            ThuongHieu thuongHieu = ThuongHieu.builder()
                    .tenThuongHieu(ten)
                    .trangThai(1)
                    .build();

            // Lưu và lấy entity đã được persist
            ThuongHieu savedThuongHieu = thuongHieuService.addThuongHieu(thuongHieu);

            // Tạo response data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", savedThuongHieu.getId());
            response.put("ten", savedThuongHieu.getTenThuongHieu());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Thêm thất bại: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
