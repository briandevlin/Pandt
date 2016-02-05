package com.bdevlin.apps.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

/**
 * Created by brian on 1/17/2016.
 */
public class ThemeUtils {

    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    private static final int[] TEMP_ARRAY = new int[1];

    public static int getThemeAttrColor(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TypedArray a = context.obtainStyledAttributes(null, TEMP_ARRAY);
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
    public static float getThemeAttrDimension(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TypedArray a = context.obtainStyledAttributes(null, TEMP_ARRAY);
        try {
            return a.getDimension(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static String getThemeName(Context context)
    {
        PackageInfo packageInfo;
        try
        {
            String packageName =  context.getPackageName();
             packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            int theme = packageInfo.applicationInfo.theme;
            return  context.getResources().getResourceEntryName(theme);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue = TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }
}
