package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.entity.TaiKhoan;
import nhom6.duancanhan.doantotnghiep.entity.VaiTro;
import nhom6.duancanhan.doantotnghiep.repository.HoaDonRepository;
import nhom6.duancanhan.doantotnghiep.repository.TaiKhoanRepository;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/nhanvien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private VaiTroService vaiTroService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private UploadImage uploadImage;

    @GetMapping("")
    public String searchNhanVien(
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

        // Kiểm tra page và size hợp lệ
        if (page < 0) page = 0;
        if (size < 1) size = 5;

        // Truy vấn danh sách khách hàng
        Page<NhanVien> listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listNV.getTotalPages();

        // Xử lý khi vượt quá tổng số trang
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0;
            listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size);
        }

        // Tạo danh sách số trang
        List<Integer> pageNumbers = new ArrayList<>();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        // Đẩy dữ liệu ra model
        model.addAttribute("nhanVien", new NhanVien());
        model.addAttribute("listNV", listNV);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("pageNumbers", pageNumbers);

        return "/admin/nhanvien/nhanvien";
    }

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("/hoa-don/{nhanVienId}")
    public String getHoaDonsByNhanVien(
            @PathVariable("nhanVienId") Integer nhanVienId,
            @RequestParam(value = "status", required = false, defaultValue = "all") String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {
        NhanVien nhanVien = nhanVienService.findById(nhanVienId);
        if (nhanVien == null) {
            model.addAttribute("error", "Khách hàng không tồn tại");
            return "error-page"; // Trả về trang lỗi nếu cần
        }


        // Lấy danh sách hóa đơn theo trạng thái và nhân viên
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách hóa đơn theo trạng thái và khách hàng
        Page<HoaDon> hoaDonPage;
        if ("all".equalsIgnoreCase(status)) {
            hoaDonPage = hoaDonRepository.findByKhachHang_Id(nhanVienId, pageable);
        } else {
            hoaDonPage = hoaDonRepository.findByKhachHang_IdAndTrangThai(nhanVienId, Integer.parseInt(status), pageable);
        }

        // Kiểm tra nếu không có dữ liệu
        // Thêm dữ liệu vào model
        model.addAttribute("nhanVien", nhanVienService.detailNhanVien(nhanVienId));
        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("hoaDonPage", hoaDonPage);
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("listNV", nhanVienService.getAll());
        model.addAttribute("listVT", vaiTroService.getAll());
        model.addAttribute("status", status); // Trạng thái hiện tại để hiển thị đúng trong select
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        return "/admin/nhanvien/nhan-vien-hoa-don"; // Tên file Thymeleaf để hiển thị danh sách hóa đơn
    }


    @GetMapping("/{id}")
    public String xemChiTietHoaDon(@PathVariable("id") Integer idHoaDon, Model model) {
        HoaDon hoaDon = hoaDonService.findById(idHoaDon);
        model.addAttribute("hoaDon", hoaDon);
        return "/admin/nhanvien/nhan-vien-chitietsp"; // Chỉ đến file Thymeleaf
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.detailNhanVien(id));
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("listNV", nhanVienService.getAll());
        model.addAttribute("listVT", vaiTroService.getAll());
        return "/admin/nhanvien/nhanvien";
    }

    @GetMapping("/view_add")
    public String viewadd(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        return "/admin/nhanvien/adNhanVien";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien,
//                      @RequestParam(value = "anhUrlFile", required = false) MultipartFile anhUrlFile,
                      BindingResult result, Model model) {


        // Tạo mới tài khoản
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(nhanVien.getSdt());
        taiKhoan.setMatKhau("123");
        taiKhoan.setTrangThai(1);
        VaiTro vaiTro = vaiTroService.findById(2);
        if (vaiTro != null) {
            taiKhoan.setVaiTro(vaiTro);
        } else {
            model.addAttribute("error", "Vai trò không tồn tại.");
            return "/admin/customer/adKhachHang";
        }
        taiKhoanService.addTaiKhoan(taiKhoan);
        nhanVien.setTaiKhoan(taiKhoan);

        if (nhanVien.getTen() != null && !nhanVien.getTen().matches("^[a-zA-Z ]+$")) {
            result.rejectValue("ten", "error.nhanVien", "Tên NhanVien chỉ được chứa chữ cái và khoảng trắng.");
        }


        // Kiểm tra email không phải là chuỗi rỗng
        String email = nhanVien.getEmail();
        if (email != null && !email.isEmpty()) {
            // Kiểm tra email bắt đầu bằng chữ thường
            if (!Character.isLowerCase(email.charAt(0))) {
                result.rejectValue("email", "error.khachHang", "Email phải bắt đầu bằng chữ thường.");
            }
            if (nhanVienService.isEmailExist(email)) {
                result.rejectValue("email", "error.khachHang", "Email đã tồn tại.");
            }
        }


        if (nhanVien.getNgaySinh() != null) {
            LocalDate ngaySinh = nhanVien.getNgaySinh();
            LocalDate today = LocalDate.now();
            int age = Period.between(ngaySinh, today).getYears();

            if (age < 0 || age > 150) {
                result.rejectValue("ngaySinh", "error.nhanVien", "Tuổi phải trong khoảng từ 0 đến 150.");
            }
        }

        if (nhanVienService.isSdtExist(nhanVien.getSdt())) {
            result.rejectValue("sdt", "error.nhanVien", "Số điện thoại đã tồn tại.");
        }

        // Nếu có lỗi, thêm tất cả các lỗi vào model để hiển thị
        if (result.hasErrors()) {
            model.addAttribute("fieldErrors", result.getFieldErrors());
            return "/admin/nhanvien/adNhanVien";
        }


        // Xử lý tệp tải lên

        MultipartFile anhUrlFile = nhanVien.getAnhUrlFile();
        if (anhUrlFile == null || anhUrlFile.isEmpty()) {
            // Nếu không tải ảnh lên, gán ảnh mặc định
            nhanVien.setAnhUrl("u.png");
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
                        uploadFolder.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                    }

                    // Tạo file trên server
                    File uploadFile = new File(uploadDir + fileName);
                    anhUrlFile.transferTo(uploadFile); // Lưu file

                    // Gán tên file vào entity
                    nhanVien.setAnhUrl(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                    return "/admin/nhanvien/adNhanVien";
                }
            }
        }

