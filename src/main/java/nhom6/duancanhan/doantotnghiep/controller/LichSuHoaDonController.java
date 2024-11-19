package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonRequest;
import nhom6.duancanhan.doantotnghiep.dto.LichSuHoaDonResponse;
import nhom6.duancanhan.doantotnghiep.service.service.LichSuHoaDonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/lich-su-hoa-don")
public class LichSuHoaDonController {
    private final LichSuHoaDonService lichSuHoaDonService;
    @GetMapping("/index")
    public ResponseEntity<List<LichSuHoaDonResponse>> getAll(){
        return new ResponseEntity<>(lichSuHoaDonService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<LichSuHoaDonResponse> findById(@PathVariable Integer id){
        LichSuHoaDonResponse lichSuHoaDonResponse = lichSuHoaDonService.getById(id);
        return new ResponseEntity<>(lichSuHoaDonResponse, HttpStatus.OK);
    }
    @PostMapping("/store")
    public ResponseEntity<?> store(LichSuHoaDonRequest lichSuHoaDonRequest){
        LichSuHoaDonResponse lichSuHoaDonResponse = lichSuHoaDonService.create(lichSuHoaDonRequest);
        return new ResponseEntity<>(lichSuHoaDonResponse, HttpStatus.CREATED);
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Integer id, LichSuHoaDonRequest lichSuHoaDonRequest) {
        LichSuHoaDonResponse lichSuHoaDonResponse = lichSuHoaDonService.update(id, lichSuHoaDonRequest);
        return new ResponseEntity<>(lichSuHoaDonResponse, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        lichSuHoaDonService.delete(id);
        return ResponseEntity.ok("Delete success:" + id);
    }
}
