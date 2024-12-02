package nhom6.duancanhan.doantotnghiep.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataNotFoundException.class)
    public ModelAndView handleDataNotFoundException(
            DataNotFoundException ex,
            HttpServletRequest request
    ) {
        logger.error("Data Not Found: {}", request.getRequestURI(), ex);

        // Tạo ModelAndView cho trang lỗi
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorTitle", "Dữ Liệu Không Tồn Tại");
        modelAndView.addObject("errorMessage", "Rất tiếc, dữ liệu bạn yêu cầu không tồn tại.");
        modelAndView.addObject("requestedUrl", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ModelAndView handleDuplicateKeyException(
            DuplicateKeyException ex,
            HttpServletRequest request
    ) {
        logger.error("Duplicate Key Error: {}", request.getRequestURI(), ex);

        // Tạo ModelAndView cho trang lỗi
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorTitle", "Lỗi Trùng Lặp Dữ Liệu");
        modelAndView.addObject("errorMessage", "Rất tiếc, dữ liệu bạn nhập đã bị trùng lặp.");
        modelAndView.addObject("requestedUrl", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.CONFLICT);

        return modelAndView;
    }

    @ExceptionHandler({DataAccessException.class, JpaSystemException.class})
    public ModelAndView handleDatabaseExceptions(
            RuntimeException e,
            HttpServletRequest request
    ) {
        logger.error("Database Error: {}", request.getRequestURI(), e);

        // Tạo ModelAndView cho trang lỗi
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorTitle", "Lỗi Cơ Sở Dữ Liệu");
        modelAndView.addObject("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý dữ liệu.");
        modelAndView.addObject("requestedUrl", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return modelAndView;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.error("Resource Not Found: {}", request.getRequestURI(), ex);

        // Tạo ModelAndView cho trang lỗi
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorTitle", "Trang Không Tồn Tại");
        modelAndView.addObject("errorMessage", "Rất tiếc, trang bạn yêu cầu không tồn tại.");
        modelAndView.addObject("requestedUrl", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);

        return modelAndView;
    }
}

