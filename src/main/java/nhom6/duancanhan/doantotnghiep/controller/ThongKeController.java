package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.HoaDonChiTietResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.mapper.HoaDonChiTietMapper;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin/thong-ke")
public class ThongKeController {

    private final HoaDonService hoaDonService;
    private final SanPhamService sanPhamService;
    private final KhachHangService khachHangService;
    private final HoaDonChiTietMapper hoaDonChiTietMapper;
    private final SanPhamChiTietService sanPhamChiTietService;

    @GetMapping(value = "/thong_ke_doanh_thu_thong_hoa_don")
    public String doanhThuTongHoaDon(Model model) {
        List<HoaDon> hoaDons = hoaDonService.getAll();
        chuyenDoiSangJson(model, "hoaDons", hoaDons);
        return "/admin/thongke/thong_ke_doanh_thu_tong_hoa_don";
    }

    @GetMapping(value = "/thong_ke_trang_thai_hoa_don")
    public String trangThaiHoaDon(Model model) {
        List<HoaDon> hoaDons = hoaDonService.getAll();
        chuyenDoiSangJson(model, "hoaDons", hoaDons);
        return "/admin/thongke/thong_ke_trang_thai_hoa_don";
    }

    @GetMapping(value = "/thong_ke_khach_hang")
    public String khachHangMoi(Model model) {
        List<KhachHang> khachHangs = khachHangService.getAll();
        chuyenDoiSangJson(model, "khachHangs", khachHangs);
        return "/admin/thongke/thong_ke_khach_hang";
    }

    @GetMapping(value = "/thong_ke_san_pham")
    public String sanPham(Model model) {
        List<HoaDon> hoaDons = hoaDonService.getAll();
        List<HoaDonChiTietResponse> hoaDonChiTietResponses = new ArrayList<>();
        List<SanPhamResponse> sanPhamResponses = sanPhamService.getAllSanPham();
        List<SanPhamChiTietResponse> sanPhamChiTietResponses = sanPhamChiTietService.getAllSanPhamChiTiet();

        hoaDons.forEach(hoaDon -> hoaDon.getHoaDonChiTietList().forEach(hoaDonChiTiet ->
                hoaDonChiTietResponses.add(hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTiet))));

        chuyenDoiSangJson(model, "hoaDons", hoaDons);
        chuyenDoiSangJson(model, "hoaDonChiTiets", hoaDonChiTietResponses);
        chuyenDoiSangJson(model, "sanPhams", sanPhamResponses);
        chuyenDoiSangJson(model, "sanPhamChiTiets", sanPhamChiTietResponses);

        model.addAttribute("sanPhams_S", sanPhamResponses);
        return "/admin/thongke/thong_ke_san_pham";
    }

    private void chuyenDoiSangJson(Model model, String ten, Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute(ten, objectMapper.writeValueAsString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
