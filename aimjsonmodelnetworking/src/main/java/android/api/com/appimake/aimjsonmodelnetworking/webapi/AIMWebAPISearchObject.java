package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.base.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Search;
import android.net.Uri;
import android.util.Log;

import java.net.HttpURLConnection;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPISearchObject {

    private static AIMWebAPISearchObject ourInstance = new AIMWebAPISearchObject();

    private AIMWebAPISearchObject() {
    }

    public synchronized static AIMWebAPISearchObject getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPISearchObject();
        return ourInstance;
    }

    public void search(final String url, AIMDateTime lastUpdate, String keyword, String target, String module, int limit, int offset, String token, final IWebAPINotification callback, final Class Class) {
        search(url, lastUpdate, keyword, target, module, limit, offset, token, callback, Class, 10);
    }

    public void search(final String url, AIMDateTime lastUpdate, String keyword, final String target, final String module, int limit, int offset, String token, final IWebAPINotification callback, final Class Class, final long numberOfDayCache) {
        final Search upload = new Search();
        upload.updatedate = lastUpdate;
        upload.setKeyword(keyword);
        upload.setTarget(target);
        upload.setModule(module);
        upload.setLimit(limit);
        upload.setOffset(offset);
        upload.setToken(token);

        Log.d("SEARCHING OBJECT", "SEARCHING " + Class.getName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(module + " : " + target, upload.toJSONString() + "");
                    postUpload(callback, Class, url, upload, AIMConfig.DAY * numberOfDayCache);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void postUpload(final IWebAPINotification callback, Class Class, String url, Search upload, long numberOfDayCache) {

        int responseCode = 0;
        String responseString = "";

        HttpURLConnection conn = AIMWebAPIConnection.createHttpConnection(Class.getSimpleName(), url);

        Uri.Builder builder = new Uri.Builder().appendQueryParameter(AIMWebAPI.KEY_PARAM_JSON_HTTP_REQUEST, upload.toJSONString());
        String query = builder.build().getEncodedQuery();

        AIMWebAPIConnection.postQueryParameterString(conn, query);

        responseCode = AIMWebAPIConnection.getConnectionResponseCode(Class.getSimpleName(), conn);
        responseString = AIMWebAPIConnection.getConnectionResponseString(Class.getSimpleName(), conn);

        if (responseCode != 0) {
            new AIMCheckResponseCode().checkStatusAndCallBack(callback, responseCode, responseString, Class);
        }
    }
}