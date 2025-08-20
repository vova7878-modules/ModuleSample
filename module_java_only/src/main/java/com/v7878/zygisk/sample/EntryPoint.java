package com.v7878.zygisk.sample;

import static com.v7878.zygisk.sample.Main.TAG;

import android.util.Log;

public class EntryPoint {
    private static void printLoader(ClassLoader loader) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "loader: " + loader);
        }
    }

    // This method is executed before process specialization and with zygote privileges
    public static void premain() {
        Log.i(TAG, "premain");

        var loader = ClassLoader.getSystemClassLoader();
        printLoader(loader);
    }

    // This method is executed once per process immediately after specialization. No user classes have been loaded yet
    public static void mainCommon() {
        Log.i(TAG, "mainCommon");

        var loader = ClassLoader.getSystemClassLoader();
        printLoader(loader);
    }

    // This method is run after the system_server process classes are loaded
    public static void mainSystemServer(ClassLoader loader) {
        Log.i(TAG, "mainSystemServer");

        printLoader(loader);
    }

    // This method is executed every time an application package is loaded into the process
    // One process can run multiple applications
    public static void mainApplication(String package_name, ClassLoader loader) {
        Log.i(TAG, "mainApplication " + package_name);

        printLoader(loader);
    }
}
