package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.util.ChangeNumberOfDetailProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private ChangeNumberOfDetailProduct changeNumberOfDetailProduct;

    @GetMapping("")
    public String getAll(Model model) {
        return phanTrang(1, model);
    }

    @GetMapping("/{pageNo}")
    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 3;
        Page<HoaDon> page = hoaDonService.phanTrang(pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/customer/hoadon";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Integer id, Model model) {
        // Lấy thông tin hóa đơn theo ID
        HoaDon hoaDon = hoaDonService.findById(id);

        // Lấy chi tiết sản phẩm của hóa đơn
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDonId(id);

        // Thêm thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("hoaDonChiTietList", hoaDonChiTietList);

        return "/admin/customer/hoadonchitiet"; // Trang hiển thị chi tiết hóa đơn
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("hoaDon") HoaDon hoaDon, Model model) {
        hoaDonService.addHoaDon(hoaDon);
        return "redirect:/admin/hoadon";
    }


    @PutMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("hoaDon") HoaDon hoaDon) {
        hoaDonService.updateHoaDon(id, hoaDon);
        return "redirect:/admin/hoadon";
    }

    // Xóa hóa đơn
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, @RequestParam("_method") String method) {
        if ("delete".equals(method)) {
            hoaDonService.deleteHoaDon(id);
        }
        return "redirect:/admin/hoadon";
    }
    @GetMapping("/search")
    public String timKiem(@RequestParam("keyword") String keyword, Model model) {
        int pageNo = 1;
        int pageSize = 7;
        Page<HoaDon> page = hoaDonService.timKiem(keyword, pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "/admin/customer/hoadon";
    }

    @GetMapping(value = "/delete_client/{idHD}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@PathVariable("idHD") Integer idHD, Model model) {
        HoaDon hoaDon = hoaDonService.findById(idHD);
        hoaDon.getHoaDonChiTietList().forEach(hoaDonChiTiet -> {
            changeNumberOfDetailProduct.updateProductDetailQuantity(hoaDonChiTiet.getSanPhamChiTiet().getId(), hoaDonChiTiet.getSoLuong(), "+");
        });
        hoaDon.setTrangThai(6);
        hoaDonService.updateHoaDon(idHD, hoaDon);
    }
}
