package lightchaser.core.base.exception;

public class NotAllowedException extends BaseException {
    public NotAllowedException(String message) {
        super(message);
    }

    public NotAllowedException(String message, int errCode) {
        super(message);
        this.setErrCode(errCode);
    }

    public NotAllowedException() {
        super();
    }

}
