package android.api.com.appimake.aimjsonmodelnetworking.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */

public class ScreenSize {
    public static DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    // Get display size
    private static int screenWidth;
    private static int screenHeight;

    private ScreenSize() {
    }

    /**
     * Use for get screen size
     */
    private static void getScreenSize() {
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public static int getScreenWidth() {
        getScreenSize();
        return ScreenSize.screenWidth;
    }

    public static int getScreenHeigh() {
        getScreenSize();
        return ScreenSize.screenHeight;
    }

    public static int getScreenWidthInDP() {
        getScreenSize();
        return (int) (ScreenSize.screenWidth / metrics.density);
    }

    public static int getScreenHeighInDP() {
        getScreenSize();
        return (int) (ScreenSize.screenHeight / metrics.density);
    }

    public static boolean isTabletSize(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
