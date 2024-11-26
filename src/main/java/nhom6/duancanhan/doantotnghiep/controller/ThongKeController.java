package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/thong-ke")
public class ThongKeController {

    private final HoaDonService hoaDonService;

    @GetMapping(value = "/thong_ke_doanh_thu_thong_hoa_don")
    public String doanhThuTongHoaDon(Model model) {
        List<HoaDon> hoaDons = hoaDonService.getAll();
        System.out.println("size : " + hoaDons.size());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute("hoaDons", objectMapper.writeValueAsString(hoaDons));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/thongke/thong_ke_doanh_thu_tong_hoa_don";
    }

    @GetMapping(value = "/thong_ke_trang_thai_hoa_don")
    public String trangThaiHoaDon(Model model) {
        List<HoaDon> hoaDons = hoaDonService.getAll();
        System.out.println("size : " + hoaDons.size());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute("hoaDons", objectMapper.writeValueAsString(hoaDons));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/thongke/thong_ke_trang_thai_hoa_don";
    }
}
