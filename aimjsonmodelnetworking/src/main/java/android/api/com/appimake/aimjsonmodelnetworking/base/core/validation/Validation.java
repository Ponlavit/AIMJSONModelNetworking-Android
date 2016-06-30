package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nattapongr on 2/27/15.
 */
public abstract class Validation {
    private Pattern pattern;
    private Matcher matcher;

    public boolean validate(String input) {
        pattern = Pattern.compile(getRegExp());
        matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public abstract String getErrorDescription();

    public abstract String getRegExp();
}
