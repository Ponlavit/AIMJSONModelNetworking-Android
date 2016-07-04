package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.AIMIWebAPINotification;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Delete;
import android.net.Uri;

import java.net.HttpURLConnection;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPIDeleteObject {
    private static AIMWebAPIDeleteObject ourInstance = new AIMWebAPIDeleteObject();
    private Delete upload;
    private String url;

    private AIMWebAPIDeleteObject() {
    }

    public static AIMWebAPIDeleteObject getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPIDeleteObject();
        return ourInstance;
    }

    public synchronized void delete(String url, String id, String target, String module, String token, final AIMIWebAPINotification callback, final Class Class) {
        this.url = url;
        upload = new Delete();
        upload.setObjectStringId(id);
        upload.setTarget(target);
        upload.setModule(module);
        upload.setToken(token);
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

    private void postUpload(final AIMIWebAPINotification callback, Class Class) {

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

        new AIMCheckResponseCode().checkStatusAndCallBack(callback, responseCode, responseStringUnEscape, Class);
    }
}
