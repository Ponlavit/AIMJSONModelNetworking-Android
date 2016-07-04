package android.api.com.appimake.aimjsonmodelnetworking.base.core.model;


import android.api.com.appimake.aimjsonmodelnetworking.AIMConfig;

import java.util.Date;

/**
 * Created by ponlavitlarpeampaisarl on 2/4/15 AD.
 */
public class AIMDateTime extends Date {
    public AIMDateTime(String unixTime) {
        if (unixTime != null)
            setTime(Long.parseLong(unixTime) * (AIMConfig.USE_MILLISECONDS_TIMESTAMP_EXCHANGE ? 1 : 1000));
    }

    public AIMDateTime(long unixTime) {
        this((unixTime + "").length() > 10 ? (unixTime + "").substring(0, 10) : unixTime + "");
    }

    public AIMDateTime() {
        super();
    }

    public String toUnixTimeString() {
        return (getTime() / (AIMConfig.USE_MILLISECONDS_TIMESTAMP_EXCHANGE ? 1 : 1000)) + "";
    }

}
