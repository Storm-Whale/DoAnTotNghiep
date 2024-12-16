package nhom6.duancanhan.doantotnghiep.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Integer role = (Integer) session.getAttribute("role");
        String requestURI = request.getRequestURI();
        // Nếu là nhân viên (role 2)
        if (role != null && role == 2) {
            // Danh sách các đường dẫn được phép truy cập
            List<String> allowedPaths = Arrays.asList(
                    "/admin/hoadon",
                    "/admin/taiquay",
                    "/admin/hoadon/",
                    "/admin/taiquay/"
            );
            // Kiểm tra xem đường dẫn hiện tại có trong danh sách được phép không
            boolean isAllowedPath = allowedPaths.stream()
                    .anyMatch(requestURI::startsWith);

            if (!isAllowedPath) {
                // Chuyển hướng hoặc hiển thị thông báo
                response.sendRedirect("/access-denied");
                return false;
            }
        }
        return true;
    }
}