//        model.addAttribute("nhanVien", nhanVien);
        nhanVien.setTrangThai(1);
        nhanVienService.addNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.detailNhanVien(id));
        model.addAttribute("listNV", nhanVienService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("listVT", vaiTroService.getAll());
        return "/admin/nhanvien/updatenhanvien";
    }

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien
            , BindingResult result, Model model) throws Exception {


        NhanVien existingNhanVien = nhanVienService.findById(nhanVien.getId());
        if (existingNhanVien == null) {
            model.addAttribute("error", "Không tìm thấy khách hàng.");
            return "/admin/nhanvien/updatenhanvien";
        }

        TaiKhoan taiKhoan = existingNhanVien.getTaiKhoan();
        if (taiKhoan == null) {
            taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(nhanVien.getSdt());
            taiKhoan.setMatKhau("123");
            taiKhoan.setTrangThai(1);
            VaiTro vaiTro = vaiTroService.findById(2);
            if (vaiTro != null) {
                taiKhoan.setVaiTro(vaiTro);
            } else {
                model.addAttribute("error", "Vai trò không tồn tại.");
                return "/admin/customer/updateKhachHang";
            }
            taiKhoanService.addTaiKhoan(taiKhoan);
        } else {
            taiKhoan.setTenDangNhap(nhanVien.getSdt());
            taiKhoan.setMatKhau("123");
            taiKhoanService.updateTaiKhoan(taiKhoan.getId(), taiKhoan);
        }
        nhanVien.setTaiKhoan(taiKhoan);


        MultipartFile anhUrlFile = nhanVien.getAnhUrlFile();
        String currentAnhUrl = existingNhanVien.getAnhUrl();

        if ((anhUrlFile == null || anhUrlFile.isEmpty()) && "u.png".equals(currentAnhUrl)) {
            nhanVien.setAnhUrl("u.png"); // Giữ nguyên ảnh mặc định
        }
        // Nếu upload ảnh mới, xử lý upload
        else if (anhUrlFile != null && !anhUrlFile.isEmpty()) {
            String newFileName = handleFileUpload(anhUrlFile, result, model, nhanVien);
            if (newFileName == null) {
                return "/admin/nhanvien/updatenhanvien";
            }
            uploadImage.deleteOldImage(currentAnhUrl);
            nhanVien.setAnhUrl(newFileName);
        } else {
            nhanVien.setAnhUrl(currentAnhUrl);
        }

        nhanVien.setTrangThai(1);

        nhanVienService.updateNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        nhanVienService.deleteNhanVien(id);
        return "redirect:/admin/nhanvien";
    }

    private String handleFileUpload(MultipartFile file, BindingResult result, Model model, NhanVien nhanVien) {
        try {
            // Kiểm tra loại tệp
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.nhanVien", "Tệp tải lên không phải là hình ảnh.");
                model.addAttribute("fieldErrors", result.getFieldErrors());
                model.addAttribute("nhanVien", nhanVien);
                return null;
            }

            // Lưu tệp
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/upload/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }
            File uploadFile = new File(uploadDir + fileName);
            file.transferTo(uploadFile);

            return fileName; // Trả về tên tệp mới
        } catch (IOException e) {
            e.printStackTrace();
            result.rejectValue("anhUrl", "error.nhanVien", "Lỗi khi tải lên tệp.");
            model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
            model.addAttribute("nhanVien", nhanVien);
            return null;
        }
    }
}


