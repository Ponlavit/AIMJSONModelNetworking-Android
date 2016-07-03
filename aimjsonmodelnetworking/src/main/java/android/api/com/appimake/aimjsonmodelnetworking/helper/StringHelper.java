package android.api.com.appimake.aimjsonmodelnetworking.helper;

/**
 * Created by ponlavitlarpeampaisarl on 2/4/15 AD.
 */
public class StringHelper {

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

    public static String unicodeEscapedString(String stringToEncode) {

        String st = "";
        for (int i = 0; i < stringToEncode.length(); i++)
            st += unicodeEscaped(stringToEncode.charAt(i));
        return st;
    }

    public static String objToString(Object input, String defaultString) {
        if (input == null || input.equals("null"))
            return defaultString;
        return input.toString().trim();
    }

    public static String nullAbleString(String input) {
        if (input == null || input.equals("") || input.equals("null")) return null;
        return input;
    }

    public static String formatJSONString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString().replaceAll("\\\\", "");
    }
}
