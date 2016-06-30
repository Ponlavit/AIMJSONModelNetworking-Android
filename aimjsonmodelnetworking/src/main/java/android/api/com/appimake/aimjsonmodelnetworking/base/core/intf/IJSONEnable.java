package android.api.com.appimake.aimjsonmodelnetworking.base.core.intf;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;

/**
 * Created by ponlavitlarpeampaisarl on 2/4/15 AD.
 */
public interface IJSONEnable<T> {

    public String toJSONLocalString();

    public String toJSONString();

    public Object toJSONObject(boolean isLocaleOnly);

    public void updateTimeStamp();

    public AIMDateTime getUpdateDate();
}
