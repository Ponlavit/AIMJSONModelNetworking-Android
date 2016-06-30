package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Update;
import android.net.Uri;

import java.net.HttpURLConnection;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPIUpdateObject {
    private static AIMWebAPIUpdateObject ourInstance = new AIMWebAPIUpdateObject();
    private Update upload;
    private String url;

    private AIMWebAPIUpdateObject() {
    }

    public static AIMWebAPIUpdateObject getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPIUpdateObject();
        return ourInstance;
    }

    public synchronized void update(String url, String id, String data, String target, String module, String token, final IWebAPINotification callback, final Class Class) {
        this.url = url;
        upload = new Update();
        upload.setObjectStringId(id);
        upload.setData(data);
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

    private void postUpload(final IWebAPINotification callback, Class Class) {

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
