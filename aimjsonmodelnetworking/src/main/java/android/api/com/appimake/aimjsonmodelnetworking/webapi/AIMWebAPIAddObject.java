package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.converter.UnicodeConverter;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Add;
import android.net.Uri;

import java.net.HttpURLConnection;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPIAddObject {
    private static AIMWebAPIAddObject ourInstance = new AIMWebAPIAddObject();
    private Add upload;
    private String url;

    private AIMWebAPIAddObject() {
    }

    public static AIMWebAPIAddObject getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPIAddObject();
        return ourInstance;
    }

    public synchronized void add(String url, String json, String target, String module, String token, final IWebAPINotification callback, final Class Class) {
        this.url = url;
        json = UnicodeConverter.unicodeEscapedString(json);
        upload = new Add();
        upload.updatedate = new AIMDateTime();
        upload.setToken(token);
        upload.setData(json);
        upload.setModule(module);
        upload.setTarget(target);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postUpload(callback, Class);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void postUpload(final IWebAPINotification callback, Class Class) {

        int responseCode = 0;
        String responseString = "";
        String responseStringUnEscape = "";

        HttpURLConnection conn = AIMWebAPIConnection.createHttpConnection(Class.getSimpleName(), url);

        Uri.Builder builder = new Uri.Builder().appendQueryParameter(AIMWebAPI.KEY_PARAM_JSON_HTTP_REQUEST, UnicodeConverter.unicodeEscapedString(upload.toJSONString()));
        String query = builder.build().getEncodedQuery();

        AIMWebAPIConnection.postQueryParameterString(conn, query);

        responseCode = AIMWebAPIConnection.getConnectionResponseCode(Class.getSimpleName(), conn);
        responseString = AIMWebAPIConnection.getConnectionResponseString(Class.getSimpleName(), conn);

        responseStringUnEscape = AIMWebAPIConnection.unEscapeResponseStringWithTrim(responseString);
        responseStringUnEscape = AIMWebAPIConnection.unEscapeResponseString(responseStringUnEscape);

        if (responseStringUnEscape != null)
            new AIMCheckResponseCode().checkStatusAndCallBack(callback, responseCode, responseStringUnEscape, Class);
        else
            callback.webAPIFailed("404", "Time Out !!", Class);
    }
}
