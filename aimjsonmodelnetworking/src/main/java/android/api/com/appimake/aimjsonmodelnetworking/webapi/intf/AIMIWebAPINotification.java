package android.api.com.appimake.aimjsonmodelnetworking.webapi.intf;

/**
 * Created by nattapongr on 5/12/15.
 */
public interface AIMIWebAPINotification {
    void webAPISuccess(String code, String message, String jsonData, Class Class);

    void webAPIProgress(float sizeUploaded, float fileSize);

    void webAPIFailed(String code, String message, Class Class);
}
