package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.GioHangReponse;
import nhom6.duancanhan.doantotnghiep.dto.GioHangRequest;
import nhom6.duancanhan.doantotnghiep.service.service.GioHangService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/gio-hang")
public class GioHangController {
    private final GioHangService gioHangService;
    @GetMapping("/index")
    public ResponseEntity<List<GioHangReponse>> getAll(){
        return new ResponseEntity<>(gioHangService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<GioHangReponse> findById(@PathVariable Integer id){
        GioHangReponse gioHangReponse = gioHangService.getById(id);
        return new ResponseEntity<>(gioHangReponse, HttpStatus.OK);
    }
    @PostMapping("/store")
    public ResponseEntity<?> store(GioHangRequest gioHangRequest){
        GioHangReponse gioHangReponse = gioHangService.create(gioHangRequest);
        return new ResponseEntity<>(gioHangReponse, HttpStatus.CREATED);
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, GioHangRequest gioHangRequest) {
        GioHangReponse gioHangReponse = gioHangService.update(id, gioHangRequest);
        return new ResponseEntity<>(gioHangReponse, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        gioHangService.delete(id);
        return ResponseEntity.ok("Delete success:" + id);
    }
}
