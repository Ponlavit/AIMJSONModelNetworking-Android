package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.base.AIMConfig;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by NARZTIIZZER on 2/23/2016.
 */
public class AIMWebAPIConnection {

    public static int ERROR_EXCEPTION_RESPONSE_CODE = 9925;

    @Nullable
    public static HttpsURLConnection createHttpsConnection(String url) {
        try {
            URL requestUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) requestUrl.openConnection();
            conn.setReadTimeout(AIMConfig.RequestDefaultTimeout);
            conn.setConnectTimeout(AIMConfig.RequestDefaultTimeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Cache-Control", "no-cache");

            return conn;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static HttpURLConnection createHttpConnection(String tag, String url) {

        try {
            URL requestUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setReadTimeout(AIMConfig.RequestDefaultTimeout);
            conn.setConnectTimeout(AIMConfig.RequestDefaultTimeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Cache-Control", "no-cache");

            Log.d(tag, "CREATE CONNECTION SUCCESS.");
            return conn;
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.d(tag, "CREATE CONNECTION FAIL, CAUSE : " + e.getMessage());
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(tag, "CREATE CONNECTION FAIL, CAUSE : " + e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(tag, "CREATE CONNECTION FAIL, CAUSE : " + e.getMessage());
            return null;
        }
    }

    public static void postQueryParameterString(HttpURLConnection connection, String queryString) {

        try {
            OutputStream outputReader = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputReader, "UTF-8"));
            writer.write(queryString);
            writer.flush();
            writer.close();
            outputReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getConnectionResponseCode(String tag, HttpURLConnection connection) {
        try {
            int responseCode = connection.getResponseCode();
            Log.d(tag, "REQUEST RESPONSE CODE COMPLETE, CODE : " + responseCode);

            return responseCode;
        } catch (IOException e) {
            Log.d(tag, "REQUEST RESPONSE CODE FAIL, CODE : " + ERROR_EXCEPTION_RESPONSE_CODE);
            return ERROR_EXCEPTION_RESPONSE_CODE;
        }
    }

    public static String getConnectionResponseString(String tag, HttpURLConnection connection) {

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }

            inputReader.close();

            Log.d(tag, "REQUEST RESPONSE STRING COMPLETE.");
            return response.toString();
        } catch (IOException e) {
            Log.d(tag, "REQUEST RESPONSE STRING FAIL, CAUSE : " + e.getMessage());
            return e.getMessage();
        }
    }

    public static String unEscapeResponseStringWithTrim(String responseString) {
        if (responseString == null)
            responseString = "";
        return StringEscapeUtils.unescapeJson(responseString.trim());
    }

    public static String unEscapeResponseString(String responseString) {
        return StringEscapeUtils.unescapeJson(responseString);
    }

}
