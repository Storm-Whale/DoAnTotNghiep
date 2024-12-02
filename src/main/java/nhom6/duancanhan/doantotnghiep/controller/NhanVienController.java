package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.KhachHang;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
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
        Page<NhanVien> listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listNV.getTotalPages();

        // Xử lý khi vượt quá tổng số trang
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0;
            listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size);
        }

        // Tạo danh sách số trang
        List<Integer> pageNumbers = totalPages > 0
                ? IntStream.range(0, totalPages).boxed().collect(Collectors.toList())
                : Collections.emptyList();

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
    @GetMapping("/{id}/hoa-dons")
    public String getHoaDonsByNhanVien(@PathVariable("id") Integer nhanVienId, Model model) {
        List<HoaDon> hoaDonList = hoaDonService.findByNhanVienId(nhanVienId);
        model.addAttribute("hoaDonList", hoaDonList);
        return "/admin/nhanvien/nhan-vien-hoa-don"; // Tên file Thymeleaf để hiển thị danh sách hóa đơn
    }
    @GetMapping("/{id}")
    public String xemChiTietHoaDon(@PathVariable("id") Integer idHoaDon, Model model) {
        HoaDon hoaDon = hoaDonService.findById(idHoaDon);
        model.addAttribute("hoaDon", hoaDon);
        return "/admin/nhanvien/nhan-vien-chitietsp"; // Chỉ đến file Thymeleaf
    }
//    @GetMapping("{pageNo}")
//    public String phanTrang(@PathVariable(value = "pageNo") int pageNo, Model model) {
//        int pageSize = 3;
//        Page<NhanVien> page = nhanVienService.phanTrang(pageNo,pageSize);
//        List<NhanVien> listNV = page.getContent();
//        model.addAttribute("nhanVien",new NhanVien());
//        model.addAttribute("listNV",listNV);
//        model.addAttribute("listTK",taiKhoanService.getAll());
//        model.addAttribute("listVT",vaiTroService.getAll());
//        model.addAttribute("currentPage ", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems",page.getTotalElements());
//        return "/admin/nhanvien/nhanvien";
//    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.detailNhanVien(id));
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("listNV", nhanVienService.getAll());
        model.addAttribute("listVT", vaiTroService.getAll());
        return "/admin/nhanvien/nhanvien";
    }
    @GetMapping("/view_add")
    public String viewadd( @ModelAttribute("nhanVien") NhanVien nhanVien) {
        return "/admin/nhanvien/adNhanVien";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien,
//                      @RequestParam(value = "anhUrlFile", required = false) MultipartFile anhUrlFile,
                      BindingResult result, Model model) {
        MultipartFile anhUrlFile = nhanVien.getAnhUrlFile();
        if (anhUrlFile == null || anhUrlFile.isEmpty()) {
            result.rejectValue("anhUrl", "error.nhanVien", "Vui lòng chọn tệp ảnh.");
        } else {
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.nhanVien", "Tệp tải lên không phải là hình ảnh.");
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

//        model.addAttribute("nhanVien", nhanVien);
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

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien
//                         @RequestParam(value = "anhUrlFile", required = false) MultipartFile anhUrlFile
            , BindingResult result, Model model) throws Exception {

        NhanVien existingNhanVien = nhanVienService.findById(nhanVien.getId());
        if (existingNhanVien == null) {
            model.addAttribute("error", "Không tìm thấy khách hàng.");
            return "/admin/nhanvien/updatenhanvien";
        }
        uploadImage.deleteOldImage(existingNhanVien.getAnhUrl());


        MultipartFile anhUrlFile = nhanVien.getAnhUrlFile();
        if (anhUrlFile != null && !anhUrlFile.isEmpty()) {
            String contentType = anhUrlFile.getContentType();
            if (!contentType.startsWith("image/")) {
                result.rejectValue("anhUrl", "error.khachHang", "Tệp tải lên không phải là hình ảnh.");
            }
        } else {
            // Nếu không tải lên tệp mới, giữ ảnh cũ
            nhanVien.setAnhUrl(existingNhanVien.getAnhUrl());
        }


        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/admin/nhanvien/updatenhanvien";
        }
        // Xử lý tệp tải lên (nếu có)
        if (anhUrlFile != null && !anhUrlFile.isEmpty()) {
            try {

                String fileName = uploadImage.saveImage(anhUrlFile);
                // Gán tên tệp (hoặc đường dẫn) vào thuộc tính `anhUrl`
                nhanVien.setAnhUrl(fileName);
            } catch (IOException e) {
                model.addAttribute("uploadError", "Lỗi khi tải lên tệp.");
                return "/admin/nhanvien/updatenhanvien";
            }
        } else {
            // Giữ lại ảnh cũ
            nhanVien.setAnhUrl(existingNhanVien.getAnhUrl());
        }
//        model.addAttribute("nhanVien",nhanVien);
        nhanVienService.updateNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        nhanVienService.deleteNhanVien(id);
        return "redirect:/admin/nhanvien";
    }
//    @GetMapping("/search")
//    public String searchNhanVien(
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "trangThai", required = false) Integer trangThai,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "2") int size,
//            Model model) {
//        Page<NhanVien> listNV = nhanVienService.SearchandPhantrang(keyword,trangThai, page, size);
//        model.addAttribute("listNV", listNV);
//        model.addAttribute("nhanVien",new NhanVien());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", listNV.getTotalPages());
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("trangThai", trangThai);
//        return "/admin/nhanvien/nhanvien";
//    }
//    @GetMapping("/search")
//    public String search(@Valid @ModelAttribute, BindingResult result, Model model) {
//        if(result.hasErrors()) {
//            for(FieldError error : result.getFieldErrors()) {
//              model.addAttribute(error.getField(),error.getDefaultMessage());
//            }
//            phanTrang(1,model);
//            return "/admin/nhanvien/nhanvien";
//        }
//        List<NhanVien> nv = nhanVienService.findSearch(searchDTO.getKeyword());
//      if(nv.isEmpty()) {
//          model.addAttribute("message", "No results found");
//      }else{
//          model.addAttribute("nhanVien",new NhanVien());
//          model.addAttribute("listNV",nv);
//      }
//        return "/admin/nhanvien/nhanvien";
//    }
}
