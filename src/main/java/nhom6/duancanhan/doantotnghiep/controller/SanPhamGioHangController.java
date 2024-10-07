package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangResponse;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamGioHangService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/san-pham-gio-hang")
public class SanPhamGioHangController {
    private final SanPhamGioHangService sanPhamGioHangService;
    @GetMapping("/index")
    public ResponseEntity<List<SanPhamGioHangResponse>> getAll(){
        return new ResponseEntity<>(sanPhamGioHangService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<SanPhamGioHangResponse> findById(@PathVariable Integer id){
        SanPhamGioHangResponse spGioHangReponse = sanPhamGioHangService.getById(id);
        return new ResponseEntity<>(spGioHangReponse, HttpStatus.OK);
    }
    @PostMapping("/store")
    public ResponseEntity<?> store(SanPhamGioHangRequest gioHangRequest){
        SanPhamGioHangResponse gioHangReponse = sanPhamGioHangService.create(gioHangRequest);
        return new ResponseEntity<>(gioHangReponse, HttpStatus.CREATED);
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, SanPhamGioHangRequest gioHangRequest) {
        SanPhamGioHangResponse gioHangReponse = sanPhamGioHangService.update(id, gioHangRequest);
        return new ResponseEntity<>(gioHangReponse, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        sanPhamGioHangService.delete(id);
        return ResponseEntity.ok("Delete success:" + id);
    }
}
