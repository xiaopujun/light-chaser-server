package lightchaser.data.provider.base.dto;

import lightchaser.core.base.exception.BaseException;

public class DataProviderException extends BaseException {


    public DataProviderException(String message) {
        super(message);
    }

    public DataProviderException() {

    }

    public DataProviderException(Throwable cause) {
        super(cause);
    }

}
