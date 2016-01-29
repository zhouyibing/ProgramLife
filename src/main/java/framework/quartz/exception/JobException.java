package framework.quartz.exception;

/**
 * Created by zhou on 2015/12/29.
 */
public class JobException extends  RuntimeException{
    private static final long serialVersionUID = 3583566093089790852L;

    public JobException() {
        super();
    }

    public JobException(String message) {
        super(message);
    }

    public JobException(Throwable cause) {
        super(cause);
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }
}
