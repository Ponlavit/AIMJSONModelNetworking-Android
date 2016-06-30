package android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ponlavitlarpeampaisarl on 2/3/15 AD.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONVariable {
    boolean localOnly() default false;
}
