package android.api.com.appimake.aimjsonmodelnetworking.webapi.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseModel;

/**
 * Created by nattapongr on 5/13/15.
 */
public class Add extends BaseModel {
    @JSONVariable
    private String target;
    @JSONVariable
    private String module;
    @JSONVariable
    private String data;
    @JSONVariable
    private String token;

    public void setTarget(String target) {
        this.target = target;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setToken(String token) {
        this.token = token;
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
