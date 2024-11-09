package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.*;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.DiaChiService;
import nhom6.duancanhan.doantotnghiep.service.service.PhieuGiamGiaService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import nhom6.duancanhan.doantotnghiep.util.ChangeNumberOfDetailProduct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    //  TODO: Khai Báo Inject Rep or Component
    private final DiaChiService diaChiService;
    private final SanPhamService sanPhamService;
    private final HoaDonRepository hoaDonRepository;
    private final GioHangRepository gioHangRepository;
    private final AnhSanPhamService anhSanPhamService;
    private final PhieuGiamGiaService phieuGiamGiaService;
    private final KhachHangRepository khachHangRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamGioHangRepository sanPhamGioHangRepository;
    private final ChangeNumberOfDetailProduct changeNumberOfDetailProduct;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    //  TODO: Access Home Client
    @GetMapping
    private String index(
            @SessionAttribute(value = "user", required = false) KhachHang khachHang,
            HttpSession session, Model model
    ) {
        boolean isLoggedIn = session != null &&
                session.getAttribute("loginStatus") != null &&
                (Boolean) session.getAttribute("loginStatus");

        if (!isLoggedIn) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", khachHang);
        }

        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));
        return "client/trangchu";
    }

    //  TODO: Detail Sản Phẩm Chi Tiết từ ID Sản Phẩm
    @GetMapping("/san_pham_chi_tiet/{id}")
    private String sanPhamChiTiet(
            @SessionAttribute(value = "user", required = false) KhachHang khachHang,
            @PathVariable int id, Model model
    ) {
        // Retrieve product details by ID
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findSanPhamChiTietByIdSanPham(id);

        // Use Set to eliminate duplicate sizes
        Set<KichCo> uniqueSizes = new HashSet<>();
        // Map colors to a list of sizes
        Map<Integer, List<KichCo>> colorToSizeMap = new HashMap<>();
        // List of product detail images
        List<AnhSanPhamResponse> detailImageList = new ArrayList<>();
        // Map colors to representative images
        Map<MauSac, AnhSanPhamResponse> colorToImageMap = new HashMap<>();
        // List of product detail requests
        List<SanPhamChiTietRequest> productDetailRequests = new ArrayList<>();

        // Process each product detail
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            // Map sizes to their respective colors
            colorToSizeMap.computeIfAbsent(sanPhamChiTiet.getMauSac().getId(), k -> new ArrayList<>())
                    .add(sanPhamChiTiet.getKichCo());
            uniqueSizes.add(sanPhamChiTiet.getKichCo());

            // Get representative image for each color
            anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamChiTiet.getId())
                    .stream()
                    .findFirst()
                    .ifPresent(representativeImage ->
                            colorToImageMap.put(sanPhamChiTiet.getMauSac(), representativeImage)
                    );

            // Add detail images to the list
            detailImageList.addAll(anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamChiTiet.getId()));

            // Build and add product detail request
            productDetailRequests.add(SanPhamChiTietRequest.builder()
                    .idSanPham(sanPhamChiTiet.getSanPham().getId())
                    .idKichCo(sanPhamChiTiet.getKichCo().getId())
                    .idMauSac(sanPhamChiTiet.getMauSac().getId())
                    .gia(sanPhamChiTiet.getGia())
                    .soLuong(sanPhamChiTiet.getSoLuong())
                    .trangThai(sanPhamChiTiet.getTrangThai())
                    .build());
        }

        // Sort sizes and colors
        List<KichCo> sortedSizes = new ArrayList<>(uniqueSizes);
        sortedSizes.sort(Comparator.comparing(KichCo::getTenKichCo));

        LinkedHashMap<MauSac, AnhSanPhamResponse> sortedColorToImageMap = colorToImageMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(MauSac::getTenMauSac)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // Get product information and first image
        SanPhamShowOnClient sanPhamShowOnClient = sanPhamService.getSanPhamShowOnClientById(id);
        AnhSanPhamResponse firstImage = detailImageList.stream().findFirst().orElse(null);

        // Set image URL for the first image
        if (firstImage != null) {
            firstImage.setAnhUrl(sanPhamShowOnClient.getSanPhamResponse().getAnhUrl());
            if (!detailImageList.contains(firstImage)) {
                detailImageList.add(firstImage);
            }
        }

        // Add information to the model
        model.addAttribute("giaBanDau", productDetailRequests.isEmpty() ? null : productDetailRequests.get(0).getGia());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute("spcts", objectMapper.writeValueAsString(productDetailRequests));
            model.addAttribute("mauSacToKichCoJson", objectMapper.writeValueAsString(colorToSizeMap));
        } catch (Exception e) {
            // Consider using a logger instead of printing the stack trace
            e.printStackTrace();
        }

        model.addAttribute("user", khachHang);
        model.addAttribute("idSP", id);
        model.addAttribute("kichCoSet", sortedSizes);
        model.addAttribute("mauSacToAnhDaiDienMap", sortedColorToImageMap);
        model.addAttribute("anhSanPhamResponseList", detailImageList);
        model.addAttribute("infoSP", sanPhamShowOnClient);
        model.addAttribute("firstImageUrl", firstImage != null ? firstImage.getAnhUrl() : "img/default-image.jpg");
        model.addAttribute("sp_random", sanPhamService.getAllSanPhamShowOnClient("get-random"));
        return "/client/chitiet";
    }

    //  TODO: Add New Sản Phẩm Chi Tiết Vào Giỏ Hàng
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add_sp_vao_gio_hang/{idSP}")
    private void addSpVaoGioHang(
            @SessionAttribute(value = "user") KhachHang khachHang,
            @PathVariable(name = "idSP") int idSP, @RequestParam(name = "soluong") Integer soluong,
            @RequestParam(name = "mausac") Integer idMauSac, @RequestParam(name = "kichco") Integer idKichCo,
            HttpServletRequest httpServletRequest, Model model
    ) {
        Integer idGioHang = gioHangRepository.findByKhachHangId(khachHang.getId()).getId();
        addSPGH(khachHang.getId(), idGioHang, idSP, idKichCo, idMauSac, soluong);
    }

    //  TODO: Giỏ Hàng Sản Phẩm Chi Tiết Muốn Mua
    @GetMapping("/gio-hang")
    public String gioHang(
            @SessionAttribute(value = "user") KhachHang khachHang,
            Model model
    ) {
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId());

        List<Integer> listIDSPGH = new ArrayList<>();
        Map<ThuongHieu, List<SanPhamGioHangCustom>> thuongHieuSPGHListHashMap = new HashMap<>();

        //  What For : Xử lý các sản phẩm trong giỏ hàng
        sanPhamGioHangRepository.findByGioHangIdAndTrangThai(gioHang.getId(), 1).forEach(sanPhamGioHang -> {
            List<AnhSanPhamResponse> anhSanPhamList = anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamGioHang.getSanPhamChiTiet().getId());
            String anhUrl = anhSanPhamList.isEmpty() ? null : anhSanPhamList.get(0).getAnhUrl();

            SanPhamGioHangCustom sanPhamGioHangCustom = SanPhamGioHangCustom.builder()
                    .sanPhamGioHang(sanPhamGioHang)
                    .anhUrl(anhUrl)
                    .build();

            ThuongHieu thuongHieu = sanPhamGioHang.getSanPhamChiTiet().getSanPham().getThuongHieu();
            listIDSPGH.add(sanPhamGioHang.getId());
            thuongHieuSPGHListHashMap.computeIfAbsent(thuongHieu, k -> new ArrayList<>()).add(sanPhamGioHangCustom);
        });

        //  What For : Sắp xếp danh sách thương hiệu theo tên
        Map<ThuongHieu, List<SanPhamGioHangCustom>> sortedMap = thuongHieuSPGHListHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ThuongHieu::getTenThuongHieu)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        model.addAttribute("user", khachHang);
        model.addAttribute("listIDSPGH", listIDSPGH);
        model.addAttribute("thuongHieuSPGHListHashMap", sortedMap);
        return "/client/gio_hang";
    }

    //  TODO : Gio-Hang Post & Mua And Buy Quickly
    @PostMapping("/gio-hang")
    private String gioHangPost(
            @SessionAttribute(value = "user") KhachHang khachHang,
            @RequestParam(name = "buyNow", required = false) String buyNow,
            @RequestParam(name = "idSP", required = false) Integer idSP,
            @RequestParam(name = "mausac", required = false) Integer idMauSac,
            @RequestParam(name = "kichco", required = false) Integer idKichCo,
            @RequestParam(name = "soluong", required = false) Integer soluong,
            Model model
    ) {
        try {
            GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId());
            //  What For : Chức năng mua nhanh
            if (buyNow != null && !buyNow.isEmpty()) {
                SanPhamGioHang sanPhamGioHang = addSPGH(khachHang.getId(), gioHang.getId(), idSP, idKichCo, idMauSac, soluong);
            }
            return gioHang(khachHang, model);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //  TODO: Check-Out Sản Phẩm Chi Tiết
    @PostMapping(value = "/check-out")
    @ResponseStatus(HttpStatus.OK)
    private String checkOut(
            @SessionAttribute(value = "user") KhachHang khachHang,
            @RequestParam("productIds") List<Integer> listIDSPGHJson,
            Model model
    ) {
        List<SanPhamGioHangCustom> sanPhamGioHangCustomList = new ArrayList<>();
        List<Integer> listIDSPGH = new ArrayList<>();
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId());

        //  What For : Hiển thị list các sản phẩm check out nếu không mua nhanh
        if (listIDSPGHJson != null && !listIDSPGHJson.isEmpty()) {
            listIDSPGHJson.forEach(idspgh -> {
                SanPhamGioHang sanPhamGioHang = sanPhamGioHangRepository.findById(idspgh)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm giỏ hàng với id: " + idspgh));
                AnhSanPhamResponse anhSanPhamResponse = anhSanPhamService
                        .getAnhSanPhamByIdSPCT(sanPhamGioHang.getSanPhamChiTiet().getId()).get(0);
                sanPhamGioHangCustomList.add(SanPhamGioHangCustom.builder()
                        .sanPhamGioHang(sanPhamGioHang)
                        .anhUrl(anhSanPhamResponse.getAnhUrl())
                        .build());
                listIDSPGH.add(idspgh);
            });
        }

        //  What For : Calculate total amount
        BigDecimal tongTien = sanPhamGioHangCustomList.stream()
                .map(sanPhamGioHangCustom -> sanPhamGioHangCustom.getSanPhamGioHang().tongTien())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //  What For : Calculate shipping fee as 10% of the total amount
        BigDecimal phiShip = tongTien.multiply(BigDecimal.valueOf(0.1));

        List<DiaChi> diaChiList = diaChiService.getDiaChiByIdKhachHang(khachHang.getId());
        List<PhieuGiamGiaResponse> phieuGiamGiaResponseList = phieuGiamGiaService.getPGGByTrangThai(1);
        if (!phieuGiamGiaResponseList.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                model.addAttribute("diaChiList", objectMapper.writeValueAsString(diaChiList));
                model.addAttribute("phieugiamgia", objectMapper.writeValueAsString(phieuGiamGiaResponseList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("user", khachHang);
        model.addAttribute("listSP", sanPhamGioHangCustomList);
        model.addAttribute("phiShip", phiShip);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("listIDSPGH", listIDSPGH);
        return "/client/check-out";
    }

    //  TODO: Thanh Toán Sản Phẩm Chi Tiết
    @PostMapping("/thanh-toan")
    private void thanhToan(
            @SessionAttribute(value = "user") KhachHang khachHang,
            @RequestParam(name = "tongTien") BigDecimal tongTien,
            @RequestParam(name = "pttt") Integer pttt,
            @RequestParam(name = "listIDSPGH") String listIDSPGHString,
            @RequestParam(name = "idDiaChi") String idDiaChi,
            @RequestParam(name = "maPGG", required = false) String maPGG,
            @RequestParam(name = "ghiChu", required = false) String ghiChu,
            Model model
    ) {
        DiaChi diaChi = diaChiService.getDiaChiById(Integer.parseInt(idDiaChi));
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findById(pttt)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy phương thức thanh toán"));
        PhieuGiamGia phieuGiamGia = (maPGG == null || maPGG.isEmpty()) ? null : phieuGiamGiaService.getByMaPhieuGiamGia(maPGG);

        List<Integer> listIDSPGH;
        try {
            listIDSPGH = new ObjectMapper().readValue(listIDSPGHString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid list format for listIDSPGH", e);
        }

        HoaDon hoaDon = HoaDon.builder()
                .khachHang(khachHang)
                .tenNguoiNhan(diaChi.getTenKhachHang())
                .sdt(diaChi.getSoDienThoai())
                .emailNguoiNhan(khachHang.getEmail())
                .diaChi(diaChi)
                .phuongThucThanhToan(phuongThucThanhToan)
                .phieuGiamGia(phieuGiamGia)
                .tongTien(tongTien)
                .ghiChu(ghiChu)
                .trangThai(1)
                .build();
        hoaDon = hoaDonRepository.save(hoaDon);

        for (int idSPGH : listIDSPGH) {
            SanPhamGioHang sanPhamGioHang = sanPhamGioHangRepository.findById(idSPGH)
                    .orElseThrow(() -> new DataNotFoundException("Không thể tìm thấy sản phẩm giỏ hàng với ID: " + idSPGH));
            sanPhamGioHang.setTrangThai(0);
            sanPhamGioHangRepository.save(sanPhamGioHang);
            HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                    .gia(sanPhamGioHang.tongTien())
                    .hoaDon(hoaDon)
                    .sanPhamChiTiet(sanPhamGioHang.getSanPhamChiTiet())
                    .soLuong(sanPhamGioHang.getSoLuong())
                    .build();
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }
    }

    //  TODO: Update Sản Phẩm Chi Tiết In Giỏ Hàng
    private SanPhamGioHang addSPGH(
            Integer idKhachHang, Integer idGioHang, Integer idSP, Integer idKichCo, Integer idMauSac, Integer soluong
    ) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository
                .findSanPhamChiTietBySanPhamIdAndKichCoIdAndMauSacId(idSP, idKichCo, idMauSac);
        GioHang gioHang = gioHangRepository.findById(idGioHang)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng"));

        int idspct = sanPhamChiTiet.getId();
        SanPhamGioHang sanPhamGioHang;

        //  What For : Check xem có sản phẩm chi tiết có trong gio hàng không
        if (!sanPhamGioHangRepository.existsBySanPhamChiTietIdAndTrangThai(idspct, 1)) {
            // Nếu sản phẩm chi tiết không tồn tại trong giỏ hàng, thêm sản phẩm mới
            changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, soluong, "-");
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));
            sanPhamGioHang = SanPhamGioHang.builder().gioHang(gioHang).sanPhamChiTiet(sanPhamChiTiet).soLuong(soluong).trangThai(1).build();
        } else {
            // Nếu sản phẩm chi tiết đã tồn tại trong giỏ hàng, cập nhật số lượng
            sanPhamGioHang = sanPhamGioHangRepository
                    .findSanPhamGioHangByGioHangIdAndSanPhamChiTietIdAndTrangThai(idGioHang, idspct, 1);
            int soLuongSPGH = sanPhamGioHang.getSoLuong();
            sanPhamGioHang.setSoLuong(soLuongSPGH + soluong);
            changeNumberOfDetailProduct.updateProductDetailQuantity(idspct, soluong, "-");
        }

        return sanPhamGioHangRepository.save(sanPhamGioHang);
    }
}
