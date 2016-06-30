package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class AlphanumericCharactersValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "AlphanumericCharacters Validation";
    }

    @Override
    public String getRegExp() {
        return "[A-Za-z0-9]+";
    }
}
