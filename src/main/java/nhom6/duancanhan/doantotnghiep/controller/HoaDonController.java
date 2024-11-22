package nhom6.duancanhan.doantotnghiep.controller;

import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepo;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

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

    // Hiển thị chi tiết hóa đơn
//    @GetMapping("/detail/{id}")
//    public String showDetail(@PathVariable("id") Integer id, Model model) {
//        Optional<HoaDon> hoaDon = hoaDonService.detail(id);
//        if (hoaDon.isPresent()) {
//            model.addAttribute("hoaDon", hoaDon.get());
//            return "/admin/hoadon/HoaDon/Detail";
//        }
//        return "redirect:/admin/hoadon";
//    }


//    @PutMapping("/update-status/{id}")
//    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> payload) {
//        try {
//            HoaDon hoaDon = hoaDonService.findById(id);
//            if (hoaDon == null || hoaDon.getTrangThai() != 1) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
//            }
//
//            hoaDon.setTrangThai(3); // Chuyển trạng thái sang "Đang Chuẩn Bị Hàng"
//            hoaDonService.addHoaDon(hoaDon);
//            return ResponseEntity.ok("Cập nhật trạng thái thành công");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi");
//        }
//    }


    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> payload) {
        try {
            HoaDon hoaDon = hoaDonService.findById(id);

            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
            }

            // Cập nhật trạng thái tùy vào giá trị hiện tại của trạng thái
            if (hoaDon.getTrangThai() == 1) {
                hoaDon.setTrangThai(3); // Trạng thái chuyển từ 'Chờ Xác Nhận' sang 'Đang Chuẩn Bị Hàng'

            } else if (hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(4); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            } else if (hoaDon.getTrangThai() == 4) {
                hoaDon.setTrangThai(5); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            }else if (hoaDon.getTrangThai() == 1) {
                hoaDon.setTrangThai(6); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            }else if (hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(6); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trạng thái không hợp lệ để cập nhật");
            }

            hoaDonService.addHoaDon(hoaDon); // Lưu lại vào cơ sở dữ liệu
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi");
        }

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
//    @GetMapping("/search")
//    public String timKiem(@RequestParam("keyword") String keyword, Model model) {
//        int pageNo = 1;
//        int pageSize = 7;
//        Page<HoaDon> page = hoaDonService.timKiem(keyword, pageNo, pageSize);
//        List<HoaDon> listHD = page.getContent();
//        model.addAttribute("hoaDon", new HoaDon());
//        model.addAttribute("listHD", listHD);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        return "/admin/customer/hoadon";
//    }
@GetMapping("/search")
public String timKiem(
        @RequestParam("keyword") String keyword,
        @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "7") int pageSize,
        Model model) {

    // Tìm kiếm với các tham số đã cung cấp
    Page<HoaDon> page = hoaDonService.timKiem(keyword, pageNo, pageSize);
    List<HoaDon> listHD = page.getContent();

    // Thêm các đối tượng cần thiết vào model để hiển thị
    model.addAttribute("hoaDon", new HoaDon());
    model.addAttribute("listHD", listHD);
    model.addAttribute("keyword", keyword);
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());

    // Trả về trang cần hiển thị
    return "/admin/customer/hoadon";
}





}
