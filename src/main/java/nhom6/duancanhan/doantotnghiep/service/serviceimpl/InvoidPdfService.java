package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class InvoidPdfService {
    public byte[] generateInvoicePdf(HoaDon hoaDon) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        //
//        String fontPath = "D:/WorkPlace/Java/DuAnTotNghiep/DoAnTotNghiep/src/main/resources/times.ttf";
        // Thay bằng đường dẫn tới file font hỗ trợ Unicode (VD: Times New Roman)
      String fontPath = "D:/DoAnTotNghiep/DoAnTotNghiep/src/main/resources/times.ttf";

      // Thay bằng đường dẫn tới file font hỗ trợ Unicode (VD: Times New Roman)
//      String fontPath = "D:/FALL_2024/DATN/DoAnTotNghiep/src/main/resources/times.ttf"; // Thay bằng đường dẫn tới file font hỗ trợ Unicode (VD: Times New Roman)
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        document.setFont(font);

        // Thêm tiêu đề hóa đơn
        document.add(new Paragraph("YAGI SHOP")
                .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        document.add(new LineSeparator(new SolidLine(1)));
        document.add(new Paragraph("Số điện thoại: 0983123329")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Email: yagishop1612@gmail.com")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Địa chỉ: Cổng số 1, Tòa nhà FPT Polytechnic, 13 phố Trịnh Văn Bô, phường Phuơng Canh, quận Nam Từ Liêm, TP Hà Nội")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new LineSeparator(new SolidLine(1)));
        document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Mã Hóa Đơn: HD" + hoaDon.getId()).setBold());
        LocalDateTime ngaySua = hoaDon.getNgaySua(); // Lấy ngày sửa
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); // Định dạng ngày giờ

        String formattedDate = ngaySua.format(formatter); // Định dạng ngày giờ

// Thêm vào tài liệu
        document.add(new Paragraph("Ngày Lập: " + formattedDate).setBold());
        String tenKhachHang = "Khách lẻ";
        String soDienThoai = "";
        if (hoaDon.getKhachHang() != null) {
            tenKhachHang = hoaDon.getKhachHang().getTen() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ";
            soDienThoai = hoaDon.getKhachHang().getSoDienThoai() != null ? hoaDon.getKhachHang().getSoDienThoai() : "";
        }
//        document.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getTen()));
        document.add(new Paragraph("Khách hàng: " + tenKhachHang).setBold());
        document.add(new Paragraph("Số Điện Thoại: " + soDienThoai).setBold());
        document.add(new Paragraph("Nhân viên: " + hoaDon.getNguoiTao().getTen()).setBold());
        document.add(new Paragraph("DANH SÁCH SẢN PHẨM MUA HÀNG")
                .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {1, 3, 2, 2, 2, 2, 3, 3}; // Tỉ lệ chiều rộng các cột
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Đặt độ rộng bảng chiếm 100% chiều ngang PDF
        table.setTextAlignment(TextAlignment.CENTER); // Căn giữa nội dung trong bảng
        table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Căn giữa bảng trong tài liệu

// Thêm bảng sản phẩm
        table.addHeaderCell(new Cell().add(new Paragraph("STT").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Tên Sản Phẩm").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Màu Sắc").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Kích Cỡ").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Chất Liệu").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Số Lượng").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Đơn Giá").setFont(font).setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Tổng Tiền").setFont(font).setBold()));

        int index = 1;
        BigDecimal tongTien = BigDecimal.ZERO ;
        for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTietList()) {
            tongTien = tongTien.add(chiTiet.tongTien());
            table.addCell(new Cell().add(new Paragraph(String.valueOf(index++)).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(chiTiet.getTenSanPham()).setFont(font)).setMinWidth(50)); // Đặt chiều rộng tối thiểu
            table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac())).setFont(font)).setMinWidth(30));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSanPhamChiTiet().getKichCo().getTenKichCo())).setFont(font)).setMinWidth(30));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSanPhamChiTiet().getSanPham().getChatLieu().getTenChatLieu())).setFont(font)).setMinWidth(30));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSoLuong())).setFont(font)).setMinWidth(30));
            table.addCell(new Cell().add(new Paragraph(formatCurrency(chiTiet.getSanPhamChiTiet().getGia())).setFont(font)).setMinWidth(30));
            table.addCell(new Cell().add(new Paragraph(formatCurrency(chiTiet.tongTien())).setFont(font)).setMinWidth(50));


        }
// Tổng tiền từ hóa đơn
        BigDecimal hoaDonTongTien = hoaDon.getTongTien();

// Tính số tiền giảm giá
        BigDecimal soTienGiamGia = tongTien.subtract(hoaDonTongTien);
// Tổng tiền
        document.add(table);
//        document.add(new Paragraph("\nTỔNG TIỀN: " + hoaDon.getTongTien()).setBold());
        document.add(new Paragraph("\nTỔNG TIỀN : " + formatCurrency(tongTien)).setFont(font).setBold());

        document.add(new Paragraph("\n SỐ TIỀN GIẢM GIÁ: " + formatCurrency(soTienGiamGia)).setFont(font).setBold());

        document.add(new Paragraph("\nTỔNG TIỀN KHÁCH TRẢ: " + formatCurrency(hoaDon.getTongTien())).setFont(font).setBold());

        document.add(new Paragraph("****** Cảm ơn quý khách! ******")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.close();
        return out.toByteArray();
    }
    public String formatCurrency(BigDecimal value) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormatter.format(value);
    }
}
