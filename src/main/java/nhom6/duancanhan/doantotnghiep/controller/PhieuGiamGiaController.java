package nhom6.duancanhan.doantotnghiep.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nhom6.duancanhan.doantotnghiep.dto.PhieuGiamGiaHoaDonDTO;
import nhom6.duancanhan.doantotnghiep.entity.PhieuGiamGia;
import nhom6.duancanhan.doantotnghiep.service.service.HoaDonService;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaService phieuGiamGiaService;
    private final HoaDonService hoaDonService;

    public PhieuGiamGiaController(PhieuGiamGiaService phieuGiamGiaService, HoaDonService hoaDonService) {
        this.phieuGiamGiaService = phieuGiamGiaService;
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("/index")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "ngayBatDau", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc,
            @RequestParam(value = "kieuGiamGia", required = false) Integer kieuGiamGia,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            Model model) {
        // Lấy ngày hiện tại (đặt về đầu ngày)
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);
        Date today = calToday.getTime();
        //
        Page<PhieuGiamGia> pageFind = phieuGiamGiaService.findByCriteria(keyword, ngayBatDau,
                ngayKetThuc, kieuGiamGia, trangThai, page, size);
        Pageable pageable = PageRequest.of(page, size);
        List<PhieuGiamGia> listPGG = pageFind.getContent();
        // Duyệt và cập nhật trạng thái
        for (PhieuGiamGia phieuGiamGia : listPGG) {
            boolean needUpdate = false;

            // Kiểm tra và cập nhật trạng thái dựa trên số lượng
            if (phieuGiamGia.getSoLuong() == 0) {
                phieuGiamGia.setTrangThai(0); // Inactive
                needUpdate = true;
            } else if (phieuGiamGia.getNgayBatDau() != null && phieuGiamGia.getNgayKetThuc() != null) {
                // Đặt lại giờ cho ngày bắt đầu và kết thúc về đầu ngày để so sánh chính xác
                Calendar calNgayBatDau = Calendar.getInstance();
                calNgayBatDau.setTime(phieuGiamGia.getNgayBatDau());
                calNgayBatDau.set(Calendar.HOUR_OF_DAY, 0);
                calNgayBatDau.set(Calendar.MINUTE, 0);
                calNgayBatDau.set(Calendar.SECOND, 0);
                calNgayBatDau.set(Calendar.MILLISECOND, 0);
                //
                Calendar calNgayKetThuc = Calendar.getInstance();
                calNgayKetThuc.setTime(phieuGiamGia.getNgayKetThuc());
                calNgayKetThuc.set(Calendar.HOUR_OF_DAY, 23);
                calNgayKetThuc.set(Calendar.MINUTE, 59);
                calNgayKetThuc.set(Calendar.SECOND, 59);
                calNgayKetThuc.set(Calendar.MILLISECOND, 999);
                // Kiểm tra và set trạng thái
                int newTrangThai;
                if (today.compareTo(calNgayBatDau.getTime()) >= 0 &&
                        today.compareTo(calNgayKetThuc.getTime()) <= 0) {
                    // Nếu ngày hiện tại nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc
                    newTrangThai = 1; // Active
                } else if (today.compareTo(calNgayBatDau.getTime()) < 0) {
                    // Nếu ngày hiện tại chưa đến ngày bắt đầu
                    newTrangThai = 0; // Inactive
                } else {
                    // Nếu đã qua ngày kết thúc
                    newTrangThai = 0; // Inactive
                }
                // Cập nhật trạng thái nếu khác trạng thái hiện tại
                if (phieuGiamGia.getTrangThai() != newTrangThai) {
                    phieuGiamGia.setTrangThai(newTrangThai);
                    needUpdate = true;
                }
            }
            // Lưu trạng thái mới vào database nếu có thay đổi
            if (needUpdate) {
                phieuGiamGiaService.update(phieuGiamGia.getId(), phieuGiamGia);
            }
        }
        model.addAttribute("listPGG", listPGG);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageFind.getTotalPages());
        model.addAttribute("totalItems", pageFind.getTotalElements());

        // Thêm các tham số tìm kiếm vào model để hiển thị lại trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("ngayBatDau", ngayBatDau);
        model.addAttribute("ngayKetThuc", ngayKetThuc);
        model.addAttribute("kieuGiamGia", kieuGiamGia);
        model.addAttribute("trangThai", trangThai);

        return "/admin/PhieuGiamGia/PhieuGiamGia";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaService.getById(id);
        if (phieuGiamGia.isPresent()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia.get());
            List<PhieuGiamGiaHoaDonDTO> hoaDonList = hoaDonService.getHoaDonByPhieuGiamGia(id);
            model.addAttribute("hoaDonList", hoaDonList);
            return "/admin/PhieuGiamGia/Detail";
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia) {
        String maPhieuGiamGia = phieuGiamGiaService.generateMaPhieuGiamGia();
        phieuGiamGia.setMaPhieuGiamGia(maPhieuGiamGia);
        return "/admin/PhieuGiamGia/Add";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<PhieuGiamGia> phieuGiamGia = phieuGiamGiaService.getById(id);
        if (phieuGiamGia.isPresent()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia.get());
            return "/admin/PhieuGiamGia/Update";
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
            BindingResult bindingResult,
            Model model) {
        // Lấy ngày hiện tại (đặt về đầu ngày)
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);
        Date today = calToday.getTime();
        // Kiểm tra ngày bắt đầu không được là ngày trong quá khứ
        if (phieuGiamGia.getNgayBatDau() != null) {
            Calendar calNgayBatDau = Calendar.getInstance();
            calNgayBatDau.setTime(phieuGiamGia.getNgayBatDau());
            calNgayBatDau.set(Calendar.HOUR_OF_DAY, 0);
            calNgayBatDau.set(Calendar.MINUTE, 0);
            calNgayBatDau.set(Calendar.SECOND, 0);
            calNgayBatDau.set(Calendar.MILLISECOND, 0);

            // Kiểm tra ngày bắt đầu không được là ngày trong quá khứ
            if (calNgayBatDau.getTime().before(today)) {
                bindingResult.rejectValue("ngayBatDau", "error.phieuGiamGia", "Ngày bắt đầu không được là ngày trong quá khứ");
            }
        }
        // Kiểm tra ngày kết thúc không được là ngày trong quá khứ
        if (phieuGiamGia.getNgayKetThuc() != null) {
            Calendar calNgayKetThuc = Calendar.getInstance();
            calNgayKetThuc.setTime(phieuGiamGia.getNgayKetThuc());
            calNgayKetThuc.set(Calendar.HOUR_OF_DAY, 0);
            calNgayKetThuc.set(Calendar.MINUTE, 0);
            calNgayKetThuc.set(Calendar.SECOND, 0);
            calNgayKetThuc.set(Calendar.MILLISECOND, 0);

            // Kiểm tra ngày kết thúc không được là ngày trong quá khứ
            if (calNgayKetThuc.getTime().before(today)) {
                bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc không được là ngày trong quá khứ");
            }
        }
        // Kiểm tra ngày kết thúc phải sau hoặc bằng ngày bắt đầu
        if (phieuGiamGia.getNgayBatDau() != null
                && phieuGiamGia.getNgayKetThuc() != null
                && phieuGiamGia.getNgayKetThuc().before(phieuGiamGia.getNgayBatDau())) {
            bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
        }
        // Kiểm tra ngày bắt đầu không được vượt quá 1 năm từ ngày hiện tại
        if (phieuGiamGia.getNgayBatDau() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.YEAR, 1);
            Date oneYearLater = calendar.getTime();

            if (phieuGiamGia.getNgayBatDau().after(oneYearLater)) {
                bindingResult.rejectValue("ngayBatDau", "error.phieuGiamGia", "Ngày bắt đầu không được vượt quá 1 năm từ ngày hiện tại");
            }
        }
        if (phieuGiamGia.getSoLuong() < 0) {
            bindingResult.rejectValue("soLuong", "error.phieuGiamGia", "Số lượng phiếu giảm giá cập nhật không được là số âm");
        }
        // Kiểm tra giá trị giảm khi kiểu giảm giá là 1 (%)
        if (phieuGiamGia.getKieuGiamGia() == 1 && phieuGiamGia.getGiaTriGiam() != null
                && phieuGiamGia.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            bindingResult.rejectValue("giaTriGiam", "error.phieuGiamGia", "Giá trị giảm không được lớn hơn 100% khi giảm theo phần trăm.");
        }
        // Nếu có lỗi, trả về lại form sửa và hiển thị lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("phieuGiamGia", phieuGiamGia);
            return "/admin/PhieuGiamGia/Update";
        }

        // Tự động set trạng thái
        if (phieuGiamGia.getSoLuong() == 0) {
            phieuGiamGia.setTrangThai(0); // Inactive
        } else {
            if (phieuGiamGia.getNgayBatDau() != null && phieuGiamGia.getNgayKetThuc() != null) {
                // Đặt lại giờ cho ngày bắt đầu và kết thúc về đầu ngày để so sánh chính xác
                Calendar calNgayBatDau = Calendar.getInstance();
                calNgayBatDau.setTime(phieuGiamGia.getNgayBatDau());
                calNgayBatDau.set(Calendar.HOUR_OF_DAY, 0);
                calNgayBatDau.set(Calendar.MINUTE, 0);
                calNgayBatDau.set(Calendar.SECOND, 0);
                calNgayBatDau.set(Calendar.MILLISECOND, 0);

                Calendar calNgayKetThuc = Calendar.getInstance();
                calNgayKetThuc.setTime(phieuGiamGia.getNgayKetThuc());
                calNgayKetThuc.set(Calendar.HOUR_OF_DAY, 23);
                calNgayKetThuc.set(Calendar.MINUTE, 59);
                calNgayKetThuc.set(Calendar.SECOND, 59);
                calNgayKetThuc.set(Calendar.MILLISECOND, 999);

                // Kiểm tra và set trạng thái
                if (today.compareTo(calNgayBatDau.getTime()) >= 0 &&
                        today.compareTo(calNgayKetThuc.getTime()) <= 0) {
                    // Nếu ngày hiện tại nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc
                    phieuGiamGia.setTrangThai(1); // Active
                } else if (today.compareTo(calNgayBatDau.getTime()) < 0) {
                    // Nếu ngày hiện tại chưa đến ngày bắt đầu
                    phieuGiamGia.setTrangThai(0); // Inactive
                } else {
                    // Nếu đã qua ngày kết thúc
                    phieuGiamGia.setTrangThai(0);
                }
            }
        }

        // Cập nhật thông tin nếu không có lỗi
        phieuGiamGiaService.update(id, phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia/index";
    }

    @PostMapping("/update-status/{id}")
    @Transactional
    public String updateStatus(@PathVariable("id") Integer id) {
        Optional<PhieuGiamGia> phieuGiamGiaOptional = phieuGiamGiaService.getById(id);
        if (phieuGiamGiaOptional.isPresent()) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaOptional.get();
            phieuGiamGia.setTrangThai(0);
            phieuGiamGiaService.updateStatus(id, phieuGiamGia);
        }
        return "redirect:/admin/phieu-giam-gia/index";
    }


    @PostMapping("/addpg")
    public String addpg(@Valid @ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                        BindingResult bindingResult, Model model) {
        // Lấy ngày hiện tại (đặt về đầu ngày)
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);
        Date today = calToday.getTime();
        // Tự động set trạng thái
        if (phieuGiamGia.getNgayBatDau() != null && phieuGiamGia.getNgayKetThuc() != null) {
            // Đặt lại giờ cho ngày bắt đầu và kết thúc về đầu ngày để so sánh chính xác
            Calendar calNgayBatDau = Calendar.getInstance();
            calNgayBatDau.setTime(phieuGiamGia.getNgayBatDau());
            calNgayBatDau.set(Calendar.HOUR_OF_DAY, 0);
            calNgayBatDau.set(Calendar.MINUTE, 0);
            calNgayBatDau.set(Calendar.SECOND, 0);
            calNgayBatDau.set(Calendar.MILLISECOND, 0);

            Calendar calNgayKetThuc = Calendar.getInstance();
            calNgayKetThuc.setTime(phieuGiamGia.getNgayKetThuc());
            calNgayKetThuc.set(Calendar.HOUR_OF_DAY, 23);
            calNgayKetThuc.set(Calendar.MINUTE, 59);
            calNgayKetThuc.set(Calendar.SECOND, 59);
            calNgayKetThuc.set(Calendar.MILLISECOND, 999);

            // Kiểm tra và set trạng thái
            if (today.compareTo(calNgayBatDau.getTime()) >= 0 &&
                    today.compareTo(calNgayKetThuc.getTime()) <= 0) {
                // Nếu ngày hiện tại nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc
                phieuGiamGia.setTrangThai(1); // Active
            } else if (today.compareTo(calNgayBatDau.getTime()) < 0) {
                // Nếu ngày hiện tại chưa đến ngày bắt đầu
                phieuGiamGia.setTrangThai(0); // Inactive
            } else {
                // Nếu đã qua ngày kết thúc
                phieuGiamGia.setTrangThai(0);
            }
        }
        // Đặt ngày bắt đầu về đầu ngày để so sánh chính xác
        if (phieuGiamGia.getNgayBatDau() != null) {
            Calendar calNgayBatDau = Calendar.getInstance();
            calNgayBatDau.setTime(phieuGiamGia.getNgayBatDau());
            calNgayBatDau.set(Calendar.HOUR_OF_DAY, 0);
            calNgayBatDau.set(Calendar.MINUTE, 0);
            calNgayBatDau.set(Calendar.SECOND, 0);
            calNgayBatDau.set(Calendar.MILLISECOND, 0);
            // Kiểm tra ngày bắt đầu không được là ngày trong quá khứ
            if (calNgayBatDau.getTime().before(today)) {
                bindingResult.rejectValue("ngayBatDau", "error.phieuGiamGia", "Ngày bắt đầu không được là ngày trong quá khứ");
            }
        }
        //
        if (phieuGiamGia.getNgayKetThuc() != null) {
            Calendar calNgayKetThuc = Calendar.getInstance();
            calNgayKetThuc.setTime(phieuGiamGia.getNgayKetThuc());
            calNgayKetThuc.set(Calendar.HOUR_OF_DAY, 0);
            calNgayKetThuc.set(Calendar.MINUTE, 0);
            calNgayKetThuc.set(Calendar.SECOND, 0);
            calNgayKetThuc.set(Calendar.MILLISECOND, 0);
            // Kiểm tra ngày kết thúc không được là ngày trong quá khứ
            if (calNgayKetThuc.getTime().before(today)) {
                bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc không được là ngày trong quá khứ");
            }
        }

        // Kiểm tra ngày kết thúc phải sau hoặc trùng ngày bắt đầu
        if (phieuGiamGia.getNgayBatDau() != null
                && phieuGiamGia.getNgayKetThuc() != null
                && phieuGiamGia.getNgayKetThuc().before(phieuGiamGia.getNgayBatDau())) {
            bindingResult.rejectValue("ngayKetThuc", "error.phieuGiamGia", "Ngày kết thúc phải sau hoặc trùng với ngày bắt đầu");
        }
        // Kiểm tra ngày bắt đầu không được vượt quá 1 năm từ ngày hiện tại
        if (phieuGiamGia.getNgayBatDau() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.YEAR, 1);
            Date oneYearLater = calendar.getTime();

            if (phieuGiamGia.getNgayBatDau().after(oneYearLater)) {
                bindingResult.rejectValue("ngayBatDau", "error.phieuGiamGia", "Ngày bắt đầu không được vượt quá 1 năm từ ngày hiện tại");
            }
        }
        // Validate số lượng phiếu giảm giá
        if (phieuGiamGia.getSoLuong() < 1) {
            bindingResult.rejectValue("soLuong", "error.phieuGiamGia", "Số lượng phiếu giảm giá phải lớn hơn hoặc bằng 1");
        }
        if (phieuGiamGia.getKieuGiamGia() == 1 && phieuGiamGia.getGiaTriGiam() != null
                && phieuGiamGia.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            bindingResult.rejectValue("giaTriGiam", "error.phieuGiamGia", "Giá trị giảm không được lớn hơn 100% khi giảm theo phần trăm.");
        }

        if (bindingResult.hasErrors()) {
            return "/admin/PhieuGiamGia/Add";
        }
        phieuGiamGiaService.create(phieuGiamGia);
        return "redirect:/admin/phieu-giam-gia/index";
    }
}
