package android.api.com.appimake.aimjsonmodelnetworking.base.core.model;

/**
 * Created by nattapongr on 8/5/15 AD.
 */
public class BaseMasterArrayList<T> extends BaseArrayList {

    private String target = "none";
    private String module = "none";


    protected BaseMasterArrayList(Class<T> c) {
        super(c);
    }

    public static BaseMasterArrayList Builder(Class asClass, String getTarget, String getModule) {
        return new BaseMasterArrayList(asClass).setTarget(getTarget).setModule(getModule);
    }

    public String getTarget() {
        return target;
    }

    public BaseMasterArrayList setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getModule() {
        return module;
    }

    public BaseMasterArrayList setModule(String module) {
        this.module = module;
        return this;
    }
}
