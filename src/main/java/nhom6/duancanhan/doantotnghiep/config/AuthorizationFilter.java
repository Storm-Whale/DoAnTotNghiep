package nhom6.duancanhan.doantotnghiep.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizationFilter implements Filter {

    // Danh sách các URL công khai (không yêu cầu xác thực)
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/login",      // Trang đăng nhập
            "/register",   // Trang đăng ký
            "/public",     // Các trang công khai
            "/css/",       // Tài nguyên CSS
            "/js/",        // Tài nguyên JavaScript
            "/images/"     // Tài nguyên hình ảnh
    );

    // Danh sách các URL được phép truy cập có điều kiện
    private static final List<String> ALLOWED_URLS = Arrays.asList(
            "/client/gio-hang",           // Trang giỏ hàng
            "/client/check-out",           // Trang thanh toán
            "/client/thanh-toan",          // Trang thanh toán
            "/client/vnpay-payment",       // Trang thanh toán VNPay
            "/client/showInfoCustomer",    // Trang thông tin khách hàng
            "/client/showInfoAddress"      // Trang thông tin địa chỉ
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();

        // Kiểm tra URL công khai
        boolean isPublicUrl = PUBLIC_URLS.stream().anyMatch(requestURI::contains);
        if (isPublicUrl) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra URL được phép (không yêu cầu role)
        boolean isAllowedUrl = ALLOWED_URLS.stream().anyMatch(requestURI::startsWith);
        if (isAllowedUrl) {
            if (session == null || session.getAttribute("user") == null) {
                saveRequestedUrl(httpRequest, requestURI);
                httpResponse.sendRedirect("/login/login-client");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra URL bắt đầu bằng "/admin/"
        if (requestURI.startsWith("/admin/")) {
            // Kiểm tra xem session có hợp lệ không
            if (session == null || session.getAttribute("nhanvien") == null) {
                saveRequestedUrl(httpRequest, requestURI);
                httpResponse.sendRedirect("/login/login-client");
                return;
            }

            // Kiểm tra quyền truy cập (role = 1)
            Object role = session.getAttribute("role");
            if (role == null || "3".equals(role.toString())) {
                // Nếu không có quyền, chuyển hướng đến trang không đủ quyền
                httpResponse.sendRedirect("/error/unauthorized");
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }


    // Phương thức lưu URL được yêu cầu
    private void saveRequestedUrl(HttpServletRequest request, String requestURI) {
        // Tạo session nếu chưa tồn tại
        HttpSession session = request.getSession(true);
        // Lưu URL được yêu cầu vào session
        session.setAttribute("requestedUrl", requestURI);
    }
}
