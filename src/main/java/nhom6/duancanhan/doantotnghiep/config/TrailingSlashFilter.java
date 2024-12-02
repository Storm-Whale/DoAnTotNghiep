package nhom6.duancanhan.doantotnghiep.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TrailingSlashFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // Kiểm tra nếu URL kết thúc bằng "/" và không phải URL gốc
        if (requestURI.length() > 1 && requestURI.endsWith("/")) {
            // Loại bỏ dấu "/" cuối
            String redirectUrl = requestURI.substring(0, requestURI.length() - 1);

            // Thực hiện redirect
            httpResponse.sendRedirect(redirectUrl);
            return;
        }

        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }
}