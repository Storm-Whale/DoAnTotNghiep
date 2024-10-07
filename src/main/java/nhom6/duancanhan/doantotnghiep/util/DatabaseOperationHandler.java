package nhom6.duancanhan.doantotnghiep.util;

import org.springframework.dao.DataAccessException;

public class DatabaseOperationHandler {

    @FunctionalInterface
    public interface DatabaseOperation<T> {
        T execute() throws DataAccessException;
    }

    public static <T> T handleDatabaseOperation(DatabaseOperation<T> operation, String errorMessage) {
        try {
            return operation.execute();
        } catch (DataAccessException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }
}
