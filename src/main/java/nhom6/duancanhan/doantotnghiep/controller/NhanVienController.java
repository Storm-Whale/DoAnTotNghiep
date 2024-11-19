package nhom6.duancanhan.doantotnghiep.controller;


import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.entity.NhanVien;
import nhom6.duancanhan.doantotnghiep.service.service.NhanVienService;
import nhom6.duancanhan.doantotnghiep.service.service.TaiKhoanService;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
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


import java.util.List;
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

    @GetMapping("")
    public String searchNhanVien(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        // Kiểm tra và xử lý giá trị keyword nếu có
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null; // Đặt thành null nếu keyword rỗng
            }
        }

        // Đảm bảo trang không âm
        if (page < 0) {
            page = 0;
        }

        // Đảm bảo kích thước trang hợp lệ
        if (size < 1) {
            size = 5;
        }

        // Lấy danh sách nhân viên đã phân trang
        Page<NhanVien> listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size);
        int totalPages = listNV.getTotalPages();

        // Nếu trang yêu cầu lớn hơn tổng số trang, chuyển sang trang cuối
        if (page >= totalPages) {
            page = totalPages > 0 ? totalPages - 1 : 0;
            listNV = nhanVienService.SearchandPhantrang(keyword, trangThai, page, size); // Lấy dữ liệu trang cuối
        }

        // Tạo danh sách các số trang để phân trang
        List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());

        // Thêm các thuộc tính vào model để hiển thị trên view
        model.addAttribute("listNV", listNV); // Danh sách nhân viên đã phân trang
        model.addAttribute("nhanVien", new NhanVien()); // Đối tượng nhân viên mới để thêm vào form
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", totalPages); // Tổng số trang
        model.addAttribute("keyword", keyword); // Từ khóa tìm kiếm
        model.addAttribute("trangThai", trangThai); // Trạng thái lọc
        model.addAttribute("pageNumbers", pageNumbers); // Số trang phân trang

        // Trả về view với tên tương ứng
        return "/admin/nhanvien/nhanvien";
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
        model.addAttribute("nhanVien", nhanVienService.detail(id));
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
    public String add(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("error", "Kiểm tra lại");
            return "/admin/nhanvien/adNhanVien";
        }
        model.addAttribute("nhanVien", nhanVien);
        nhanVienService.addNhanVien(nhanVien);
        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/view_update/{id}")
    public String viewUpdate(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.detail(id));
        model.addAttribute("listNV", nhanVienService.getAll());
        model.addAttribute("listTK", taiKhoanService.getAll());
        model.addAttribute("listVT", vaiTroService.getAll());
        return "/admin/nhanvien/updatenhanvien";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("nhanVien") NhanVien nhanVien, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("listTK", taiKhoanService.getAll());
            model.addAttribute("listVT", vaiTroService.getAll());
            return "/admin/nhanvien/updatenhanvien";
        }
        model.addAttribute("nhanVien", nhanVien);
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
