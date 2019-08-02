package com.kiyota.proteincalendarapp.util;

import android.util.Log;

/**
 * Created by kiyota on 2019/07/14.
 */

public class LogUtil {

    private static final String PREFIX_SPA = "SPA_";
    private static final boolean IS_INFO = true;
    private static final boolean IS_DEBUG = true;
    private static final boolean IS_WARN = true;
    private static final boolean IS_ERROR = true;

    public static void i(String tag, String msg) {
        if (IS_INFO) {
            Log.i(PREFIX_SPA + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(PREFIX_SPA + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_WARN) {
            Log.w(PREFIX_SPA + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_ERROR) {
            Log.e(PREFIX_SPA + tag, msg);
        }
    }
}
