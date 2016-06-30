package android.api.com.appimake.aimjsonmodelnetworking.base.core.exception;

/**
 * Created by ponlavitlarpeampaisarl on 2/4/15 AD.
 */
public class UnsupportedClassException extends RuntimeException {

    String message;

    public UnsupportedClassException() {
        this.message = super.getMessage();
    }

    public UnsupportedClassException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
