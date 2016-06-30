package android.api.com.appimake.aimjsonmodelnetworking.base.core.converter;

import java.security.MessageDigest;

/**
 * Created by nattapongr on 5/14/15.
 */
public class MD5Converter {
    public static String StringToMD5(String args) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(args.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString().trim();
    }
}
