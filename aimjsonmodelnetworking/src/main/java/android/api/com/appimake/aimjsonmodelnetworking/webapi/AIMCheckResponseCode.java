package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.AIMIWebAPINotification;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Map;

/**
 * Created by nattapongr on 5/14/15.
 */
public class AIMCheckResponseCode {

    public static int AUTHEN_ERROR = 401;
    public static String TEXT_AUTHEN_ERROR = "Authentication error.\nPlease login again.";

    public static int URL_NOT_FOUND = 404;
    public static String TEXT_URL_NOT_FOUND = "URL not found";

    public static int INTERNAL_SERVER_ERROR = 500;
    public static String TEXT_INTERNAL_SERVER_ERROR = "Internal server error";

    public static int NORMAL = 200;
    public static String TEXT_NORMAL = "SUCCESS";

    public static int DEFAULT_ERROR = 100;
    public static String TEXT_DEFAULT_ERROR = "Default error";

    public static int CONNECTION_ERROR = 0;
    public static String TEXT_CONNECTION_ERROR = "Check your internet connection";

    public static String checkStatus(int statusCode) {
        switch (statusCode) {
            case 200:
                return TEXT_NORMAL;
            case 404:
                return TEXT_URL_NOT_FOUND;
            case 500:
                return TEXT_INTERNAL_SERVER_ERROR;
            default:
                return TEXT_DEFAULT_ERROR;
        }
    }

    public void checkStatusAndCallBack(AIMIWebAPINotification callback, int statusCode, String responseString, Class Class) {
        if (statusCode == 888 || statusCode == 889) { // IMAGE
            if (statusCode == 888)
                callback.webAPISuccess("888", "Success", responseString + "", Class);
            else
                callback.webAPIFailed("889", "Failed", Class);
        } else if (responseString != null) {
            responseString = responseString.replaceAll("\\[\\]", "null");
            responseString = responseString.replaceAll("\"\"", "null");
            responseString = responseString.replace("\\\n", System.getProperty("line.separator"));
            responseString = StringEscapeUtils.unescapeJson(responseString);
            if (statusCode == AIMCheckResponseCode.NORMAL) {
                try {
                    JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
                    Map<String, Object> jsonData = parser.parseJson(responseString);
                    if (jsonData != null) {
                        String stat = jsonData.get("status").toString().trim() + "";
                        switch (stat) {
                            case "104":
                                callback.webAPISuccess(stat, "Approved", responseString + "", Class);
                                break;
                            case "401":
                                callback.webAPISuccess(stat, TEXT_AUTHEN_ERROR, responseString + "", Class);
                                break;
                            default:
                                callback.webAPISuccess(stat, jsonData.get("message") + "", responseString + "", Class);
                                break;
                        }
                    } else {
                        callback.webAPISuccess("200", "", "", Class);
                    }
                } catch (Exception e) {
                    callback.webAPIFailed(DEFAULT_ERROR + "", TEXT_DEFAULT_ERROR, Class);
                }
            } else if (statusCode == AIMCheckResponseCode.URL_NOT_FOUND) {
                callback.webAPIFailed(statusCode + "", TEXT_URL_NOT_FOUND, Class);
            } else if (statusCode == AIMCheckResponseCode.INTERNAL_SERVER_ERROR) {
                callback.webAPIFailed(statusCode + "", TEXT_INTERNAL_SERVER_ERROR, Class);
            } else if (statusCode == AIMCheckResponseCode.CONNECTION_ERROR) {
                callback.webAPIFailed(statusCode + "", TEXT_CONNECTION_ERROR, Class);
            } else {
                callback.webAPIFailed(DEFAULT_ERROR + "", TEXT_NORMAL, Class);
            }
        } else {
            callback.webAPIFailed(CONNECTION_ERROR + "", TEXT_CONNECTION_ERROR, Class);
        }
    }
}