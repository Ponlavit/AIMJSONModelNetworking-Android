package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class SpaceAndTabValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "SpaceAndTab Validation";
    }

    @Override
    public String getRegExp() {
        return "[ \\t]+";
    }
}
