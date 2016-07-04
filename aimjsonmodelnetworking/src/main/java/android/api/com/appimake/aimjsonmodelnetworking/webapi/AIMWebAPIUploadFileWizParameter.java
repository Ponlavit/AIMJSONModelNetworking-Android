package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.authentication.AIMCurrentUser;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.AIMIWebAPINotification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nattapongr on 3/5/2559.
 */
public class AIMWebAPIUploadFileWizParameter {

    private static AIMWebAPIUploadFileWizParameter ourInstance = new AIMWebAPIUploadFileWizParameter();

    private AIMWebAPIUploadFileWizParameter() {
    }

    public static AIMWebAPIUploadFileWizParameter getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMWebAPIUploadFileWizParameter();
        return ourInstance;
    }

    public synchronized void uploadImageToServices(final Class Class, final AIMIWebAPINotification callback, final String urlUpload, final String key, final String name, final String type, final File fileInput) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String status = "";
                String resMessage = null;

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024;
                int resCode = 0;
                BufferedReader br;

                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                try {
                    if (fileInput.exists()) {
                        FileInputStream fileInputStream = new FileInputStream(fileInput);
                        URL url = new URL(urlUpload);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(AIMConfig.RequestDefaultTimeout);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        final DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"key\"" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(key);
                        outputStream.writeBytes(lineEnd);

                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(name + "." + type);
                        outputStream.writeBytes(lineEnd);

                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"token\"" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(AIMCurrentUser.getInstance().getCurrentUser().getToken());
                        outputStream.writeBytes(lineEnd);


                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + name + "." + type + "\"" + lineEnd);
                        outputStream.writeBytes(lineEnd);

                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // Read file
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        while (bytesRead > 0) {
                            final long fileSize = fileInput.length();

                            outputStream.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            final int sizeUpload = outputStream.size();
                            callback.webAPIProgress((float) sizeUpload, (float) fileSize);
                        }

                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Response Code and  Message
                        resCode = conn.getResponseCode();

                        if (resCode == HttpURLConnection.HTTP_OK) {
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } else {
                            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        }

                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        resMessage = sb.toString();
                        status = String.valueOf(resCode);

                        fileInputStream.close();
                        outputStream.flush();
                        outputStream.close();

                        callback.webAPISuccess(AIMCheckResponseCode.NORMAL + "", status, resMessage, Class);
                    }
                } catch (java.net.SocketTimeoutException e) {
                    callback.webAPIFailed("100", e.toString(), Class);
                } catch (Exception ex) {
                    callback.webAPIFailed("100", ex.toString(), Class);
                }
            }
        }).start();
    }
}
