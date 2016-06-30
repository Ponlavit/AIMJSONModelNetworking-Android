package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by nattapongr on 2/27/15.
 */
public class PunctuationCharactersValidation extends Validation {
    @Override
    public String getErrorDescription() {
        return "PunctuationCharacters Validation";
    }

    @Override
    public String getRegExp() {
        return "[\\]\\[!\"#$%&'()*+,./:;<=>?@\\^_`{|}~-]+";
    }
}
