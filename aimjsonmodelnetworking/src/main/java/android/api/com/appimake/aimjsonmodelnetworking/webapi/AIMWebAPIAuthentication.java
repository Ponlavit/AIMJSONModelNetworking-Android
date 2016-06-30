package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.authentication.models.AIMAuthen;
import android.api.com.appimake.aimjsonmodelnetworking.base.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.converter.UnicodeConverter;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.net.Uri;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPIAuthentication {
    private static AIMWebAPIAuthentication ourInstance = new AIMWebAPIAuthentication();
    private Timer tm;
    private String json, url;
    private AIMAuthen upload;

    private AIMWebAPIAuthentication() {
    }

    public static AIMWebAPIAuthentication getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPIAuthentication();
        return ourInstance;
    }

    public synchronized void authen(String url, final AIMAuthen info, final IWebAPINotification callback, final Class Class) {
        this.url = url;
        json = UnicodeConverter.unicodeEscapedString(info.toJSONLocalString());
        upload = info;
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

        timeOut(callback, Class);

        int responseCode = 0;
        String responseString = "";
        String responseStringUnEscape = "";

        HttpURLConnection conn = AIMWebAPIConnection.createHttpConnection(Class.getSimpleName(), url);

        Uri.Builder builder = new Uri.Builder().appendQueryParameter(AIMWebAPI.KEY_PARAM_JSON_HTTP_REQUEST, upload.toJSONString());
        String query = builder.build().getEncodedQuery();

        AIMWebAPIConnection.postQueryParameterString(conn, query);

        responseCode = AIMWebAPIConnection.getConnectionResponseCode(Class.getSimpleName(), conn);
        responseString = AIMWebAPIConnection.getConnectionResponseString(Class.getSimpleName(), conn);
        responseStringUnEscape = AIMWebAPIConnection.unEscapeResponseStringWithTrim(responseString);

        tm.cancel();
        if (responseStringUnEscape != null)
            new AIMCheckResponseCode().checkStatusAndCallBack(callback, responseCode, responseString, Class);
        else
            callback.webAPIFailed("404", "Check your internet connection", Class);
    }

    private void timeOut(final IWebAPINotification callback, final Class Class) {
        tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                new AIMCheckResponseCode().checkStatusAndCallBack(callback, 0, "", Class);
            }
        }, AIMConfig.RequestDefaultTimeout, AIMConfig.RequestDefaultTimeout);
    }
}
