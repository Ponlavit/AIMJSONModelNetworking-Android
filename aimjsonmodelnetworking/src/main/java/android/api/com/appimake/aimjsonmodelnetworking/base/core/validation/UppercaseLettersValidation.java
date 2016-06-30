package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class UppercaseLettersValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "UppercaseLetters Validation";
    }

    @Override
    public String getRegExp() {
        return "[A-Z]+";
    }
}
