package android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ponlavitlarpeampaisarl on 3/6/15 AD.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction {
    RestrictionType type() default RestrictionType.NONE;

    int value() default 0;
}
