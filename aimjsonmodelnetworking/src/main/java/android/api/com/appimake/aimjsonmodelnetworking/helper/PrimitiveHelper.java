package android.api.com.appimake.aimjsonmodelnetworking.helper;

/**
 * Created by ponlavitlarpeampaisarl on 2/4/15 AD.
 */
public class PrimitiveHelper {
    /**
     * Change the boolean string representative to actual boolean variable
     *
     * @param booleanString the string representative of boolean
     * @return the boolean format of input data
     */
    public static boolean boolFromString(String booleanString) {
        return booleanString.trim().equalsIgnoreCase("1") ||
                booleanString.trim().equalsIgnoreCase("t") ||
                booleanString.trim().equalsIgnoreCase("true");
    }
}
