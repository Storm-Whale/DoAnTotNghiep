//package nhom6.duancanhan.doantotnghiep.util;
//
//import lombok.RequiredArgsConstructor;
//import nhom6.duancanhan.doantotnghiep.service.service.ChatLieuService;
//import nhom6.duancanhan.doantotnghiep.service.service.KichCoService;
//import nhom6.duancanhan.doantotnghiep.service.service.KieuCoAoService;
//import nhom6.duancanhan.doantotnghiep.service.service.KieuTayAoService;
//import nhom6.duancanhan.doantotnghiep.service.service.MauSacService;
//import nhom6.duancanhan.doantotnghiep.service.service.ThuongHieuService;
//import org.springframework.ui.Model;
//
//@RequiredArgsConstructor
//public class ModelAttribute {
//
//    private final ThuongHieuService thuongHieuService;
//    private final ChatLieuService chatLieuService;
//    private final KieuCoAoService kieuCoAoService;
//    private final KieuTayAoService kieuTayAoService;
//
//    private final MauSacService mauSacService;
//
//    private final KichCoService kichCoService;
//
//    public void modelAttribute(Model model) {
//        model.addAttribute("mauSac", mauSacService.getAll());
//        model.addAttribute("kichCo", kichCoService.getAll());
//        model.addAttribute("thuongHieu", thuongHieuService.getAll());
//        model.addAttribute("chatLieu", chatLieuService.getAll());
//        model.addAttribute("tayAo", kieuTayAoService.getAll());
//        model.addAttribute("coAo", kieuCoAoService.getAll());
//    }
//}
