package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class EmailValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "Email Validation";
    }

    @Override
    public String getRegExp() {
        return "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    }
}
