package android.api.com.appimake.aimjsonmodelnetworking.base.core.converter;

/**
 * Created by nattapongr on 5/13/15.
 */
public class UnicodeConverter {

    /**
     * Converts the string to the unicode format '\u0020'.
     *
     * This format is the Java source code format.
     *
     * <pre>
     *   CharUtils.unicodeEscaped(' ') = "\u0020"
     *   CharUtils.unicodeEscaped('A') = "\u0041"
     * </pre>
     *
     * @param ch the character to convert
     * @return the escaped unicode string
     */
    public static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    /**
     * Converts the string to the unicode format '\u0020'.
     *
     * This format is the Java source code format.
     *
     * If <code>null</code> is passed in, <code>null</code> will be returned.
     *
     * <pre>
     *   CharUtils.unicodeEscaped(null) = null
     *   CharUtils.unicodeEscaped(' ')  = "\u0020"
     *   CharUtils.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch the character to convert, may be null
     * @return the escaped unicode string, null if null input
     */
    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }

    public static String unicodeEscapedString(String slaSolution) {

        String st = "";
        for (int i = 0; i < slaSolution.length(); i++)
            st += unicodeEscaped(slaSolution.charAt(i));
        return st;
    }
}
