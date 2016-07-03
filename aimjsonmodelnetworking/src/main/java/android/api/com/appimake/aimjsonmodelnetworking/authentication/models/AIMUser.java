package android.api.com.appimake.aimjsonmodelnetworking.authentication.models;

import android.api.com.appimake.aimjsonmodelnetworking.authentication.enumulation.URLService;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by nattapongr on 5/14/15.
 */
public class AIMUser extends AIMModel {

    @JSONVariable
    private String u_id;
    @JSONVariable
    private String token;
    @JSONVariable
    private String name;
    @JSONVariable
    private String role;
    @JSONVariable
    private ArrayList allow_module = new ArrayList();
    @JSONVariable
    private ArrayList allow_service = new ArrayList();
    @JSONVariable
    private AIMArrayList<AIMUserServiceURL> webapi = AIMArrayList.Builder(AIMUserServiceURL.class);
    private Bitmap profilePhoto;

    public String getWepAPIForKey(URLService service) {
        switch (service) {
            case URL_ADDED:
                return getURL("ADDED");
            case URL_DELETED:
                return getURL("DELETED");
            case URL_SELECTED:
                return getURL("SELECTED");
            case URL_UPDATED:
                return getURL("UPDATED");
            case URL_IMAGES:
                return getURL("IMAGES");
            default:
                return "";
        }
    }

    private String getURL(String txt) {
        for (int i = 0; i < webapi.size(); i++) {
            AIMUserServiceURL tmpAPI = (AIMUserServiceURL) webapi.get(i);
            if (tmpAPI.getService_key().equalsIgnoreCase(txt))
                return tmpAPI.getService_url();
        }
        return "";
    }

    public Bitmap getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Bitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList getAllow_module() {
        if (allow_module == null)
            allow_module = new ArrayList();
        return allow_module;
    }

    public void setAllow_module(ArrayList allow_module) {
        this.allow_module = allow_module;
    }

    public ArrayList getAllow_service() {
        return allow_service;
    }

    public void setAllow_service(ArrayList allow_service) {
        this.allow_service = allow_service;
    }

    public AIMArrayList<AIMUserServiceURL> getWebapi() {
        return webapi;
    }

    public void setWebapi(AIMArrayList<AIMUserServiceURL> webapi) {
        this.webapi = webapi;
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
