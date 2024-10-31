package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamGioHangCustom;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamShowOnClient;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.*;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
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

    private final SanPhamService sanPhamService;
    private final HoaDonRepository hoaDonRepository;
    private final GioHangRepository gioHangRepository;
    private final AnhSanPhamService anhSanPhamService;
    private final DiaChiRepository diaChiRepossitory;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamGioHangRepository sanPhamGioHangRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @GetMapping()
    private String index(Model model) {
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient());
        return "client/trangchu";
    }

    @GetMapping("/san_pham_chi_tiet/{id}")
    private String sanPhamChiTiet(@PathVariable int id, Model model) {
        // Lấy danh sách chi tiết sản phẩm theo ID
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findSanPhamChiTietByIdSanPham(id);

        // Sử dụng Set để loại bỏ các kích cỡ trùng lặp
        Set<KichCo> kichCoSet = new HashSet<>();

        // Map màu sắc đến danh sách kích cỡ
        Map<Integer, List<KichCo>> mauSacToKichCoMap = new HashMap<>();

        // Danh sách ảnh sản phẩm chi tiết
        List<AnhSanPhamResponse> anhSanPhamResponseListByChiTiet = new ArrayList<>();

        // Map màu sắc đến ảnh đại diện
        Map<MauSac, AnhSanPhamResponse> mauSacToAnhDaiDienMap = new HashMap<>();

        // Danh sách yêu cầu chi tiết sản phẩm
        List<SanPhamChiTietRequest> spctGia = new ArrayList<>();

        // Xử lý từng sản phẩm chi tiết
        for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
            mauSacToKichCoMap.computeIfAbsent(sanPhamChiTiet.getMauSac().getId(), k -> new ArrayList<>()).add(sanPhamChiTiet.getKichCo());
            kichCoSet.add(sanPhamChiTiet.getKichCo());

            // Lấy ảnh đại diện cho mỗi màu sắc
            anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamChiTiet.getId()).stream().findFirst()
                    .ifPresent(anhDaiDien -> mauSacToAnhDaiDienMap.put(sanPhamChiTiet.getMauSac(), anhDaiDien));

            // Thêm tất cả ảnh chi tiết vào danh sách
            anhSanPhamResponseListByChiTiet.addAll(anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamChiTiet.getId()));

            // Thêm spct và giá vào danh sách
            spctGia.add(SanPhamChiTietRequest.builder()
                    .idSanPham(sanPhamChiTiet.getSanPham().getId())
                    .idKichCo(sanPhamChiTiet.getKichCo().getId())
                    .idMauSac(sanPhamChiTiet.getMauSac().getId())
                    .gia(sanPhamChiTiet.getGia())
                    .soLuong(sanPhamChiTiet.getSoLuong())
                    .trangThai(sanPhamChiTiet.getTrangThai())
                    .build());
        }

        // Sắp xếp kích cỡ
        List<KichCo> sortedKichCoList = new ArrayList<>(kichCoSet);
        sortedKichCoList.sort(Comparator.comparing(KichCo::getTenKichCo));

        // Sắp xếp màu sắc theo tên
        LinkedHashMap<MauSac, AnhSanPhamResponse> sortedMauSacToAnhDaiDienMap = mauSacToAnhDaiDienMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(MauSac::getTenMauSac)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        // Lấy thông tin sản phẩm và ảnh đầu tiên
        SanPhamShowOnClient sanPhamShowOnClient = sanPhamService.getSanPhamShowOnClientById(id);
        AnhSanPhamResponse firstImage = anhSanPhamResponseListByChiTiet.stream().findFirst().orElse(null);

        String anhUrl = sanPhamShowOnClient.getSanPhamResponse().getAnhUrl();

        if (firstImage != null) {
            firstImage.setAnhUrl(anhUrl);
        }

        if (firstImage != null && !anhSanPhamResponseListByChiTiet.contains(firstImage)) {
            anhSanPhamResponseListByChiTiet.add(firstImage);
        }

        // Thêm thông tin vào model
        model.addAttribute("giaBanDau", spctGia.isEmpty() ? null : spctGia.get(0).getGia());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            model.addAttribute("spcts", objectMapper.writeValueAsString(spctGia));
            model.addAttribute("mauSacToKichCoJson", objectMapper.writeValueAsString(mauSacToKichCoMap));
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("idSP", id);
        model.addAttribute("kichCoSet", sortedKichCoList);
        model.addAttribute("mauSacToAnhDaiDienMap", sortedMauSacToAnhDaiDienMap);
        model.addAttribute("anhSanPhamResponseList", anhSanPhamResponseListByChiTiet);
        model.addAttribute("infoSP", sanPhamShowOnClient);
        model.addAttribute("firstImageUrl", firstImage != null ? firstImage.getAnhUrl() : "img/default-image.jpg");

        return "/client/chitiet";
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add_sp_vao_gio_hang/{idSP}")
    private void addSpVaoGioHang(
            @PathVariable(name = "idSP") int idSP, @RequestParam(name = "soluong") Integer soluong,
            @RequestParam(name = "mausac") Integer idMauSac, @RequestParam(name = "kichco") Integer idKichCo,
            HttpServletRequest httpServletRequest, Model model
    ) {
        addSPGH(idSP, idKichCo, idMauSac, soluong);
    }

    @PostMapping("/check-out/{buyNow}")
    private String gioHang(
            @PathVariable(name = "buyNow", required = false) String buyNow, @RequestParam(name = "idSP") Integer idSP,
            @RequestParam(name = "mausac") Integer idMauSac, @RequestParam(name = "kichco") Integer idKichCo,
            @RequestParam(name = "soluong") Integer soluong, Model model
    ) {
        List<SanPhamGioHangCustom> sanPhamGioHangCustomList = new ArrayList<>();
        if (buyNow != null && !buyNow.isEmpty()) {
            SanPhamGioHang sanPhamGioHang = addSPGH(idSP, idKichCo, idMauSac, soluong);
            AnhSanPhamResponse anhSanPhamResponse = anhSanPhamService
                    .getAnhSanPhamByIdSPCT(sanPhamGioHang.getSanPhamChiTiet().getId()).get(0);
            sanPhamGioHangCustomList.add(SanPhamGioHangCustom.builder()
                    .sanPhamGioHang(sanPhamGioHang)
                    .anhUrl(anhSanPhamResponse.getAnhUrl())
                    .build());
        }

        // Calculate total amount
        BigDecimal tongTien = sanPhamGioHangCustomList.stream()
                .map(sanPhamGioHangCustom -> sanPhamGioHangCustom.getSanPhamGioHang().tongTien())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate shipping fee as 10% of the total amount
        BigDecimal phiShip = tongTien.multiply(BigDecimal.valueOf(0.1));

        List<Integer> listIDSPGH = sanPhamGioHangCustomList.stream()
                .map(sanPhamGioHangCustom -> sanPhamGioHangCustom.getSanPhamGioHang().getId())
                .toList();

        model.addAttribute("listSP", sanPhamGioHangCustomList);
        model.addAttribute("phiShip", phiShip);
        model.addAttribute("tongTien", tongTien.subtract(phiShip));
        model.addAttribute("listIDSPGH", listIDSPGH);
        return "/client/check-out";
    }

    @PostMapping("/thanh-toan")
    private void thanhToan(
            @RequestParam(name = "tongTien") BigDecimal tongTien,
            @RequestParam(name = "pttt") Integer pttt,
            @RequestParam(name = "listIDSPGH") String listIDSPGHString,
            Model model
    ) {
        // Parse JSON array string to List<Integer>
        List<Integer> listIDSPGH;
        try {
            listIDSPGH = new ObjectMapper().readValue(listIDSPGHString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid list format for listIDSPGH", e);
        }

        // Fetch the customer (assuming ID 1 for this example)
        KhachHang khachHang = khachHangRepository.findById(1)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));

        // Create a new HoaDon (invoice) object and save it
        HoaDon hoaDon = hoaDonRepository.save(HoaDon.builder()
                .khachHang(khachHang)
                .tenNguoiNhan(khachHang.getTen())
                .sdt(khachHang.getSoDienThoai())
                .emailNguoiNhan(khachHang.getEmail())
                .diaChi(diaChiRepossitory.findByKhachHangId(1).get(0))
                .phuongThucThanhToan(phuongThucThanhToanRepository.findById(pttt)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy phương thức thanh toán")))
                .tongTien(tongTien)
                .trangThai(1)
                .build());

        // Loop through the product IDs and save HoaDonChiTiet (invoice details)
        for (int idSPGH : listIDSPGH) {
            SanPhamGioHang sanPhamGioHang = sanPhamGioHangRepository.findById(idSPGH)
                    .orElseThrow(() -> new DataNotFoundException("Không thể tìm thấy sản phẩm giỏ hàng"));
            sanPhamGioHang.setTrangThai(0);
            sanPhamGioHangRepository.save(sanPhamGioHang);
            hoaDonChiTietRepository.save(HoaDonChiTiet.builder()
                    .gia(sanPhamGioHang.tongTien())
                    .hoaDon(hoaDon)
                    .sanPhamChiTiet(sanPhamGioHang.getSanPhamChiTiet())
                    .soLuong(sanPhamGioHang.getSoLuong())
                    .build());
        }
    }

    @GetMapping("/gio-hang")
    public String gioHang(Model model) {
        KhachHang khachHang = khachHangRepository.findById(1)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId());

        List<Integer> listIDSPGH = new ArrayList<>();
        HashMap<ThuongHieu, List<SanPhamGioHangCustom>> thuongHieuSPGHListHashMap = new HashMap<>();

        sanPhamGioHangRepository.findByGioHangIdAndTrangThai(gioHang.getId(), 1).forEach(sanPhamGioHang -> {
            List<AnhSanPhamResponse> anhSanPhamList = anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamGioHang.getId());
            String anhUrl = anhSanPhamList.isEmpty() ? null : anhSanPhamList.get(0).getAnhUrl();

            SanPhamGioHangCustom sanPhamGioHangCustom = SanPhamGioHangCustom.builder()
                    .sanPhamGioHang(sanPhamGioHang)
                    .anhUrl(anhUrl)
                    .build();

            ThuongHieu thuongHieu = sanPhamGioHang.getSanPhamChiTiet().getSanPham().getThuongHieu();
            listIDSPGH.add(sanPhamGioHang.getId());
            thuongHieuSPGHListHashMap.computeIfAbsent(thuongHieu, k -> new ArrayList<>()).add(sanPhamGioHangCustom);
        });

        Map<ThuongHieu, List<SanPhamGioHangCustom>> sortedMap = thuongHieuSPGHListHashMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.comparing(ThuongHieu::getTenThuongHieu)))
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldValue, newValue) -> oldValue,
                                        LinkedHashMap::new
                                ));
        model.addAttribute("listIDSPGH", listIDSPGH);
        model.addAttribute("thuongHieuSPGHListHashMap", sortedMap);
        return "/client/gio_hang";
    }

    @GetMapping("LG")
    private String LG() {
        return "/client/Login";
    }

    private SanPhamGioHang addSPGH(Integer idSP, Integer idKichCo, Integer idMauSac, Integer soluong) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findSanPhamChiTietBySanPhamIdAndKichCoIdAndMauSacId(idSP, idKichCo, idMauSac);
        sanPhamChiTiet.setSoLuong(soluong);
        KhachHang khachHang = khachHangRepository.findById(1).orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));
        GioHang gioHang = gioHangRepository.findById(1).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng"));
        SanPhamGioHang sanPhamGioHang = SanPhamGioHang.builder()
                .gioHang(gioHang)
                .sanPhamChiTiet(sanPhamChiTiet)
                .soLuong(soluong)
                .trangThai(1)
                .build();
        return sanPhamGioHangRepository.save(sanPhamGioHang);
    }
    @GetMapping("/client")
    public String home(HttpSession session, Model model) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.isEmpty()) {
            return "redirect:/client/LG";
        }
        model.addAttribute("currentUser", currentUser);
        return "/client/trangchu";

    }

}
