package com.v7878.zygisk.sample;

import static com.v7878.zygisk.ZygoteLoader.PACKAGE_SYSTEM_SERVER;

import android.util.Log;

import com.v7878.zygisk.ZygoteLoader;

public class Main {
    public static String TAG = "MODULE_SAMPLE";

    public static void premain() {
        EntryPoint.premain();
    }

    @SuppressWarnings("ConfusingMainMethod")
    public static void main() {
        Log.i(TAG, "Injected into " + ZygoteLoader.getPackageName());
        try {
            EntryPoint.mainCommon();
            if (BuildConfig.RUN_FOR_SYSTEM_SERVER &&
                    PACKAGE_SYSTEM_SERVER.equals(ZygoteLoader.getPackageName())) {
                SystemServerInit.init();
            }
            if (BuildConfig.RUN_FOR_APPLICATIONS) {
                ApplicationInit.init();
            }
        } catch (Throwable th) {
            Log.e(TAG, "Exception", th);
        }
        Log.i(TAG, "Done");
    }
}
