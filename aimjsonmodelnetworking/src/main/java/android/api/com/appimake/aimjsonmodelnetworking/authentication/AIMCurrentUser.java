package android.api.com.appimake.aimjsonmodelnetworking.authentication;

import android.api.com.appimake.aimjsonmodelnetworking.authentication.models.AIMUser;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.enumulation.UpdatePolicy;

/**
 * Created by nattapongr on 5/15/15.
 */
public class AIMCurrentUser {
    private static AIMCurrentUser ourInstance;

    private AIMUser CurrentUser;

    private AIMCurrentUser() {
    }

    public static AIMCurrentUser getInstance() {
        if (ourInstance == null)
            ourInstance = new AIMCurrentUser();
        return ourInstance;
    }

    public AIMUser getCurrentUser() {
        return this.CurrentUser;
    }

    public void setCurrentUser(String json) {
        if (this.CurrentUser == null)
            this.CurrentUser = new AIMUser();
        this.CurrentUser.updateFromJson(json, UpdatePolicy.ForceUpdate);

    }

    public void clearUser() {
        this.CurrentUser = new AIMUser();
    }
}
