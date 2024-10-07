package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
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
@RequestMapping("/tai-khoan/")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(taiKhoanService.getAll());
    }

    @GetMapping("/phan-trang/{pageNo}")
    public ResponseEntity<?> phanTrang(@PathVariable(value = "pageNo") int pageNo) {
        int pageSize = 3;
        Page<TaiKhoan> page = taiKhoanService.phanTrang(pageNo,pageSize);
        List<TaiKhoan> listTK = page.getContent();
        return ResponseEntity.ok(listTK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(taiKhoanService.detail(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TaiKhoan taiKhoan) {
        taiKhoanService.addTaiKhoan(taiKhoan);
        return ResponseEntity.ok(taiKhoan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody TaiKhoan taiKhoan) {
        taiKhoanService.updateTaiKhoan(id, taiKhoan);
        return ResponseEntity.ok("update thanh cong");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        taiKhoanService.deleteTaiKhoan(id);
        return ResponseEntity.ok("delete thanh cong");
    }
}
