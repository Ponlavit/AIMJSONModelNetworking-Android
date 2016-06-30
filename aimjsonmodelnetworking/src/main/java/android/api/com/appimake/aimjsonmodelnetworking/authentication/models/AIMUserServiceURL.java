package android.api.com.appimake.aimjsonmodelnetworking.authentication.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by nattapongr on 8/5/15 AD.
 */
public class AIMUserServiceURL extends AIMModel {

    @JSONVariable
    private int service_id;
    @JSONVariable
    private String service_key;
    @JSONVariable
    private String service_url;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_key() {
        return service_key;
    }

    public void setService_key(String service_key) {
        this.service_key = service_key;
    }

    public String getService_url() {
        return service_url.replace("\\", "");
    }

    public void setService_url(String service_url) {
        this.service_url = service_url;
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
