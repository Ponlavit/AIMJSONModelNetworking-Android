package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.authentication.AIMCurrentUser;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.AIMIWebAPINotification;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nattapongr on 5/13/15.
 */
public class AIMWebAPISentFile {

    private static AIMWebAPISentFile ourInstance = new AIMWebAPISentFile();

    private AIMWebAPISentFile() {
    }

    public synchronized static AIMWebAPISentFile getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPISentFile();
        return ourInstance;
    }

    public void sent(String key, String url, File file, String name, String type, final AIMIWebAPINotification callback, final Class Class) {
        sent(key, url, file, name, type, callback, Class, 10);
    }

    public void sent(final String key, final String url, final File file, final String name, final String type, final AIMIWebAPINotification callback, final Class Class, final long numberOfDayCache) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postUpload(key, file, name, type, callback, Class, url, AIMConfig.DAY * numberOfDayCache);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void postUpload(String key, File file, String name, String type, final AIMIWebAPINotification callback, Class Class, String urlS, long numberOfDayCache) {

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            /** Check file on SD Card ***/
            if (!file.exists()) {
            }

            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            DataOutputStream outputStream = new DataOutputStream(conn
                    .getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream
                    .writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\""
                            + name + "." + type + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream
                    .writeBytes("key=" + key + "&token=" + AIMCurrentUser.getInstance().getCurrentUser().getToken() + "&image="
                            + name + "." + type + lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Response Code and  Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                resMessage = new String(result);

            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            new AIMCheckResponseCode().checkStatusAndCallBack(callback, 888, resMessage.toString(), Class);
        } catch (Exception ex) {
            new AIMCheckResponseCode().checkStatusAndCallBack(callback, 889, resMessage.toString(), Class);
        }
    }
}
