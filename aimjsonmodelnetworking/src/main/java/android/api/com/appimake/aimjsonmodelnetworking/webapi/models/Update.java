package android.api.com.appimake.aimjsonmodelnetworking.webapi.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by nattapongr on 5/13/15.
 */
public class Update extends AIMModel {
    @JSONVariable
    private String objectId;
    @JSONVariable
    private String data;
    @JSONVariable
    private String target;
    @JSONVariable
    private String module;
    @JSONVariable
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getObjectStringId() {
        return objectId;
    }

    public void setObjectStringId(String objectId) {
        this.objectId = objectId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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
