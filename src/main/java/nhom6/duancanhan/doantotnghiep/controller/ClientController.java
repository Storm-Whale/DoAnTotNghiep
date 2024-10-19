package nhom6.duancanhan.doantotnghiep.controller;

import lombok.RequiredArgsConstructor;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import nhom6.duancanhan.doantotnghiep.entity.*;
import nhom6.duancanhan.doantotnghiep.exception.DataNotFoundException;
import nhom6.duancanhan.doantotnghiep.repository.GioHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.KhachHangRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamChiTietRepository;
import nhom6.duancanhan.doantotnghiep.repository.SanPhamGioHangRepository;
import nhom6.duancanhan.doantotnghiep.service.service.AnhSanPhamService;
import nhom6.duancanhan.doantotnghiep.service.service.SanPhamService;
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

    @GetMapping("")
    private String trangchuindex(Model model) {
        model.addAttribute("sanphams", sanPhamService.getAllSanPhamShowOnClient());
        return "/client/trangchu";
    }

    @GetMapping("/san_pham_chi_tiet/{id}")
    private String sanPhamChiTiet(@PathVariable int id, Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findSanPhamChiTietByIdSanPham(id);
        Set<KichCo> kichCoSet = new HashSet<>();
        List<List<AnhSanPhamResponse>> anhSanPhamResponseListByChiTiet = new ArrayList<>();
        HashMap<MauSac, AnhSanPhamResponse> mauSacToAnhDaiDienMap = new HashMap<>();

        sanPhamChiTietList.forEach(sanPhamChiTiet -> {
            kichCoSet.add(sanPhamChiTiet.getKichCo());
            mauSacToAnhDaiDienMap.put(sanPhamChiTiet.getMauSac(), anhSanPhamService.getAnhSanPhamByIdSPCT(id).get(0));
            anhSanPhamResponseListByChiTiet.add(anhSanPhamService.getAnhSanPhamByIdSPCT(sanPhamChiTiet.getId()));
        });

        List<KichCo> sortedKichCoList = new ArrayList<>(kichCoSet);
        sortedKichCoList.sort(Comparator.comparing(KichCo::getTenKichCo));
        LinkedHashMap<MauSac, AnhSanPhamResponse> sortedMauSacToAnhDaiDienMap = mauSacToAnhDaiDienMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(MauSac::getTenMauSac)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));

        model.addAttribute("idSP", id);
        model.addAttribute("kichCoSet", sortedKichCoList);
        model.addAttribute("mauSacToAnhDaiDienMap", sortedMauSacToAnhDaiDienMap);
        model.addAttribute("anhSanPhamResponseList", anhSanPhamResponseListByChiTiet);
        return "/client/chitiet";
    }

    @PostMapping("/add_sp_vao_gio_hang/{idSP}")
    private String addSpVaoGioHang(
            @PathVariable(name = "idSP") int idSP,
            @RequestParam(name = "soluong") Integer soluong,
            @RequestParam(name = "mausac") Integer idMauSac,
            @RequestParam(name = "kichco") Integer idKichCo,
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
        return "redirect:/client";
    }
    @GetMapping("hientuong")
    private String trangct2() {
        return "/client/hientuong";
    }
}
