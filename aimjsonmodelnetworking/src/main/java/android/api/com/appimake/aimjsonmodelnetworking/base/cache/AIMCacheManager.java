package android.api.com.appimake.aimjsonmodelnetworking.base.cache;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by nattapongr on 5/8/15.
 */
public class AIMCacheManager {
    private static AIMCacheManager ourInstance;
    private final String BEGINDATE = "BeginDate";
    private final String EXPIREDATE = "ExpireDate";
    private SharedPreferences sp;
    private FileOutputStream fos;
    private FileInputStream fis;
    private Context mContext;
    private boolean isInitial;

    private AIMCacheManager() {
    }

    public static AIMCacheManager getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMCacheManager();
        return ourInstance;
    }

    public void initial(Context context) {
        this.mContext = context;
        this.isInitial = true;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public synchronized AIMDateTime getBeginDateForKey(String key) {
        sp = mContext.getSharedPreferences("KEY" + key.hashCode(), Context.MODE_MULTI_PROCESS);
        return new AIMDateTime(sp.getLong(BEGINDATE, 0) / 1000);
    }

    public synchronized AIMDateTime getExpireDateForKey(String key) {
        sp = mContext.getSharedPreferences("KEY" + key.hashCode(), Context.MODE_MULTI_PROCESS);
        return new AIMDateTime(sp.getLong(EXPIREDATE, 0) / 1000);
    }

    public synchronized long getDateLeftForKey(String key) {
        sp = mContext.getSharedPreferences("KEY" + key.hashCode(), Context.MODE_MULTI_PROCESS);
        return (sp.getLong(EXPIREDATE, 0) - Calendar.getInstance().getTime().getTime()) / 1000;
    }

    public boolean clearCacheForKey(String key) {
        return mContext.deleteFile(key.hashCode() + "");
    }

    private synchronized void setDate(String key, long timeInterval) {
        long now = Calendar.getInstance().getTime().getTime();
        sp = mContext.getSharedPreferences("KEY" + key.hashCode(), Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(BEGINDATE, now);
        editor.putLong(EXPIREDATE, now + timeInterval);
        editor.apply();
    }

    public synchronized void setCacheStringForKey(String key, String obj, long timeInterval) {
        try {
            mContext.deleteFile(key.hashCode() + "");
            fos = mContext.openFileOutput(key.hashCode() + "", Context.MODE_MULTI_PROCESS);
            try {
                fos.write(Base64.encodeToString(obj.getBytes(), 1).getBytes());
                fos.close();
                setDate(key, timeInterval * 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isDataForKeyAvaliableAndNotExpire(String key) {
        if (getDateLeftForKey(key) < 0) {
            clearCacheForKey(key);
        }
        try {
            fis = mContext.openFileInput(key.hashCode() + "");
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public synchronized String getCacheStringForKey(String key) {
        if (getDateLeftForKey(key) < 0) {
            clearCacheForKey(key);
        }
        StringBuilder fileContent = new StringBuilder("");
        try {
            fis = mContext.openFileInput(key.hashCode() + "");
            byte[] buffer = new byte[1024];
            int n;
            try {
                while ((n = fis.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        String strReturn;
        strReturn = new String(Base64.decode(fileContent.toString().trim().trim().getBytes(), 0));
        return strReturn.replaceAll("\\\\", "");
    }

    public synchronized void setCacheBitmapForKey(String key, Bitmap bitmap, long timeInterval) {

        try {
            fos = mContext.openFileOutput(key.hashCode() + "", Context.MODE_MULTI_PROCESS);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                fos.write(Base64.encodeToString(byteArray, 1).getBytes());
                fos.close();
                setDate(key, timeInterval);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public synchronized Bitmap getCacheBitmapForKey(String key) {
        StringBuilder fileContent = new StringBuilder("");
        try {
            fis = mContext.openFileInput(key.hashCode() + "");
            byte[] buffer = new byte[1024];
            int n;
            try {
                while ((n = fis.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        byte[] decodedString = Base64.decode(fileContent.toString().trim().getBytes(), 0);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
