package nhom6.duancanhan.doantotnghiep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamChiTietRequest;
import nhom6.duancanhan.doantotnghiep.dto.SanPhamShowOnClient;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final SanPhamService sanPhamService;
    private final AnhSanPhamService anhSanPhamService;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final GioHangRepository gioHangRepository;
    private final SanPhamGioHangRepository sanPhamGioHangRepository;

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

        // Kiểm tra và thêm đuôi nếu cần thiết
        if (anhUrl != null && !anhUrl.isEmpty()) {
            if (!anhUrl.matches(".*\\.(png|jpg|jpeg|gif)$")) {
                anhUrl += ".png"; // Hoặc thay bằng định dạng mà bạn muốn
            }
        }

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


    @PostMapping("/add_sp_vao_gio_hang/{idSP}")
    @ResponseStatus(HttpStatus.OK)
    private void addSpVaoGioHang(
            @PathVariable(name = "idSP") int idSP,
            @RequestParam(name = "soluong") Integer soluong,
            @RequestParam(name = "mausac") Integer idMauSac,
            @RequestParam(name = "kichco") Integer idKichCo,
            HttpServletRequest httpServletRequest,
            Model model
    ) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findSanPhamChiTietBySanPhamIdAndKichCoIdAndMauSacId(idSP, idKichCo, idMauSac);
        sanPhamChiTiet.setSoLuong(soluong);
        KhachHang khachHang = khachHangRepository.findById(1).orElseThrow(() -> new DataNotFoundException("Không tìm thấy khách hàng"));
        GioHang gioHang = gioHangRepository.findById(1).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng"));
        SanPhamGioHang sanPhamGioHang = SanPhamGioHang.builder()
                .gioHang(gioHang)
                .sanPhamChiTiet(sanPhamChiTiet)
                .soLuong(soluong)
                .build();
        sanPhamGioHangRepository.save(sanPhamGioHang);
    }

    @GetMapping("/gio-hang")
    private String gioHang(Model model) {
        return "/client/gio-hang";
    }
}
