package nhom6.duancanhan.doantotnghiep.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Set cache-control headers
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setHeader("Expires", "0");

        // Check if URL ends with "/" and is not the root URL
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.length() > 1 && requestURI.endsWith("/")) {
            // Remove the trailing "/"
            String redirectUrl = requestURI.substring(0, requestURI.length() - 1);

            // Perform redirect
            httpResponse.sendRedirect(redirectUrl);
            return;
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}