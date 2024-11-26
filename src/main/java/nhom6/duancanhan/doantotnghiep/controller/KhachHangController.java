package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.util.FileUploadUtil;
import nhom6.duancanhan.doantotnghiep.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private UploadImage uploadImage;

    @GetMapping("")
    public String searchKhachHang(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model
    ) {
        // Xử lý keyword và loại bỏ khoảng trắng
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        // Reset trang nếu là tìm kiếm mới
        if (keyword != null || trangThai != null) {
            page = 0; // Đặt về trang đầu tiên
        }

        // Kiểm tra page và size hợp lệ
        if (page < 0) page = 0;
        if (size < 1) size = 5;

        // Truy vấn danh sách khách hàng
        Page<KhachHang> listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listKH.getTotalPages();

        // Xử lý khi vượt quá tổng số trang
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0;
            listKH = khachHangService.SearchandPhantrang(keyword, trangThai, page, size);
        }

        // Tạo danh sách số trang
        List<Integer> pageNumbers = totalPages > 0
                ? IntStream.range(0, totalPages).boxed().collect(Collectors.toList())
                : Collections.emptyList();

        // Đẩy dữ liệu ra model
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("listKH", listKH);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageNumbers", pageNumbers);

        return "/admin/customer/khachhang";
    }



//    @GetMapping("{pageNo}")
//    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
//        int pageSize = 3;
//        Page<KhachHang> page = khachHangService.phanTrang(pageNo,pageSize);
//        List<KhachHang> listKH = page.getContent();
//        model.addAttribute("khachHang",new KhachHang());
//        model.addAttribute("listKH",listKH);
//        model.addAttribute("listTK",taiKhoanService.getAll());
//        model.addAttribute("currentPage ", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems",page.getTotalElements());
//        return "/admin/customer/khachhang";
//    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/khachhang";
    }
    @Autowired
    HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonService hoaDonService;
    @GetMapping("/hoa-don/{khachHangId}")
    public String viewHoaDonByKhachHang(
            @PathVariable("khachHangId") Integer khachHangId,
            Model model
    ) {
        // Lấy thông tin khách hàng
        KhachHang khachHang = khachHangService.findById(khachHangId);
        if (khachHang == null) {
            model.addAttribute("error", "Khách hàng không tồn tại");
            return "error-page"; // Trả về trang lỗi nếu cần
        }

        // Lấy danh sách hóa đơn của khách hàng
//        List<HoaDonChiTiet> hoaDonChiTietListList = hoaDonChiTietService.findByKhachHangId(khachHangId);
        List<HoaDon> danhSachHoaDon  = hoaDonRepository.findByKhachHang_IdAndTrangThai(khachHangId,2);

        // Đẩy dữ liệu vào model
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);

        return "/admin/customer/hoadonKhachHang"; // Trả về view hiển thị hóa đơn
    }

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;
    @GetMapping("/hoa-don/{hoaDonId}/chi-tiet")
    public String xemChiTietHoaDon(
            @PathVariable Integer hoaDonId,
            Model model
    ) {
        // Tìm thông tin hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        // Lấy danh sách sản phẩm trong hóa đơn
        List<HoaDonChiTiet> danhSachSanPham = hoaDonChiTietRepository.findByHoaDon_Id(hoaDonId);

        // Gán dữ liệu vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        return "/admin/customer/chi-tiet-hoa-don"; // Tên view hiển thị
    }

    @GetMapping("/view_add")
    public String viewadd(@ModelAttribute("khachHang") KhachHang khachHang) {
//        model.addAttribute("khachHang",khachHangService.getAll());
//        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/adKhachHang";
    }
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("khachHang") KhachHang khachHang,
            @RequestParam(value = "anhUrlFile", required = false) MultipartFile anhUrlFile,
            BindingResult result,
            Model model) {

        // Kiểm tra lỗi xác thực
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/customer/adKhachHang";
        }
        if (anhUrlFile == null || anhUrlFile.isEmpty()) {
            result.rejectValue("anhUrl", "error.khachHang", "Vui lòng chọn tệp ảnh."); // Thêm thông báo lỗi cho trường ảnh
        } else {
            // Kiểm tra định dạng tệp (nếu cần)
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.khachHang", "Tệp tải lên không phải là hình ảnh."); // Thêm thông báo lỗi cho trường ảnh
            }
        }
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/customer/adKhachHang";
        }
        // Xử lý tệp tải lên

            try {
                // Tạo tên file duy nhất bằng UUID
                String fileName = UUID.randomUUID() + "_" + anhUrlFile.getOriginalFilename();

                // Đường dẫn thư mục lưu file (ngoài thư mục dự án)
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                // Tạo file trên server
                File uploadFile = new File(uploadDir + fileName);
                anhUrlFile.transferTo(uploadFile); // Lưu file

                // Gán tên file vào entity
                khachHang.setAnhUrl(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                return "/admin/customer/adKhachHang";
            }

        // Lưu khách hàng vào cơ sở dữ liệu
        khachHangService.addKhachHang(khachHang);

        return "redirect:/admin/khachhang"; // Chuyển hướng về danh sách khách hàng
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/updateKhachHang";
    }


    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("khachHang") KhachHang khachHang,
            @RequestParam(value = "anhUrlFile", required = false) MultipartFile anhUrlFile,
            BindingResult result,
            Model model) throws Exception{

        // Kiểm tra lỗi xác thực
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/customer/updateKhachHang";
        }

        // Lấy thông tin khách hàng từ DB để giữ ảnh cũ nếu không có ảnh mới
        KhachHang existingKhachHang = khachHangService.findById(khachHang.getId());
        if (existingKhachHang == null) {
            model.addAttribute("error", "Không tìm thấy khách hàng.");
            return "/admin/customer/updateKhachHang";
        }

        uploadImage.deleteOldImage(existingKhachHang.getAnhUrl());
        // Xử lý tệp tải lên (nếu có)
        if (anhUrlFile != null && !anhUrlFile.isEmpty()) {
            try {
               String fileName = uploadImage.saveImage(anhUrlFile);
                // Gán tên tệp (hoặc đường dẫn) vào thuộc tính `anhUrl`
                khachHang.setAnhUrl(fileName);
            } catch (IOException e) {
                model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                return "/admin/customer/updateKhachHang";
            }
        } else {
            // Giữ lại ảnh cũ
            khachHang.setAnhUrl(existingKhachHang.getAnhUrl());
        }
        // Lưu thông tin khách hàng vào database
        khachHangService.updateKhachHang(khachHang);
        return "redirect:/admin/khachhang";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        khachHangService.deleteKhachHang(id);
        return "redirect:/admin/khachhang";
    }

}
