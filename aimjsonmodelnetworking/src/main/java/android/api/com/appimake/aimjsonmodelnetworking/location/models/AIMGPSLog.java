package android.api.com.appimake.aimjsonmodelnetworking.location.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.IQueryObject;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by nattapongr on 8/24/15 AD.
 */

@IQueryObject(name = "gps_log")
public class AIMGPSLog extends AIMModel {
    @JSONVariable
    private boolean isActive;

    @JSONVariable
    @IQueryObject(
            name = "lat",
            type = ColumnType.TEXT
    )
    private String lat;

    @JSONVariable
    @IQueryObject(
            name = "lon",
            type = ColumnType.TEXT
    )
    private String lon;

    @JSONVariable
    @IQueryObject(
            name = "tag",
            type = ColumnType.TEXT
    )
    private String tag;

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public double getLat() {
        if (lat == null)
            lat = "0";
        return Double.parseDouble(lat);
    }

    public void setLat(double lat) {
        this.lat = lat + "";
    }

    public double getLon() {
        if (lon == null)
            lon = "0";
        return Double.parseDouble(lon);
    }

    public void setLon(double lon) {
        this.lon = lon + "";
    }

    public String getTag() {
        if (tag == null)
            tag = "";
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public String getObjectIdentificationRequest() {
        return null;
    }

    @Override
    public String getKeyForObjectIdentificationRequest() {
        return null;
    }
}
