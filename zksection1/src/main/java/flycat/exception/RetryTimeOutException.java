package flycat.exception;

/**
 * <p>Title: RetryTimeOutException</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 4/4/19
 */
public class RetryTimeOutException extends RuntimeException{

    public RetryTimeOutException() {
    }

    public RetryTimeOutException(String message) {
        super(message);
    }

    public RetryTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryTimeOutException(Throwable cause) {
        super(cause);
    }

    public RetryTimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

