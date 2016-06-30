package android.api.com.appimake.aimjsonmodelnetworking.base.core.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nattapongr on 2/25/15.
 */
public class RegExp {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String NUMBER_PATTERN = "[0-9.]+";
    private Pattern pattern;
    private Matcher matcher;
    //private static final String NUMBER_PATTEN =

    public boolean RegexpNumber(String string) {
        pattern = Pattern.compile(NUMBER_PATTERN);
        matcher = pattern.matcher(string);
        return matcher.matches();
    }


    public boolean RegexpEmail(String string) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public boolean RegexpPostCode(String string) {
        if (string.matches("[0-9]+"))
            if (string.length() >= 3 && string.length() <= 5)
                return true;
        return false;
    }

    public boolean RegexpIdentificationNumber(String string) {
        if (string.matches("[0-9]+"))
            if (string.length() == 13)
                return true;
        return false;
    }
//    public boolean RegexpFloating(String string){
//        return string.matches("[0-9.]+");
//    }
//    public boolean RegexpAlphanumericCharacters(String string){
//        return string.matches("[A-Za-z0-9]+");
//    }
//    public boolean RegexpNonWordcharacters(String string){
//        return string.matches("[^A-Za-z0-9_]+");
//    }
//    public boolean RegexpAlphabeticCharacters(String string){
//        return string.matches("[A-Za-z]+");
//    }
//    public boolean RegexpSpaceAndTab(String string){
//        return string.matches("[ \\t]+");
//    }
//    public boolean RegexpWordBoundaries(String string){
//        return string.matches("[(?<=\\W)(?=\\w)|(?<=\\w)(?=\\W)]+");
//    }
//    public boolean RegexpDigits(String string){
//        return string.matches("[0-9]+");
//    }
//    public boolean RegexpNonDigits(String string){
//        return string.matches("[^0-9]+");
//    }
//    public boolean RegexpVisibleCharacters(String string){
//        return string.matches("[\\x21-\\x7E]+");
//    }
//    public boolean RegexpLowercaseLetters(String string){
//        return string.matches("[a-z]+");
//    }
//    public boolean RegexpVisibleCharactersAndTheSpaceCharacter(String string){
//        return string.matches("[\\x20-\\x7E]+");
//    }
//    public boolean RegexpPunctuationCharacters(String string){
//        return string.matches("[\\]\\[!\"#$%&'()*+,./:;<=>?@\\^_`{|}~-]+");
//    }
//    public boolean RegexpWhitespaceCharacters(String string){
//        return string.matches("[\\t\\r\\n\\\\v\\f]+");
//    }
//    public boolean RegexpNonWhitespaceCharacters(String string){
//        return string.matches("[^ \\t\\r\\n\\\\v\\f]+");
//    }
//    public boolean RegexpUppercaseLetters(String string){
//        return string.matches("[A-Z]+");
//    }
//    public boolean RegexpHexadecimalDigits(String string){
//        return string.matches("[A-Fa-f0-9]+");
//    }
}
