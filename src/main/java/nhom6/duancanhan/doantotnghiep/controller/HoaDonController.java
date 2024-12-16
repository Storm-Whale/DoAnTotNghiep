package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.servlet.http.HttpServletResponse;
import nhom6.duancanhan.doantotnghiep.dto.HoaDonDTO;

import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamChiTietService;
import nhom6.duancanhan.doantotnghiep.util.ChangeNumberOfDetailProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


<<<<<<< HEAD

=======
>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d
import java.io.IOException;

import java.io.IOException;

import java.io.IOException;
import java.text.DecimalFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
<<<<<<< HEAD
//<<<<<<< HEAD
//import java.util.List;
//import java.util.Map;
//=======

=======
import java.util.List;
import java.util.Map;
>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
<<<<<<< HEAD
//>>>>>>> 14b06343e86fc8a1528c10ebd171020092c9abc8
=======

>>>>>>> 25025b7a04466b9b44b88e581f850a3437257c3d


@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private ChangeNumberOfDetailProduct changeNumberOfDetailProduct;
    private static final int PAGE_SIZE = 5; // Số hóa đơn mỗi trang
    @GetMapping("")
    public String getAll(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                         @RequestParam(value = "tab", defaultValue = "all") String tab,
                         Model model) {
        Page<HoaDon> page;
        if ("tai-quay".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(1, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Tại quầy"
        } else if ("online".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(2, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Online"
        } else if ("prepare".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(3, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Đang Chuẩn Bị Hàng"
        } else if ("shipping".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(4, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Đang Giao Hàng"
        } else if ("delivered".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(5, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Giao Hàng Thành Công"
        }else if ("pass".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(6, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Giao Hàng Thành Công"
        } else if ("fall".equals(tab)) {
            page = hoaDonService.getByTrangThaiWithPagination(7, pageNo, PAGE_SIZE); // Lọc theo trạng thái "Hủy"
        }
        else {
            page = hoaDonService.getAllWithPagination(pageNo, PAGE_SIZE); // Lấy tất cả hóa đơn
        }
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", tab); // Duy trì trạng thái tab
        return "/admin/customer/hoadon";
    }

    // Lấy hóa đơn trạng thái = 2 (Tại quầy) với phân trang
    @GetMapping("/tai-quay")
    public String getTaiQuay(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(1, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 2
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "tai-quay"); // Tab đang mở
        return "/admin/customer/hoadon";
    }

    // Lấy hóa đơn trạng thái = 3 (Online) với phân trang
    @GetMapping("/online")
    public String getOnline(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(2, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 3
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "online"); // Tab đang mở
        return "/admin/customer/hoadon";
    }

    // Lấy hóa đơn trạng thái = 4 (Đang Chuẩn Bị Hàng) với phân trang
    @GetMapping("/prepare")
    public String getPrepare(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(3, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 4
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "prepare"); // Tab đang mở
        return "/admin/customer/hoadon";
    }

    // Lấy hóa đơn trạng thái = 5 (Đang Giao Hàng) với phân trang
    @GetMapping("/shipping")
    public String getShipping(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(4, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 5
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "shipping"); // Tab đang mở
        return "/admin/customer/hoadon";
    }

    // Lấy hóa đơn trạng thái = 6 (Giao Hàng Thành Công) với phân trang
    @GetMapping("/delivered")
    public String getDelivered(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(5, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 6
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "delivered"); // Tab đang mở
        return "/admin/customer/hoadon";
    }
    @GetMapping("/pass")
    public String getpass(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(6, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 6
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "pass"); // Tab đang mở
        return "/admin/customer/hoadon";
    }
    @GetMapping("/fall")
    public String getfall(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, Model model) {
        Page<HoaDon> page = hoaDonService.getByTrangThaiWithPagination(7, pageNo, PAGE_SIZE); // Phân trang hóa đơn trạng thái 6
        model.addAttribute("listHD", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("activeTab", "fall"); // Tab đang mở
        return "/admin/customer/hoadon";
    }
//    @GetMapping("")
//    public String getAll(Model model) {
//        List<HoaDon> listHD = hoaDonService.getAll(); // Lấy toàn bộ danh sách không phân trang
//        model.addAttribute("hoaDon", new HoaDon());
//        model.addAttribute("listHD", listHD); // Gửi danh sách sang View
//        return "/admin/customer/hoadon"; // Đường dẫn tới View
//    }

//
//
//
//
////    @GetMapping("/{pageNo}")
////    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
////        int pageSize = 3;
////        Page<HoaDon> page = hoaDonService.phanTrang(pageNo , pageSize);
////        List<HoaDon> listHD = page.getContent();
////        model.addAttribute("hoaDon", new HoaDon());
////        model.addAttribute("listHD", listHD);
////        model.addAttribute("currentPage", pageNo);
////        model.addAttribute("totalPages", page.getTotalPages());
////        model.addAttribute("totalItems", page.getTotalElements());
////        return "/admin/customer/hoadon";
////    }
////@GetMapping("/{pageNo}")
////public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
////    int pageSize = 3;
////
////    // Thêm Sort để sắp xếp ngày tạo mới nhất (giảm dần)
////    Sort sort = Sort.by(Sort.Order.desc("ngayTao"));
////
////    // Phân trang với Sort
////    PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
////    Page<HoaDon> page = hoaDonService.phanTrang(pageRequest);
////
////    List<HoaDon> listHD = page.getContent();
////    model.addAttribute("hoaDon", new HoaDon());
////    model.addAttribute("listHD", listHD);
////    model.addAttribute("currentPage", pageNo);
////    model.addAttribute("totalPages", page.getTotalPages());
////    model.addAttribute("totalItems", page.getTotalElements());
////
////    return "/admin/customer/hoadon";
////}
////@GetMapping("/{pageNo}")
////public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
////    int pageSize = 3;
////
//////    // Thêm Sort để sắp xếp ngày tạo mới nhất (giảm dần)
//////    Sort sort = Sort.by(Sort.Order.desc("ngayTao"));
////
//////    // Phân trang với Sort
//////    PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
//////    Page<HoaDon> page = hoaDonService.phanTrang(pageRequest);
////
////    List<HoaDon> listHD = page.getContent();
////
////    // Kiểm tra nếu số lượng dữ liệu ít hơn 3 thì thêm dữ liệu giả
////    if (listHD.size() < 3) {
////        int remainingItems = 3 - listHD.size();
////        // Thêm dữ liệu giả vào nếu cần thiết
////        for (int i = 0; i < remainingItems; i++) {
////            listHD.add(new HoaDon());  // Thêm đối tượng HoaDon trống hoặc dữ liệu mặc định
////        }
////    }
////
////    model.addAttribute("hoaDon", new HoaDon());
////    model.addAttribute("listHD", listHD);
////    model.addAttribute("currentPage", pageNo);
////    model.addAttribute("totalPages", page.getTotalPages());
////    model.addAttribute("totalItems", page.getTotalElements());
////
////    return "/admin/customer/hoadon";
////}
//
//    @GetMapping("/tab2/{pageNo}")
//    public String phanTrangTaiQuay(@PathVariable(value = "pageNo") int pageNo, Model model) {
//        int pageSize = 10;
//        Page<HoaDon> page = hoaDonService.phanTrangTaiQuay(pageNo, pageSize);
//        System.out.println("Page: " + page.getContent());  // Kiểm tra dữ liệu trong log
//        model.addAttribute("listHD2", page.getContent());
//        model.addAttribute("currentPage2", pageNo);
//        model.addAttribute("totalPages2", page.getTotalPages());
//        model.addAttribute("totalItems2", page.getTotalElements());
//        return "/admin/customer/hoadon";  // Đảm bảo đường dẫn đúng
//    }









//    @GetMapping("/detail/{id}")
//    public String showDetail(@PathVariable Integer id, Model model) {
//        // Lấy thông tin hóa đơn theo ID
//        HoaDon hoaDon = hoaDonService.findById(id);
//
//        // Lấy chi tiết sản phẩm của hóa đơn
//        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDonId(id);
//
//        // Thêm thông tin vào model
//        model.addAttribute("hoaDon", hoaDon);
//        model.addAttribute("hoaDonChiTietList", hoaDonChiTietList);
//
//        return "/admin/customer/hoadonchitiet"; // Trang hiển thị chi tiết hóa đơn
//    }
@GetMapping("/detail/{id}")
public String showDetail(@PathVariable Integer id, Model model) {
    // Lấy thông tin hóa đơn theo ID
    HoaDon hoaDon = hoaDonService.findById(id);

    // Lấy chi tiết sản phẩm của hóa đơn
    List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDonId(id);

    // Kiểm tra trạng thái của hóa đơn
    if (hoaDon != null) {
        // Thêm thông tin vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("hoaDonChiTietList", hoaDonChiTietList);

        // Kiểm tra trạng thái và chuyển hướng đến trang phù hợp
        if (hoaDon.getTrangThai() == 1 || hoaDon.getTrangThai() == 2) {
            return "/admin/customer/hoadonchitiet"; // Trang có thể cập nhật hóa đơn
        } else {
            return "/admin/customer/hoadonchitietupdate"; // Trang không thể cập nhật
        }
    } else {
        // Nếu hóa đơn không tìm thấy, có thể trả về trang lỗi
        return "error/404"; // Trang lỗi nếu không tìm thấy hóa đơn
    }
}


    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> payload) {
        try {
            HoaDon hoaDon = hoaDonService.findById(id);

            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
            }

            // Cập nhật trạng thái tùy vào giá trị hiện tại của trạng thái
            if (hoaDon.getTrangThai() == 1) {
                hoaDon.setTrangThai(2); // Trạng thái chuyển từ 'Chờ Xác Nhận' sang 'Đang Chuẩn Bị Hàng'

            }else if (hoaDon.getTrangThai() == 2) {
                hoaDon.setTrangThai(3); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            } else if (hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(4); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            } else if (hoaDon.getTrangThai() == 4) {
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
    @PutMapping("/update-reload/{id}")
    public ResponseEntity<?> updatereload(@PathVariable("id") Integer id, @RequestBody Map<String, Integer> payload) {
        try {
            HoaDon hoaDon = hoaDonService.findById(id);

            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
            }

            // Cập nhật trạng thái tùy vào giá trị hiện tại của trạng thái
            if (hoaDon.getTrangThai() == 2) {
                hoaDon.setTrangThai(1); // Trạng thái chuyển từ 'Chờ Xác Nhận' sang 'Đang Chuẩn Bị Hàng'

            }else if (hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(2); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            } else if (hoaDon.getTrangThai() == 4) {
                hoaDon.setTrangThai(3); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            } else if (hoaDon.getTrangThai() == 5) {
                hoaDon.setTrangThai(4); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            }  else if (hoaDon.getTrangThai() == 6) {
                hoaDon.setTrangThai(5); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            } else if (hoaDon.getTrangThai() == 7) {
                hoaDon.setTrangThai(6); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đã Giao'
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trạng thái không hợp lệ để cập nhật");
            }

            hoaDonService.addHoaDon(hoaDon); // Lưu lại vào cơ sở dữ liệu
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi");
        }

    }
    @PutMapping("/cancel-order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Integer id) {
        try {
            HoaDon hoaDon = hoaDonService.findById(id);

            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
            }
            if (hoaDon.getTrangThai() == 1) {
                hoaDon.setTrangThai(7); // Trạng thái chuyển từ 'Chờ Xác Nhận' sang 'Đang Chuẩn Bị Hàng'

            } else if (hoaDon.getTrangThai() == 2) {
                hoaDon.setTrangThai(7); // Trạng thái chuyển từ 'Đang Chuẩn Bị Hàng' sang 'Đang Vận Chuyển'
            }


            hoaDonService.addHoaDon(hoaDon); // Lưu lại vào cơ sở dữ liệu
            return ResponseEntity.ok("Đơn hàng đã được hủy thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi");
        }
    }
    @PutMapping("/fall-order/{id}")
    public ResponseEntity<?> fallOrder(@PathVariable("id") Integer id) {
        try {
            HoaDon hoaDon = hoaDonService.findById(id);

            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hóa đơn không hợp lệ");
            }
            if (hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(5);

            }


            hoaDonService.addHoaDon(hoaDon); // Lưu lại vào cơ sở dữ liệu
            return ResponseEntity.ok("Đơn hàng Giao Thất Bại");
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

    @GetMapping("/filter")
    public String getFilteredHoaDon(
            @RequestParam(name = "status", required = false, defaultValue = "all") String status,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model) {

        // Lấy dữ liệu từ service với phân trang
        Page<HoaDon> page = hoaDonService.findHoaDonByStatus(status, pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();

        // Thêm các thuộc tính vào model để hiển thị
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        return "/admin/customer/hoadon"; // Chỉnh sửa đường dẫn đến trang cần hiển thị
    }


    @GetMapping("/filter/loaiHoaDon")
    public String getFilteredHoaDonByLoaiHoaDon(
            @RequestParam(name = "loaiHoaDon", required = false, defaultValue = "all") String loaiHoaDon,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model) {

        // Lọc theo loại hóa đơn
        Page<HoaDon> page = hoaDonService.findHoaDonByLoaiHoaDon(loaiHoaDon, pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();

        // Thêm các thuộc tính vào model
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("loaiHoaDon", loaiHoaDon);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        return "/admin/customer/hoadon"; // Trang hiển thị hóa đơn
    }
    @GetMapping("/filter/tenPhuongThuc")
    public String getFilteredHoaDonByTenPhuongThuc(
            @RequestParam(name = "tenPhuongThuc", required = false, defaultValue = "all") String tenPhuongThuc,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model) {

        // Lọc theo phương thức thanh toán
        Page<HoaDon> page = hoaDonService.findHoaDonByTenPhuongThuc(tenPhuongThuc, pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();

        // Thêm các thuộc tính vào model
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("tenPhuongThuc", tenPhuongThuc);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        return "/admin/customer/hoadon"; // Trang hiển thị hóa đơn
    }

    @GetMapping("/search")
    public String searchHoaDon(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model) {

        // Lọc theo từ khóa (tìm kiếm theo số điện thoại hoặc tên khách hàng)
        Page<HoaDon> page = hoaDonService.searchHoaDon(keyword, pageNo, pageSize);
        List<HoaDon> listHD = page.getContent();

        // Thêm các thuộc tính vào model
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("listHD", listHD);
        model.addAttribute("keyword", keyword);  // Truyền lại giá trị từ khóa tìm kiếm vào form
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        return "/admin/customer/hoadon"; // Trang hiển thị hóa đơn
    }


//    @GetMapping("/export")
//    public void exportHoaDon(HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        String fileName = "Danh_Sach_Hoa_Don.xlsx";
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Hóa Đơn");
//
//            // Tạo header
//            Row header = sheet.createRow(0);
//            header.createCell(0).setCellValue("ID");
//            header.createCell(1).setCellValue("Tên Người Nhận");
//            header.createCell(2).setCellValue("Số Điện Thoại");
//            header.createCell(3).setCellValue("Địa Chỉ");
//            header.createCell(4).setCellValue("Phương Thức Thanh Toán");
//            header.createCell(5).setCellValue("Tổng Tiền");
//            header.createCell(6).setCellValue("Ghi Chú");
//            header.createCell(7).setCellValue("Loại Hóa Đơn");
//            header.createCell(8).setCellValue("Trạng Thái");
//
//            // Thêm dữ liệu
//            List<HoaDon> listHoaDon = hoaDonService.getAll(); // Lấy danh sách hóa đơn
//            int rowIndex = 1;
//            for (HoaDon hoaDon : listHoaDon) {
//                Row row = sheet.createRow(rowIndex++);
//                row.createCell(0).setCellValue(hoaDon.getId());
//                row.createCell(1).setCellValue(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ");
//                row.createCell(2).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getSoDienThoai() : "");
//                row.createCell(3).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getDiaChiChiTiet() : "");
//                row.createCell(4).setCellValue(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A");
//                row.createCell(5).setCellValue(hoaDon.getTongTien() != null ? hoaDon.getTongTien().doubleValue() : 0.0);
//
//                row.createCell(6).setCellValue(hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "");
//                row.createCell(7).setCellValue(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon() : "");
//                row.createCell(8).setCellValue(getTrangThaiText(hoaDon.getTrangThai())); // Hàm chuyển trạng thái thành text
//            }
//
//            // Ghi dữ liệu ra output stream
//            workbook.write(response.getOutputStream());
//        }
//    }
@GetMapping("/export")
public void exportHoaDon(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "trangThai", defaultValue = "0") int trangThai,
                         HttpServletResponse response) throws IOException {

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    String fileName = "Danh_Sach_Hoa_Don.xlsx";
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Hóa Đơn");

        // Tạo header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Tên Người Nhận");
        header.createCell(2).setCellValue("Số Điện Thoại");
        header.createCell(3).setCellValue("Địa Chỉ");
        header.createCell(4).setCellValue("Phương Thức Thanh Toán");
        header.createCell(5).setCellValue("Tổng Tiền");
        header.createCell(6).setCellValue("Ghi Chú");
        header.createCell(7).setCellValue("Loại Hóa Đơn");
        header.createCell(8).setCellValue("Trạng Thái");

        // Lấy dữ liệu phân trang từ service
        Page<HoaDon> hoaDonPage;
        if (trangThai == 0) {
            hoaDonPage = hoaDonService.getAllWithPagination(page, size);  // Lấy toàn bộ hóa đơn nếu trạng thái là 0
        } else {
            hoaDonPage = hoaDonService.getByTrangThaiWithPagination(trangThai, page, size); // Lấy hóa đơn theo trạng thái
        }

        // Thêm dữ liệu vào bảng
        int rowIndex = 1;
        for (HoaDon hoaDon : hoaDonPage.getContent()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(hoaDon.getId());
            row.createCell(1).setCellValue(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ");
            row.createCell(2).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getSoDienThoai() : "");
            row.createCell(3).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getDiaChiChiTiet() : "");
            row.createCell(4).setCellValue(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A");
            row.createCell(5).setCellValue(hoaDon.getTongTien() != null ? hoaDon.getTongTien().doubleValue() : 0.0);
            row.createCell(6).setCellValue(hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "");
            row.createCell(7).setCellValue(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon() : "");
            row.createCell(8).setCellValue(getTrangThaiText(hoaDon.getTrangThai())); // Hàm chuyển trạng thái thành text
        }

        // Ghi dữ liệu ra output stream
        workbook.write(response.getOutputStream());
    }
}

    private String getTrangThaiText(Integer trangThai) {
        switch (trangThai) {
            case 1: return "Chờ Xác Nhận";
            case 2: return "Xác Nhận Đơn Hàng";
            case 3: return "Giao Hàng";
            case 4: return "Giao Hàng Thành Công";
            case 5: return "Giao Hàng Thất Bại";
            case 6: return "Hoàn Thành Đơn Hàng";
            case 7: return "Hủy";
            default: return "Không Xác Định";
        }
    }



    @GetMapping("/exportID/{id}")
    public void exportHoaDonById(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        // Set type của file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "YAGI_SHOP_HD_" + id + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Tạo workbook và sheet trong file Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hóa Đơn");

            // Tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Tạo style cho dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setWrapText(true);

            // Tạo header
            String[] headers = {
                    "ID", "Tên Người Nhận", "Số Điện Thoại", "Địa Chỉ",
                    "Phương Thức Thanh Toán", "Tổng Tiền", "Ghi Chú", "Loại Hóa Đơn", "Trạng Thái"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Lấy hóa đơn theo ID
            HoaDon hoaDon = hoaDonService.findById(id);
            if (hoaDon != null) {
                Row dataRow = sheet.createRow(1);
                dataRow.createCell(0).setCellValue(hoaDon.getId());
                dataRow.createCell(1).setCellValue(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ");
                dataRow.createCell(2).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getSoDienThoai() : "");
                dataRow.createCell(3).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getDiaChiChiTiet() : "");
                dataRow.createCell(4).setCellValue(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A");
                dataRow.createCell(5).setCellValue(hoaDon.getTongTien() != null ? hoaDon.getTongTien().doubleValue() : 0.0);
                dataRow.createCell(6).setCellValue(hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "");
                dataRow.createCell(7).setCellValue(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon() : "");
                dataRow.createCell(8).setCellValue(getTrangThaiText(hoaDon.getTrangThai()));

                // Áp dụng style cho dữ liệu
                for (int i = 0; i < headers.length; i++) {
                    dataRow.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Tự động điều chỉnh độ rộng cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi dữ liệu ra output stream (xuất file)
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping(value = "/delete_client/{idHD}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteClient(@PathVariable("idHD") Integer idHD, Model model) {
        HoaDon hoaDon = hoaDonService.findById(idHD);
        int trang_thai = hoaDon.getTrangThai();
        if (trang_thai == 1 || trang_thai == 2) {
            hoaDon.getHoaDonChiTietList().forEach(hoaDonChiTiet -> {
                changeNumberOfDetailProduct.updateProductDetailQuantity(hoaDonChiTiet.getSanPhamChiTiet().getId(), hoaDonChiTiet.getSoLuong(), "+");
            });
            hoaDon.setTrangThai(6);
            hoaDonService.updateHoaDon(idHD, hoaDon);
            return ResponseEntity.ok("Hoá đơn đã được huỷ thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hoá đơn đang được giao, không thể huỷ");
        }
    }
    @GetMapping("/exportHoaDonChiTiet/{id}")
    public void exportHoaDonChiTietGop(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        // Set type của file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "YAGI_SHOP_HD_" + id + "_ChiTiet.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Tạo workbook và sheet trong file Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hóa Đơn Chi Tiết");

            // Style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Style cho dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setWrapText(true);

            // Lấy thông tin hóa đơn và chi tiết
            HoaDon hoaDon = hoaDonService.findById(id);
            List<HoaDonChiTiet> chiTietList = hoaDonChiTietService.findByHoaDonId(id);

            // === 1. Thêm thông tin hóa đơn ===
            String[] hoaDonHeaders = {
                    "ID", "Tên Người Nhận", "Số Điện Thoại", "Địa Chỉ",
                    "Phương Thức Thanh Toán", "Tổng Tiền", "Ghi Chú", "Loại Hóa Đơn", "Trạng Thái"
            };
            Row hoaDonHeaderRow = sheet.createRow(0);
            for (int i = 0; i < hoaDonHeaders.length; i++) {
                Cell cell = hoaDonHeaderRow.createCell(i);
                cell.setCellValue(hoaDonHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            // Thêm dữ liệu thông tin hóa đơn
            if (hoaDon != null) {
                Row hoaDonDataRow = sheet.createRow(1);
                hoaDonDataRow.createCell(0).setCellValue(hoaDon.getId());
                hoaDonDataRow.createCell(1).setCellValue(hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ");
                hoaDonDataRow.createCell(2).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getSoDienThoai() : "");
                hoaDonDataRow.createCell(3).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi().getDiaChiChiTiet() : "");
                hoaDonDataRow.createCell(4).setCellValue(hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan().getTenPhuongThuc() : "N/A");
                hoaDonDataRow.createCell(5).setCellValue(hoaDon.getTongTien() != null ? hoaDon.getTongTien().doubleValue() : 0.0);
                hoaDonDataRow.createCell(6).setCellValue(hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "");
                hoaDonDataRow.createCell(7).setCellValue(hoaDon.getLoaiHoaDon() != null ? hoaDon.getLoaiHoaDon() : "");
                hoaDonDataRow.createCell(8).setCellValue(getTrangThaiText(hoaDon.getTrangThai()));

                // Áp dụng style cho dữ liệu
                for (int i = 0; i < hoaDonHeaders.length; i++) {
                    hoaDonDataRow.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Thêm một dòng trống để phân cách
            int currentRowIndex = 3;

            // === 2. Thêm header cho danh sách sản phẩm ===
            String[] chiTietHeaders = {"ID Sản Phẩm", "Tên Sản Phẩm", "Màu Sắc", "Size", "Số Lượng", "Giá", "Tổng Tiền"};
            Row chiTietHeaderRow = sheet.createRow(currentRowIndex++);
            for (int i = 0; i < chiTietHeaders.length; i++) {
                Cell cell = chiTietHeaderRow.createCell(i);
                cell.setCellValue(chiTietHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            // === 3. Thêm dữ liệu danh sách sản phẩm ===
            for (HoaDonChiTiet chiTiet : chiTietList) {
                Row dataRow = sheet.createRow(currentRowIndex++);
                dataRow.createCell(0).setCellValue(chiTiet.getSanPhamChiTiet().getId());
                dataRow.createCell(1).setCellValue(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                dataRow.createCell(2).setCellValue(chiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac());
                dataRow.createCell(3).setCellValue(chiTiet.getSanPhamChiTiet().getKichCo().getTenKichCo());
                dataRow.createCell(4).setCellValue(chiTiet.getSoLuong());
                dataRow.createCell(5).setCellValue(chiTiet.getSanPhamChiTiet().getGia().doubleValue());
                dataRow.createCell(6).setCellValue(chiTiet.getSoLuong() * chiTiet.getSanPhamChiTiet().getGia().doubleValue());

                // Áp dụng style
                for (int i = 0; i < chiTietHeaders.length; i++) {
                    dataRow.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Tự động điều chỉnh độ rộng cột
            int maxColumns = Math.max(hoaDonHeaders.length, chiTietHeaders.length);
            for (int i = 0; i < maxColumns; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi dữ liệu ra output stream (xuất file)
            workbook.write(response.getOutputStream());
        }
    }

    @PostMapping("/updateHoaDon/{id}")
    public String updateHoaDon(
            @PathVariable("id") Integer id,
            @RequestParam("ghiChu") String ghiChu,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("tenKhachHang") String tenKhachHang,
            RedirectAttributes redirectAttributes) {

        // Log các giá trị nhận được
        System.out.println("ID: " + id);
        System.out.println("Ghi Chú: " + ghiChu);
        System.out.println("Số Điện Thoại: " + soDienThoai);
        System.out.println("Địa Chỉ: " + diaChi);
        System.out.println("Tên Khách Hàng: " + tenKhachHang);

        // Tìm hóa đơn
        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại!");
        }

        // Cập nhật thông tin
        hoaDon.setGhiChu(ghiChu);
//        hoaDon.setTenNguoiNhan(tenKhachHang);
        if (hoaDon.getKhachHang() == null) {
            hoaDon.setKhachHang(new KhachHang());  // Tạo mới đối tượng Khách Hàng nếu chưa có
        }
        hoaDon.getKhachHang().setTen(tenKhachHang);
        if (hoaDon.getDiaChi() == null) {
            hoaDon.setDiaChi(new DiaChi());
        }
        hoaDon.getDiaChi().setSoDienThoai(soDienThoai);
        hoaDon.getDiaChi().setDiaChiChiTiet(diaChi);

        // Lưu hóa đơn
        hoaDonService.updateHoaDon(id, hoaDon);

        // Thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hóa đơn thành công!");

        return "redirect:/admin/hoadon/detail/" + id;
    }

//    @PostMapping("/updatéHD/{id}")
//    public ResponseEntity<Map<String, Object>> updateSanPhamHDCT(
//            @PathVariable("id") Integer id,
//            @RequestParam("delta") int delta) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            // Tìm sản phẩm trong hóa đơn chi tiết
//            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietService.findById(id);
//
//
//            // Gọi phương thức cập nhật số lượng
//            updateSoLuong(hoaDonChiTiet, delta, response);
//
//            // Nếu response có lỗi, trả về ngay
//            if (response.containsKey("error")) {
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            // Tính tổng tiền và chuẩn bị dữ liệu trả về
//            Integer idHoaDon = hoaDonChiTiet.getHoaDon().getId();
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            response.put("error", "Có lỗi xảy ra: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    @PostMapping("/update-hoadon-hdct/{id}")
    public ResponseEntity<Map<String, Object>> updateSanPhamHDCT(
            @PathVariable("id") Integer id, // Lấy id của chi tiết hóa đơn từ URL
            @RequestParam("delta") int delta) { // Lấy giá trị delta (thay đổi số lượng) từ tham số yêu cầu
        Map<String, Object> response = new HashMap<>(); // Khởi tạo một Map để chứa phản hồi trả về

        try {
            // Tìm sản phẩm trong hóa đơn chi tiết theo id
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm trong hóa đơn chi tiết"));

            // Tính số lượng mới của sản phẩm trong hóa đơn chi tiết
            int newQuantity = hoaDonChiTiet.getSoLuong() + delta;

            // Kiểm tra nếu số lượng mới nhỏ hơn 1 thì trả về lỗi
            if (newQuantity < 1) {
                response.put("error", "Số lượng không thể nhỏ hơn 1.");
                return ResponseEntity.badRequest().body(response); // Trả về lỗi nếu số lượng không hợp lệ
            }

            // Lấy thông tin sản phẩm chi tiết liên quan
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

            // Kiểm tra nếu delta là số âm (giảm số lượng) thì kiểm tra tồn kho
            if (delta < 0 && (sanPhamChiTiet.getSoLuong() + delta < 0)) {
                response.put("error", "Không đủ số lượng trong kho. Chỉ còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm.");
                return ResponseEntity.badRequest().body(response); // Trả về lỗi nếu không đủ số lượng trong kho
            }

            // Cập nhật số lượng của sản phẩm trong hóa đơn chi tiết
            hoaDonChiTiet.setSoLuong(newQuantity);

            // Cập nhật lại số lượng tồn kho của sản phẩm
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - delta);

            // Lưu lại các thay đổi trong cơ sở dữ liệu
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Lấy ID của hóa đơn để tính tổng tiền
            Integer idHoaDon = hoaDonChiTiet.getHoaDon().getId();

            // Lấy danh sách chi tiết hóa đơn của hóa đơn hiện tại
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByHoaDonId(idHoaDon);

            // Tính tổng tiền của hóa đơn bằng cách cộng dồn giá trị của từng sản phẩm trong chi tiết hóa đơn


            // Chuẩn bị danh sách chi tiết hóa đơn để trả về trong response
            List<Map<String, Object>> listHDCT = hoaDonChiTietList.stream()
                    .map(hdct -> {
                        Map<String, Object> hdctMap = new HashMap<>();
                        hdctMap.put("id", hdct.getId()); // Lưu id của chi tiết hóa đơn
                        hdctMap.put("soLuong", hdct.getSoLuong()); // Lưu số lượng vào map với khóa "soLuong"
                        return hdctMap;
                    })
                    .collect(Collectors.toList());


            // Thêm thông tin số lượng tồn kho vào phản hồi
            response.put("stockQuantity", sanPhamChiTiet.getSoLuong());

            // Thêm danh sách chi tiết hóa đơn và tổng tiền vào phản hồi
            response.put("listHDCT", listHDCT);

            // Trả về phản hồi với mã trạng thái OK
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, trả về phản hồi lỗi với thông báo lỗi
            response.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }


}
