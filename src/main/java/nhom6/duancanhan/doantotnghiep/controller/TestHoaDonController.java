package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hoadontest")
public class TestHoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    private static final int PAGE_SIZE = 5; // Số hóa đơn mỗi trang

    @GetMapping("")
    public String getAll(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                         @RequestParam(value = "tab", defaultValue = "all") String tab,
                         Model model) {
        Page<HoaDon> page;
        if ("tai-quay".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(2, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Tại quầy"
        } else if ("online".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(3, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Online"
        } else if ("prepare".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(4, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Đang Chuẩn Bị Hàng"
        } else if ("shipping".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(5, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Đang Giao Hàng"
        } else if ("delivered".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(6, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Giao Hàng Thành Công"
        } else {
            page = hoaDonService.getAllWithPagination(pageNo, PAGE_SIZE); // Lấy tất cả hóa đơn
        }
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", tab); // Duy trì trạng thái tab
        return "/admin/customer/HoaDonTest";
    }

    // Lấy hóa đơn trạng thái = 2 (Tại quầy) với phân trang
    @GetMapping("/tai-quay")
    public String getTaiQuay(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(2, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 2
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "tai-quay"); // Tab đang mở
        return "/admin/customer/HoaDonTest";
    }

    // Lấy hóa đơn trạng thái = 3 (Online) với phân trang
    @GetMapping("/online")
    public String getOnline(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(3, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 3
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "online"); // Tab đang mở
        return "/admin/customer/HoaDonTest";
    }

    // Lấy hóa đơn trạng thái = 4 (Đang Chuẩn Bị Hàng) với phân trang
    @GetMapping("/prepare")
    public String getPrepare(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(4, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 4
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "prepare"); // Tab đang mở
        return "/admin/customer/HoaDonTest";
    }

    // Lấy hóa đơn trạng thái = 5 (Đang Giao Hàng) với phân trang
    @GetMapping("/shipping")
    public String getShipping(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(5, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 5
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "shipping"); // Tab đang mở
        return "/admin/customer/HoaDonTest";
    }

    // Lấy hóa đơn trạng thái = 6 (Giao Hàng Thành Công) với phân trang
    @GetMapping("/delivered")
    public String getDelivered(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(6, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 6
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "delivered"); // Tab đang mở
        return "/admin/customer/HoaDonTest";
    }

}
