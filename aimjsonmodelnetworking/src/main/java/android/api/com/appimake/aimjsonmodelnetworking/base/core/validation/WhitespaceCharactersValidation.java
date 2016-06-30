package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class WhitespaceCharactersValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "WhitespaceCharacters Validation";
    }

    @Override
    public String getRegExp() {
        return "[\\t\\r\\n\\\\v\\f]+";
    }
}
