package android.api.com.appimake.aimjsonmodelnetworking.base.core.model;

/**
 * Created by nattapongr on 8/5/15 AD.
 */
public class AIMMasterArrayList<T> extends AIMArrayList {

    private String target = "none";
    private String module = "none";


    protected AIMMasterArrayList(Class<T> c) {
        super(c);
    }

    public static AIMMasterArrayList Builder(Class asClass, String getTarget, String getModule) {
        return new AIMMasterArrayList(asClass).setTarget(getTarget).setModule(getModule);
    }

    public String getTarget() {
        return target;
    }

    public AIMMasterArrayList setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getModule() {
        return module;
    }

    public AIMMasterArrayList setModule(String module) {
        this.module = module;
        return this;
    }
}
