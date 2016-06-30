package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class HexadecimalDigitsValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "HexadecimalDigits Validation";
    }

    @Override
    public String getRegExp() {
        return "[A-Fa-f0-9]+";
    }
}
