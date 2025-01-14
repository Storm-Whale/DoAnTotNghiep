package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.GioHang;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.entity.VaiTro;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.service.service.GioHangService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonChiTietService;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.KhachHangService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
import nhom6.duancanhan.doantotnghiep.util.FileUploadUtil;
import nhom6.duancanhan.doantotnghiep.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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

    @Autowired
    VaiTroService vaiTroService;
    @Autowired
    GioHangService gioHangService;

    @Autowired
    HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HoaDonService hoaDonService;

    @GetMapping("")
    public String searchKhachHang(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model
    ) {
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
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
        List<Integer> pageNumbers = new ArrayList<>();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        // Xác định có hiển thị dấu ... ở trước và sau không
        boolean showDotsBefore = startPage > 0;
        boolean showDotsAfter = endPage < totalPages - 1;

        // Đẩy dữ liệu ra model
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("listKH", listKH);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("showDotsBefore", showDotsBefore);
        model.addAttribute("showDotsAfter", showDotsAfter);

        return "/admin/customer/khachhang";
    }



    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("khachHang", khachHangService.detail(id));
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        return "/admin/customer/khachhang";
    }



    @GetMapping("/hoa-don/{khachHangId}")
    public String viewHoaDonByKhachHang(
            @PathVariable("khachHangId") Integer khachHangId,
            @RequestParam(value = "status", required = false, defaultValue = "all") String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model
    ) {
        // Lấy thông tin khách hàng
        KhachHang khachHang = khachHangService.findById(khachHangId);
        if (khachHang == null) {
            model.addAttribute("error", "Khách hàng không tồn tại");
            return "error-page"; // Trả về trang lỗi nếu cần
        }

        // Tạo Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách hóa đơn theo trạng thái và khách hàng
        Page<HoaDon> hoaDonPage;
        if ("all".equalsIgnoreCase(status)) {
            hoaDonPage = hoaDonRepository.findByKhachHang_Id(khachHangId, pageable);
        } else {
            hoaDonPage = hoaDonRepository.findByKhachHang_IdAndTrangThai(khachHangId, Integer.parseInt(status), pageable);
        }

        // Đẩy dữ liệu vào model
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("hoaDonPage", hoaDonPage);
        model.addAttribute("listKH", khachHangService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        return "/admin/customer/hoaDonKhachHang"; // Trả về view hiển thị hóa đơn
    }

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @GetMapping("/hoa-don/{hoaDonId}/chi-tiet")
    public String xemChiTietHoaDon(
            @PathVariable Integer hoaDonId,
            Model model
    ) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));
        List<HoaDonChiTiet> danhSachSanPham = hoaDonChiTietRepository.findByHoaDon_Id(hoaDonId);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        return "/admin/customer/chi-tiet-hoa-don";
    }

    @GetMapping("/view_add")
    public String viewadd(@ModelAttribute("khachHang") KhachHang khachHang) {
        return "/admin/customer/adKhachHang";
    }
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("khachHang") KhachHang khachHang,
            BindingResult result,
            Model model) {


        // Tạo mới tài khoản
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(khachHang.getSoDienThoai());
        taiKhoan.setMatKhau(khachHang.getSoDienThoai());
        taiKhoan.setTrangThai(1);
        VaiTro vaiTro = vaiTroService.findById(3);
        if (vaiTro != null) {
            taiKhoan.setVaiTro(vaiTro);
        } else {
            model.addAttribute("error", "Vai trò không tồn tại.");
            return "/admin/customer/adKhachHang";
        }
        taiKhoanService.addTaiKhoan(taiKhoan);
        khachHang.setTaiKhoan(taiKhoan);

        // Kiểm tra email không phải là chuỗi rỗng
        String email = khachHang.getEmail();
        if (email != null && !email.isEmpty()) {
            if (khachHangService.isEmailExist(email)) {
                result.rejectValue("email", "error.khachHang", "Email đã tồn tại.");
            }
        }
// SoDienThoai
        if (khachHangService.isSoDienThoaiExist(khachHang.getSoDienThoai())) {
            result.rejectValue("soDienThoai", "error.khachHang", "Số điện thoại đã tồn tại.");
        }
        // Nếu có lỗi, thêm tất cả các lỗi vào model để hiển thị
        if (result.hasErrors()) {
            model.addAttribute("fieldErrors", result.getFieldErrors());
            return "/admin/customer/adKhachHang";
        }

        // Xử lý tệp tải lên
        MultipartFile anhUrlFile = khachHang.getAnhUrlFile();
        if (anhUrlFile == null || anhUrlFile.isEmpty()) {
            khachHang.setAnhUrl("u.png");
        } else {
            // Kiểm tra file ảnh hợp lệ
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.khachHang", "Tệp tải lên không phải là hình ảnh.");
            } else {
                try {
                    // Tạo tên file duy nhất bằng UUID
                    String fileName = UUID.randomUUID() + "_" + anhUrlFile.getOriginalFilename();
                    // Đường dẫn thư mục lưu file (ngoài thư mục dự án)
                    String uploadDir = System.getProperty("user.dir") + "/upload/";
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists()) {
                        uploadFolder.mkdirs();
                    }

                    File uploadFile = new File(uploadDir + fileName);
                    anhUrlFile.transferTo(uploadFile);

                    khachHang.setAnhUrl(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                    return "/admin/customer/adKhachHang";
                }
            }
        }
        khachHang.setTrangThai(1);
        khachHangService.addKhachHang(khachHang);
        gioHangService.addGioHang(khachHang);
        return "redirect:/admin/khachhang";
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
            BindingResult result,
            Model model) throws Exception {
        KhachHang existingKhachHang = khachHangService.findById(khachHang.getId());
        if (existingKhachHang == null) {
            model.addAttribute("error", "Không tìm thấy khách hàng.");
            return "/admin/customer/updateKhachHang";
        }
        TaiKhoan taiKhoan = existingKhachHang.getTaiKhoan();
        if (taiKhoan == null) {
            taiKhoan = new TaiKhoan();
            taiKhoan.setTrangThai(1);
            VaiTro vaiTro = vaiTroService.findById(3);
            if (vaiTro != null) {
                taiKhoan.setVaiTro(vaiTro);
            } else {
                model.addAttribute("error", "Vai trò không tồn tại.");
                return "/admin/customer/updateKhachHang";
            }
            taiKhoanService.addTaiKhoan(taiKhoan);
        } else {
            taiKhoan.setTenDangNhap(khachHang.getSoDienThoai());
            taiKhoan.setMatKhau(khachHang.getSoDienThoai());
            taiKhoanService.updateTaiKhoan(taiKhoan.getId(), taiKhoan);
        }
        khachHang.setTaiKhoan(taiKhoan);

        // ảnh
        MultipartFile anhUrlFile = khachHang.getAnhUrlFile();
        if (anhUrlFile != null && !anhUrlFile.isEmpty()) {
            // Kiểm tra file ảnh hợp lệ
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.khachHang", "Tệp tải lên không phải là hình ảnh.");
                khachHang.setAnhUrl(existingKhachHang.getAnhUrl()); // Giữ lại ảnh cũ nếu lỗi
                model.addAttribute("fieldErrors", result.getFieldErrors());
                return "/admin/customer/updateKhachHang";
            }
            // Lưu ảnh mới
            try {
                String fileName = UUID.randomUUID() + "_" + anhUrlFile.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + "/upload/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }
                File uploadFile = new File(uploadDir + fileName);
                anhUrlFile.transferTo(uploadFile);
                khachHang.setAnhUrl(fileName); // Lưu tên ảnh mới
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                return "/admin/customer/updateKhachHang";
            }
        } else {
            khachHang.setAnhUrl(existingKhachHang.getAnhUrl());
        }

        khachHang.setTrangThai(1);
        khachHangService.updateKhachHang(khachHang);

        return "redirect:/admin/khachhang";
    }


    @GetMapping("/toggleStatus/{id}")
    public String toggleStatus(@PathVariable("id") Integer id) {
        // Lấy thông tin khách hàng theo ID
        KhachHang khachHang = khachHangService.findById(id);

        if (khachHang != null) {
            // Chuyển đổi trạng thái
            if (khachHang.getTrangThai() == 1) {
                khachHang.setTrangThai(2); // Chuyển từ 1 sang 2
            } else if (khachHang.getTrangThai() == 2) {
                khachHang.setTrangThai(1); // Chuyển từ 2 sang 1
            }

            // Lưu lại thay đổi
            khachHangService.saveKhachHang(khachHang);
        }

        return "redirect:/admin/khachhang";
    }

}
