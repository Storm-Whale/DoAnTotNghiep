package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import nhom6.duancanhan.doantotnghiep.entity.HoaDon;
import nhom6.duancanhan.doantotnghiep.entity.HoaDonChiTiet;
import org.springframework.stereotype.Service;

import javax.swing.text.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class InvoidPdfService {
    public byte[] generateInvoicePdf(HoaDon hoaDon) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Thêm tiêu đề hóa đơn
        document.add(new Paragraph("HÓA ĐƠN THANH TOÁN SHOP YAGI")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Mã hóa Đơn: " + hoaDon.getId()));
        document.add(new Paragraph("Ngày Lập: " + hoaDon.getNgaySua()));
        String tenKhachHang = "Khách lẻ";
        String soDienThoai = "";
        if (hoaDon.getKhachHang() != null) {
            tenKhachHang = hoaDon.getKhachHang().getTen() != null ? hoaDon.getKhachHang().getTen() : "Khách Lẻ";
            soDienThoai = hoaDon.getKhachHang().getSoDienThoai() != null ? hoaDon.getKhachHang().getSoDienThoai() : "";
        }
//        document.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getTen()));
        document.add(new Paragraph("Khách hàng: " + tenKhachHang));
        document.add(new Paragraph("SỐ Điện Thoại: " + soDienThoai));

        // Thêm bảng sản phẩm
        Table table = new Table(4);
        table.addHeaderCell("STT");
        table.addHeaderCell("Tên Sản Phẩm");
        table.addHeaderCell("SỐ Lượng");
        table.addHeaderCell("ĐƠN giá");

        int index = 1;
        for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTietList()) {
            table.addCell(String.valueOf(index++));
            table.addCell(chiTiet.getTenSanPham());
            table.addCell(String.valueOf(chiTiet.getSoLuong()));
            table.addCell(chiTiet.tongTien().toString());
        }

        // Tổng tiền
        document.add(table);
        document.add(new Paragraph("\nTỔNG TIỀN: " + hoaDon.getTongTien()).setBold());

        document.close();
        return out.toByteArray();
    }
}
