package android.api.com.appimake.aimjsonmodelnetworking.location.service;

import android.Manifest;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.intf.iGetString;
import android.api.com.appimake.aimjsonmodelnetworking.location.intf.AIMIReciveLocationChange;
import android.api.com.appimake.aimjsonmodelnetworking.location.models.AIMGPSLog;
import android.api.com.appimake.aimjsonmodelnetworking.location.models.AIMGPSTrakingModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.AIMObjectFactory;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.AIMWebAPIUpdateObject;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nattapongr on 8/24/15 AD.
 */
public class AIMLocationManager implements LocationListener, IWebAPINotification {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 144;
    public static String[] PERMISSIONS_ACCESS_FINE_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};
    public static boolean isRealLocation = false;
    public static String provider = "";
    private static AIMLocationManager shareInstance;
    AIMIReciveLocationChange callBack;
    Location currentLocation;
    private String USER_TOKEN = "";
    private String SERVICE_URL = "";
    private Context mContext;
    private android.location.LocationManager location;
    private android.location.LocationManager locationNET;

    private AIMLocationManager() {

    }

    public static AIMLocationManager getInstance() {
        if (shareInstance == null) {
            shareInstance = new AIMLocationManager();
        }
        return shareInstance;
    }

    public void registForReviceLocation(AIMIReciveLocationChange call) {
        this.callBack = call;
    }

    public void initLocationService(Context context) {
        this.mContext = context;
        this.location = (android.location.LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
        this.locationNET = (android.location.LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.locationNET.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 0, 0, this);
        this.location.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void registerGPSDatabase() {
        AIMObjectFactory.register(AIMGPSLog.class);
    }

    public void determineAddress(final TextView tv, String latitude, String longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String returnAddress = "";

        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            returnAddress = address + " " + city + " " + state;
        }
        final String finalReturnAddress = returnAddress;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(finalReturnAddress);
            }
        });
    }

    public BaseArrayList<AIMGPSLog> getGPSLog(BaseDateTime from, BaseDateTime to) {
        BaseArrayList<AIMGPSLog> aLog = BaseArrayList.Builder(AIMGPSLog.class);
        aLog.addAll(AIMObjectFactory.select(AIMGPSLog.class).getAll());
        return aLog;
    }

    public AIMGPSLog getCurrentLocation() {
        AIMGPSLog gps = new AIMGPSLog();
        gps.updateTimeStamp();
        if (currentLocation != null) {
            gps.setIsActive(true);
            gps.setLat(currentLocation.getLatitude());
            gps.setLon(currentLocation.getLongitude());
        } else gps.setIsActive(false);
        return gps;
    }

    public double getTotalDistance(BaseArrayList<AIMGPSLog> arrLog) {
        double tDis = 0.0;
        for (int i = 0; i < arrLog.size() - 1; i++) {
            AIMGPSLog first = (AIMGPSLog) arrLog.get(i);
            AIMGPSLog seccound = (AIMGPSLog) arrLog.get(i + 1);
            tDis += calDistance(first.getLat(), first.getLon(), seccound.getLat(), seccound.getLon());
        }
        return tDis;
    }

    public void startService(Context mContext) {
        initLocationService(mContext);
        registerGPSDatabase();

        clearAllLog();
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    if (currentLocation != null) {
                        serviceRunning();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void serviceRunning() {
        if (currentLocation != null) {
            if (currentLocation.getLatitude() > 0 && currentLocation.getLongitude() > 0) {
                AIMGPSLog gps = new AIMGPSLog();
                gps.setLat(currentLocation.getLatitude());
                gps.setLon(currentLocation.getLongitude());
                gps.setTag(currentLocation.getProvider() == null ? "" : currentLocation.getProvider());
                gps.updatedate = new BaseDateTime();
                AIMObjectFactory.insert(AIMGPSLog.class).asNewItem(gps);
            }
        }
    }

    public void startServiceTraking(final String module, final String target) {
        registerGPSDatabase();
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    if (currentLocation != null) {
                        sentLocationTrakingToServer(module, target);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void sentLocationTrakingToServer(String module, String target) {

        AIMGPSTrakingModel data = new AIMGPSTrakingModel();
        data.setLat(currentLocation.getLatitude() + "");
        data.setLng(currentLocation.getLongitude() + "");

        AIMWebAPIUpdateObject.getInstance().update(
                SERVICE_URL,
                null,
                data.toJSONString(),
                target,
                module,
                USER_TOKEN,
                this,
                getClass());
    }

    public void clearAllLog() {
        AIMObjectFactory.delete(AIMGPSLog.class).deleteTable();
        registerGPSDatabase();
    }

    public double calDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.609344; // *1000 = Km.
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void getAddressFromCoordinate(double lat, double lon, iGetString callBack, Class Class) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String address = "";
        String city = "";
        String state = "";
        String country = "";
        String postalCode = "";

        String all = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();

                if (address != null && !address.equalsIgnoreCase("null"))
                    all += address;
                if (city != null && !city.equalsIgnoreCase("null"))
                    all += ", " + city;
                if (state != null && !state.equalsIgnoreCase("null"))
                    all += ",\n" + state;
                if (country != null && !country.equalsIgnoreCase("null"))
                    all += ", " + country;
                if (postalCode != null && !postalCode.equalsIgnoreCase("null"))
                    all += ",\n" + postalCode;

            }
            callBack.getSucc(Class.getClass(), all);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAddress(iGetString callBack, Class Class) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String address = "";
        if (currentLocation != null) {
            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                address = addresses.get(0).getAddressLine(0);
                callBack.getSucc(Class.getClass(), address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            callBack.getSucc(Class, "-");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        provider = location.getProvider();
        if (provider.equalsIgnoreCase("gps")) {
            isRealLocation = true;
        }
        currentLocation = location;
        if (this.callBack != null) {
            callBack.onLocationChange(location);
            Log.d("location_change_MNG", location.toString());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void webAPISuccess(String code, String message, String jsonData, Class Class) {
        Log.d("AIMLocationManager", "Class : " + Class.getName() + "Code : " + code + " MSG : " + message + " RES : " + jsonData);
    }

    @Override
    public void webAPIProgress(float sizeUploaded, float fileSize) {

    }

    @Override
    public void webAPIFailed(String code, String message, Class Class) {
        Log.d("AIMLocationManager", "Class : " + Class.getName() + "Code : " + code + " MSG : " + message);
    }

}
