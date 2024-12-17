package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.DiaChi;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dia-chi")
public class DiaChiController {

    private final DiaChiService diaChiService;
    private final KhachHangService khachHangService;

    @PostMapping(value = "/add-new-address")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewAddress(
            @RequestParam(name = "idKhachHang") Integer idKhachHang,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "district") String district,
            @RequestParam(name = "ward") String ward,
            @RequestParam(name = "detailAddress") String detailAddress,
            @RequestParam(name = "email", required = false) String email,
            HttpSession session
    ) {
        KhachHang khachHang = khachHangService.detail(idKhachHang);
        if (email != null) {
            khachHang.setEmail(email);
            khachHangService.updateKhachHang(khachHang);
        }
        session.setAttribute("user", khachHang);
        DiaChi diaChi = DiaChi.builder()
                .khachHang(khachHang)
                .tenKhachHang(name.trim())
                .soDienThoai(phone.trim())
                .xa(ward.trim())
                .huyen(district.trim())
                .thanhPho(city.trim())
                .diaChiChiTiet(detailAddress.trim())
                .trangThai(1)
                .build();
        diaChiService.addDiaChi(diaChi);
    }

    @PostMapping(value = "/update-address/{idDiaChi}")
    @ResponseStatus(HttpStatus.OK)
    public void updateAddress(
            @PathVariable(name = "idDiaChi") Integer idDiaChi,
            @RequestParam(name = "idKhachHang") Integer idKhachHang,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "district") String district,
            @RequestParam(name = "ward") String ward,
            @RequestParam(name = "detailAddress") String detailAddress,
            @RequestParam(name = "email", required = false) String email,
            HttpSession session
    ) {
        KhachHang khachHang = khachHangService.detail(idKhachHang);
        if (email != null) {
            khachHang.setEmail(email);
            khachHangService.updateKhachHang(khachHang);
        }
        session.setAttribute("user", khachHang);
        DiaChi diaChi = diaChiService.getDiaChiById(idDiaChi);
        diaChi.setDiaChiChiTiet(detailAddress.trim());
        diaChi.setKhachHang(khachHang);
        diaChi.setTenKhachHang(name.trim());
        diaChi.setSoDienThoai(phone.trim());
        diaChi.setXa(district.trim());
        diaChi.setHuyen(ward.trim());
        diaChi.setThanhPho(city.trim());
        diaChiService.updateDiaChi(idDiaChi, diaChi);
    }

    @PostMapping("/delete-address/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteAddress(@PathVariable(name = "id") Integer id) {
        DiaChi diaChi = diaChiService.getDiaChiById(id);
        diaChi.setTrangThai(0);
        diaChiService.updateDiaChi(id, diaChi);
    }
}
