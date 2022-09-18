package process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CustomRunnable<T> {
    void run(T response);

    Logger log = LogManager.getLogger(CustomRunnable.class);
    default void error(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
    }
}
