package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhom6.duancanhan.doantotnghiep.dto.*;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.*;
import nhom6.duancanhan.doantotnghiep.service.serviceimpl.VNPayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    //  TODO: Khai Báo Inject Rep or Component
    private final VNPayService vnPayService;
    private final DiaChiService diaChiService;
    private final SanPhamService sanPhamService;
    private final KieuCoAoService kieuCoAoService;
    private final ChatLieuService chatLieuService;
    private final HoaDonRepository hoaDonRepository;
    private final KieuTayAoService kieuTayAoService;
    private final GioHangRepository gioHangRepository;
    private final AnhSanPhamService anhSanPhamService;
    private final ThuongHieuService thuongHieuService;
    private final PhieuGiamGiaService phieuGiamGiaService;
    private final KhachHangRepository khachHangRepository;
    private final ForgotPasswordService forgotPasswordService;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamGioHangRepository sanPhamGioHangRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    //  TODO: Access Home Client
    @GetMapping
    private String index(
            @SessionAttribute(value = "user", required = false) KhachHang khachHang, @RequestParam(name = "thuonghieu", required = false) String tenThuongHieu,
            @RequestParam(name = "chatlieu", required = false) String tenChatLieu, @RequestParam(name = "kieucoao", required = false) String tenKieuCoAo,
            @RequestParam(name = "kieutayao", required = false) String tenKieuTayAo, @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "activeAccordion", required = false) List<String> activeAccordions, HttpSession session, Model model
    ) {
        if (session.getAttribute("user") == null) {
            session.setAttribute("loginStatus", false);
        }

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = session.getAttribute("loginStatus") != null && (Boolean) session.getAttribute("loginStatus");

        if (!isLoggedIn) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", khachHang);
        }

        // Lấy danh sách sản phẩm
        List<SanPhamShowOnClient> listSanPham;

        // Lưu trạng thái bảng lọc (true nếu có tham số lọc nào được gửi)
        boolean isFilterOpen = tenThuongHieu != null || tenChatLieu != null || tenKieuCoAo != null || tenKieuTayAo != null || sort != null;

        if (isFilterOpen) {
            listSanPham = sanPhamService.searchSanPham(tenThuongHieu, tenChatLieu, tenKieuTayAo, tenKieuCoAo, sort);
        } else {
            listSanPham = sanPhamService.getAllSanPhamShowOnClient("get-all");
        }

        // Thêm dữ liệu vào Model
        model.addAttribute("kieutayaos", kieuTayAoService.getAllKieuTayAo());
        model.addAttribute("kieucoaos", kieuCoAoService.getAllTenKieuCoAo());
        model.addAttribute("chatlieus", chatLieuService.getAllTenChatLieu());
        model.addAttribute("thuonghieus", thuongHieuService.getAllTenThuongHieu());
        model.addAttribute("sanphams", listSanPham);
        model.addAttribute("listSanPhamTimKiemHeader", sanPhamService.getAllSanPhamShowOnClient("get-all"));

        // Thêm các giá trị lọc vào Model để trả về giao diện
        model.addAttribute("selectedThuongHieu", tenThuongHieu);
        model.addAttribute("selectedChatLieu", tenChatLieu);
        model.addAttribute("selectedKieuCoAo", tenKieuCoAo);
        model.addAttribute("selectedKieuTayAo", tenKieuTayAo);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("isFilterOpen", isFilterOpen);

        // Thêm trạng thái accordion
        if (activeAccordions != null && !activeAccordions.isEmpty()) {
            model.addAttribute("activeAccordions", activeAccordions);
        }

        return "client/trangchu";
    }

    //  TODO: Detail Sản Phẩm Chi Tiết từ ID Sản Phẩm
    @GetMapping("/san_pham_chi_tiet/{id}")
    private String sanPhamChiTiet(@SessionAttribute(value = "user", required = false) KhachHang khachHang, @PathVariable(name = "id") int id, Model model) {
        // Retrieve product details by ID
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findBySanPhamIdAndTrangThai(id, 1);

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
                    .idSanPham(sanPhamChiTiet.getSanPham().getId()).idKichCo(sanPhamChiTiet.getKichCo().getId())
                    .idMauSac(sanPhamChiTiet.getMauSac().getId()).gia(sanPhamChiTiet.getGia())
                    .soLuong(sanPhamChiTiet.getSoLuong()).trangThai(sanPhamChiTiet.getTrangThai())
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
        model.addAttribute("idSP", id);
        model.addAttribute("user", khachHang);
        model.addAttribute("kichCoSet", sortedSizes);
        model.addAttribute("infoSP", sanPhamShowOnClient);
        model.addAttribute("anhSanPhamResponseList", detailImageList);
        model.addAttribute("mauSacToAnhDaiDienMap", sortedColorToImageMap);
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));
        model.addAttribute("sp_random", sanPhamService.getAllSanPhamShowOnClient("get-random"));
        return "/client/chitiet";
    }

    //  TODO: Add New Sản Phẩm Chi Tiết Vào Giỏ Hàng
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add_sp_vao_gio_hang/{idSP}")
    private void addSpVaoGioHang(
            HttpSession session, @PathVariable(name = "idSP") int idSP, @RequestParam(name = "soluong") Integer soluong,
            @RequestParam(name = "mausac") Integer idMauSac, @RequestParam(name = "kichco") Integer idKichCo, HttpServletResponse response
    ) {
        if (soluong == null || soluong <= 0 || idMauSac == null || idKichCo == null) {
            throw new IllegalArgumentException("Thông tin sản phẩm hoặc số lượng không hợp lệ.");
        }

        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            khachHang = khachHangRepository.save(KhachHang.builder().anhUrl("u.png").trangThai(1).build());
            session.setAttribute("user", khachHang);
        }

        KhachHang finalKhachHang = khachHang;

        log.info("Thông tin khách hàng : {}", finalKhachHang);

        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseGet(() -> gioHangRepository.save(GioHang.builder().khachHang(finalKhachHang).build()));

        Integer idGioHang = gioHang.getId();

        // Thêm sản phẩm vào giỏ hàng
        addSPGH(khachHang.getId(), idGioHang, idSP, idKichCo, idMauSac, soluong);
    }


    //  TODO: Giỏ Hàng Sản Phẩm Chi Tiết Muốn Mua
    @GetMapping("/gio-hang")
    public String gioHang(HttpSession session, Model model) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            khachHang = khachHangRepository.save(new KhachHang());
            session.setAttribute("user", khachHang);
        }

        KhachHang finalKhachHang = khachHang;

        GioHang gioHang = gioHangRepository.findByKhachHangId(finalKhachHang.getId())
                .orElseGet(() -> gioHangRepository.save(GioHang.builder().khachHang(finalKhachHang).build()));

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
                .sorted((entry1, entry2) -> {
                    // Tìm sản phẩm mới nhất trong mỗi thương hiệu
                    SanPhamGioHangCustom newestProduct1 = entry1.getValue().stream()
                            .max(Comparator.comparing(spgh -> spgh.getSanPhamGioHang().getId()))
                            .orElse(null);

                    SanPhamGioHangCustom newestProduct2 = entry2.getValue().stream()
                            .max(Comparator.comparing(spgh -> spgh.getSanPhamGioHang().getId()))
                            .orElse(null);

                    // Nếu một thương hiệu không có sản phẩm, nó sẽ được đẩy xuống cuối
                    if (newestProduct1 == null && newestProduct2 != null) {
                        return 1; // entry1 không có sản phẩm, entry2 có sản phẩm, vì vậy entry1 xuống sau
                    } else if (newestProduct1 != null && newestProduct2 == null) {
                        return -1; // entry1 có sản phẩm, entry2 không có sản phẩm, vì vậy entry1 lên trước
                    }

                    // Nếu cả hai thương hiệu đều có sản phẩm, so sánh ID của sản phẩm mới nhất
                    if (newestProduct1 != null && newestProduct2 != null) {
                        return newestProduct2.getSanPhamGioHang().getId()
                                .compareTo(newestProduct1.getSanPhamGioHang().getId());
                    }

                    return 0; // Nếu cả hai thương hiệu đều không có sản phẩm, không thay đổi vị trí
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<SanPhamGioHangCustom> products = entry.getValue();
                            // Sắp xếp sản phẩm trong mỗi thương hiệu theo ID giảm dần
                            products.sort(Comparator.comparing(spgh -> spgh.getSanPhamGioHang().getId(), Comparator.reverseOrder()));
                            return products;
                        },
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new // Giữ nguyên thứ tự chèn sau khi sắp xếp
                ));

        model.addAttribute("user", khachHang);
        model.addAttribute("listIDSPGH", listIDSPGH);
        model.addAttribute("thuongHieuSPGHListHashMap", sortedMap);
        return "/client/gio_hang";
    }

    //  TODO : Gio-Hang Post & Mua And Buy Quickly
    @PostMapping("/gio-hang")
    private String gioHangPost(
            HttpSession session, @RequestParam(name = "buyNow", required = false) String buyNow,
            @RequestParam(name = "idSP", required = false) Integer idSP,
            @RequestParam(name = "mausac", required = false) Integer idMauSac,
            @RequestParam(name = "kichco", required = false) Integer idKichCo,
            @RequestParam(name = "soluong", required = false) Integer soluong,
            Model model
    ) {
        try {
            KhachHang khachHang = (KhachHang) session.getAttribute("user");
            if (khachHang == null) {
                khachHang = khachHangRepository.save(KhachHang.builder().anhUrl("u.png").trangThai(1).build());
                session.setAttribute("user", khachHang);
            }

            KhachHang finalKhachHang = khachHang;

            GioHang gioHang = gioHangRepository.findByKhachHangId(finalKhachHang.getId())
                    .orElseGet(() -> gioHangRepository.save(GioHang.builder().khachHang(finalKhachHang).build()));

            //  Thêm sản phẩm vào giỏ hàng
            Integer addedProductId = null; // Lưu ID sản phẩm vừa thêm
            if (buyNow != null && !buyNow.isEmpty()) {
                SanPhamGioHang sanPhamGioHang = addSPGH(khachHang.getId(), gioHang.getId(), idSP, idKichCo, idMauSac, soluong);
                addedProductId = sanPhamGioHang.getId(); // Lấy ID của sản phẩm vừa thêm
            }

            // Gọi phương thức giỏ hàng, đồng thời truyền ID sản phẩm vừa thêm vào
            model.addAttribute("addedProductId", addedProductId); // Thêm vào model để gửi sang view
            return gioHang(session, model);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //  TODO: Check-Out Sản Phẩm Chi Tiết
    @PostMapping(value = "/check-out")
    @ResponseStatus(HttpStatus.OK)
    private String checkOut(HttpSession session, @RequestParam(value = "productIds", required = false) List<Integer> listIDSPGHJson, Model model) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            return "redirect:/login/login-client";
        }

        List<SanPhamGioHangCustom> sanPhamGioHangCustomList = new ArrayList<>();
        List<Integer> listIDSPGH = new ArrayList<>();
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId()).orElseThrow(
                () -> new DataNotFoundException("Không tìm thấy giỏ hàng với id : " + khachHang.getId())
        );

        //  What For : Hiển thị list các sản phẩm check out nếu không mua nhanh
        if (listIDSPGHJson != null && !listIDSPGHJson.isEmpty()) {
            listIDSPGHJson.forEach(idspgh -> {
                SanPhamGioHang sanPhamGioHang = sanPhamGioHangRepository.findById(idspgh)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm giỏ hàng với id: " + idspgh));
                AnhSanPhamResponse anhSanPhamResponse = anhSanPhamService
                        .getAnhSanPhamByIdSPCT(sanPhamGioHang.getSanPhamChiTiet().getId()).get(0);
                sanPhamGioHangCustomList.add(SanPhamGioHangCustom.builder()
                        .sanPhamGioHang(sanPhamGioHang).anhUrl(anhSanPhamResponse.getAnhUrl())
                        .build());
                listIDSPGH.add(idspgh);
            });
        }

        //  What For : Calculate total amount
        BigDecimal tongTien = sanPhamGioHangCustomList.stream()
                .map(sanPhamGioHangCustom -> sanPhamGioHangCustom.getSanPhamGioHang().tongTien())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //  What For : Calculate shipping fee as 10% of the total amount
        BigDecimal phiShip = BigDecimal.valueOf(10000);

        List<DiaChi> diaChiList = diaChiService.getDiaChiByIdKhachHang(khachHang.getId(), 1);
        List<PhieuGiamGiaResponse> phieuGiamGiaResponseList = phieuGiamGiaService.getPGGByTrangThai(1);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute("diaChiList", objectMapper.writeValueAsString(diaChiList));
            model.addAttribute("phieugiamgia", objectMapper.writeValueAsString(phieuGiamGiaResponseList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> emails = khachHangRepository.findAllEmails();

        model.addAttribute("emails", emails);
        model.addAttribute("user", khachHang);
        model.addAttribute("listSP", sanPhamGioHangCustomList);
        model.addAttribute("phiShip", phiShip);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("listIDSPGH", listIDSPGH);
        return "/client/check-out";
    }

    //  TODO: Thanh Toán Sản Phẩm Chi Tiết
    @PostMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(
            @SessionAttribute(value = "user", required = false) KhachHang khachHang, @RequestParam(name = "tongTien") BigDecimal tongTien,
            @RequestParam(name = "pttt") Integer pttt, @RequestParam(name = "listIDSPGH") String listIDSPGHString,
            @RequestParam(name = "idDiaChi") String idDiaChi, @RequestParam(name = "maPGG", required = false) String maPGG,
            @RequestParam(name = "ghiChu", required = false) String ghiChu, HttpServletRequest request
    ) {
        try {
            // Log input data
            log.info("Received payment request - Customer ID: {}", khachHang.getId());
            log.info("Payment details - Total: {}, Payment Method: {}, Address ID: {}", tongTien, pttt, idDiaChi);

            // Parse listIDSPGH
            List<Integer> listIDSPGH;
            try {
                listIDSPGH = new ObjectMapper().readValue(listIDSPGHString, new TypeReference<>() {
                });
                log.info("Product IDs to process: {}", listIDSPGH);
            } catch (JsonProcessingException e) {
                log.error("Error parsing product IDs: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Invalid product list format");
            }

            // Validate inputs
            if (listIDSPGH.isEmpty()) {
                return ResponseEntity.badRequest().body("No products selected for payment");
            }

            // Get address
            DiaChi diaChi = diaChiService.getDiaChiById(Integer.parseInt(idDiaChi));
            if (diaChi == null) {
                return ResponseEntity.badRequest().body("Invalid delivery address");
            }

            // Get payment method
            PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository
                    .findById(pttt).orElseThrow(() -> new DataNotFoundException("Invalid payment method"));

            // Get discount voucher if provided
            PhieuGiamGia phieuGiamGia = null;
            if (StringUtils.hasText(maPGG)) {
                phieuGiamGia = phieuGiamGiaService.getByMaPhieuGiamGia(maPGG);
                int sl_pgg = phieuGiamGia.getSoLuong();
                phieuGiamGia.setSoLuong(sl_pgg - 1);
                if (phieuGiamGia.getSoLuong() == 0) {
                    phieuGiamGia.setTrangThai(0);
                }
                phieuGiamGiaService.update(phieuGiamGia.getId(), phieuGiamGia);
            }

            log.info("phieuGiamGia : {}", phieuGiamGia);

            // Create order
            HoaDon hoaDon = HoaDon.builder()
                    .khachHang(khachHang).tenNguoiNhan(diaChi.getTenKhachHang())
                    .sdt(diaChi.getSoDienThoai()).emailNguoiNhan(khachHang.getEmail())
                    .diaChi(diaChi).phuongThucThanhToan(phuongThucThanhToan).phieuGiamGia(phieuGiamGia)
                    .tongTien(tongTien).ghiChu(ghiChu)
                    .trangThai(1).loaiHoaDon("Truc tuyen")
                    .build();

            if (pttt == 2) {
                hoaDon.setTrangThaiThanhToan(0); // COD chưa thanh toán
            } else {
                hoaDon.setTrangThaiThanhToan(1); // Thanh toán trước
            }

            // Save order
            hoaDon = hoaDonRepository.save(hoaDon);
            final HoaDon savedHoaDon = hoaDon;

            // Process each product
            listIDSPGH.forEach(idSPGH -> {
                try {
                    SanPhamGioHang sanPhamGioHang = sanPhamGioHangRepository.findById(idSPGH)
                            .orElseThrow(() -> new DataNotFoundException("Product not found in cart: " + idSPGH));

                    // Update cart item status
                    sanPhamGioHang.setTrangThai(0);
                    sanPhamGioHangRepository.save(sanPhamGioHang);

                    // Create order detail
                    HoaDonChiTiet hoaDonChiTiet = HoaDonChiTiet.builder()
                            .gia(sanPhamGioHang.tongTien())
                            .hoaDon(savedHoaDon).sanPhamChiTiet(sanPhamGioHang.getSanPhamChiTiet())
                            .soLuong(sanPhamGioHang.getSoLuong())
                            .build();
                    hoaDonChiTietRepository.save(hoaDonChiTiet);

                    log.info("Processed product ID: {} for order ID: {}", idSPGH, savedHoaDon.getId());
                } catch (Exception e) {
                    log.error("Error processing product ID: {} - {}", idSPGH, e.getMessage());
                    throw new RuntimeException("Error processing products", e);
                }
            });

            if (khachHang.getEmail() != null && !khachHang.getEmail().isEmpty()) {
                log.info("Đã gửi mail đến khách hàng với email: {} với hoá đơn: {}", khachHang.getEmail(), hoaDon.getId());
                forgotPasswordService.sendHoaDon(khachHang.getEmail(), hoaDon.getId());
            }

            // Xử lý thanh toán VNPay nếu pttt = 5
            if (pttt == 5) {
                try {
                    String urlReturn = "http://localhost:8080/client";

                    String vnpayPaymentUrl = vnPayService.createOrder(tongTien, urlReturn);

                    // Return payment URL for VNPay
                    return ResponseEntity.ok()
                            .body(new HashMap<String, String>() {{
                                put("redirectUrl", vnpayPaymentUrl);
                                put("orderId", String.valueOf(savedHoaDon.getId()));
                            }});
                } catch (Exception e) {
                    log.error("Error creating VNPay payment: {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error creating payment link");
                }
            }

            // Xử lý COD
            if (pttt == 2) {
                log.info("COD order created - Order ID: {}", hoaDon.getId());
                return ResponseEntity.ok()
                        .body(new HashMap<String, String>() {{
                            put("orderId", String.valueOf(savedHoaDon.getId()));
                            put("message", "Đặt hàng thành công! Chúng tôi sẽ giao hàng sớm nhất.");
                        }});
            }
            lichSuHoaDonRepository.save(LichSuHoaDon.builder().hoaDon(hoaDon).trangThai(hoaDon.getTrangThai()).build());
            log.info("Order processed successfully - Order ID: {}", hoaDon.getId());
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error processing order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing order: " + e.getMessage());
        }
    }

    // TODO : Thêm endpoint xử lý callback từ VNPay
    @GetMapping("/vnpay-payment")
    public String vnPayCallback(@RequestParam Map<String, String> queryParams, Model model) {
        try {
            String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
            String vnp_TxnRef = queryParams.get("vnp_TxnRef"); // Mã đơn hàng

            // Kiểm tra kết quả giao dịch từ VNPay
            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                // Cập nhật trạng thái đơn hàng
                log.info("Payment successful for order: {}", vnp_TxnRef);
            } else {
                // Thanh toán thất bại
                log.error("Payment failed for order: {}. Response code: {}", vnp_TxnRef, vnp_ResponseCode);
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback: {}", e.getMessage());
        }

        // Luôn redirect về trang client
        return "redirect:/client";
    }

    //    TODO : Thông Tin Khách Hàng
    @GetMapping(value = "/showInfoCustomer")
    private String showInfoKhachHang(HttpSession session, Model model) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            return "redirect:/login/login-client";
        }
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));
        return "/client/customer/ThongTinKhachHang";
    }

    //    TODO : Thông Tin Address
    @GetMapping(value = "/showInfoAddress")
    private String showInfoAddress(HttpSession session, Model model) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            return "redirect:/login/login-client";
        }
        model.addAttribute("diachi", diaChiService.getDiaChiByIdKhachHang(khachHang.getId(), 1));
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));
        return "/client/customer/ThongTinDiaChi";
    }

    //    TODO : Thông Tin Hóa Đơn
    @GetMapping(value = {"/showInfoBill", "/showInfoBill/type/{type}"})
    private String showInfoBill(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
            @PathVariable(name = "type", required = false) Integer type, // Tham số `type` không bắt buộc
            HttpSession session, Model model
    ) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            return "redirect:/login/login-client";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<HoaDon> hoaDons;

        // Kiểm tra `type` để lấy danh sách hóa đơn phù hợp
        if (type != null) {
            hoaDons = hoaDonRepository.findHoaDonByKhachHangIdAndTrangThaiOrderByIdDesc(khachHang.getId(), type, pageable);
        } else {
            hoaDons = hoaDonRepository.findHoaDonByKhachHangIdOrderByIdDesc(khachHang.getId(), pageable);
        }

        // Số trang hiện tại và tổng số trang
        int totalPages = hoaDons.getTotalPages();
        int currentPage = hoaDons.getNumber();

        // Tính toán chỉ hiển thị tối đa 5 trang (vùng lân cận)
        int startPage = Math.max(0, currentPage - 2); // Bắt đầu từ 2 trang trước
        int endPage = Math.min(totalPages - 1, currentPage + 2); // Kết thúc ở 2 trang sau

        // Thêm dữ liệu vào model
        model.addAttribute("hoaDons", hoaDons.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));
        model.addAttribute("type", type); // Truyền `type` vào model để dùng ở frontend

        return "/client/customer/ThongTinHoaDon";
    }

    //    TODO : Thông Tin Chi Tiết Hóa Đơn
    @GetMapping(value = "/showDetailInfoBill/idHD/{idHD}")
    private String showDetailInfoBill(HttpSession session, Model model, @PathVariable(name = "idHD") Integer idHD) {
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        if (khachHang == null) {
            return "redirect:/login/login-client";
        }

        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hóa đơn với id: " + idHD));
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient("get-all"));

        BigDecimal tongTien = hoaDon.getHoaDonChiTietList().stream()
                .map(HoaDonChiTiet::tongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tienGG = null;

        PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia() != null ? hoaDon.getPhieuGiamGia() : null;

        if (phieuGiamGia != null) {
            if (phieuGiamGia.getKieuGiamGia() == 1) {
                // Giảm giá theo phần trăm (10, 20 -> 0.1, 0.2)
                BigDecimal phanTramGiam = phieuGiamGia.getGiaTriGiam().divide(BigDecimal.valueOf(100));
                tienGG = tongTien.multiply(phanTramGiam);
            } else {
                // Giảm giá theo số tiền cố định
                tienGG = phieuGiamGia.getGiaTriGiam();
            }
        }

        log.info("Tong Tien : {}", tongTien);

        model.addAttribute("tienShip", BigDecimal.valueOf(10000));
        model.addAttribute("tienGG", tienGG == null ? BigDecimal.ZERO : tienGG);
        model.addAttribute("tongTien", tongTien);

        if (hoaDon.getTrangThai() == 5 || hoaDon.getTrangThai() == 7) {
            model.addAttribute("hd", hoaDon);
            return "/client/customer/ThongTinHoaDonHuy";
        } else {
            List<LichSuHoaDon> lichSuHoaDons = lichSuHoaDonRepository.findByHoaDonId(idHD);

            model.addAttribute("hd", hoaDon);
            model.addAttribute("lshd", lichSuHoaDons);
            return "/client/customer/ThongTinHoaDonHT";
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
        if (!sanPhamGioHangRepository.existsBySanPhamChiTietIdAndTrangThaiAndGioHangId(idspct, 1, gioHang.getId())) {
            // Nếu sản phẩm chi tiết không tồn tại trong giỏ hàng, thêm sản phẩm mới
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));
            sanPhamGioHang = SanPhamGioHang.builder().gioHang(gioHang).sanPhamChiTiet(sanPhamChiTiet).soLuong(soluong).trangThai(1).build();
        } else {
            // Nếu sản phẩm chi tiết đã tồn tại trong giỏ hàng, cập nhật số lượng
            sanPhamGioHang = sanPhamGioHangRepository
                    .findSanPhamGioHangByGioHangIdAndSanPhamChiTietIdAndTrangThai(idGioHang, idspct, 1);
            int soLuongSPGH = sanPhamGioHang.getSoLuong();
            sanPhamGioHang.setSoLuong(soLuongSPGH + soluong);
        }

        return sanPhamGioHangRepository.save(sanPhamGioHang);
    }
}
