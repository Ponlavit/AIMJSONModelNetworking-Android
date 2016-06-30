package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

/**
 * Created by Nattapongr on 28/5/2559.
 */
public class TxtENTHAndNumberOnly extends Validation {
    @Override
    public String getErrorDescription() {
        return "TxtENTHAndNumberOnly";
    }

    @Override
    public String getRegExp() {
        return "^[a-zA-Z0-9ก-๙\\s\\n]+$";
    }
}
