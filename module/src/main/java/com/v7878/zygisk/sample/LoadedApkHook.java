package com.v7878.zygisk.sample;

import static com.v7878.unsafe.Reflection.fieldOffset;
import static com.v7878.unsafe.Reflection.getDeclaredField;
import static com.v7878.unsafe.Reflection.getDeclaredMethod;
import static com.v7878.unsafe.Reflection.unreflect;
import static com.v7878.unsafe.Utils.nothrows_run;

import android.annotation.SuppressLint;
import android.util.Log;

import com.v7878.r8.annotations.DoNotShrink;
import com.v7878.unsafe.AndroidUnsafe;
import com.v7878.unsafe.invoke.EmulatedStackFrame;
import com.v7878.unsafe.invoke.Transformers;
import com.v7878.vmtools.Hooks;
import com.v7878.vmtools.Hooks.EntryPointType;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@SuppressLint("PrivateApi")
public class LoadedApkHook {
    private static final String LOADED_APK = "android.app.LoadedApk";
    private static final Class<?> LOADED_APK_CLASS = nothrows_run(() -> Class.forName(LOADED_APK));
    private static final MethodHandle getPackageName = unreflect(getDeclaredMethod(LOADED_APK_CLASS, "getPackageName"));
    private static final long CLASS_LOADER_OFFSET = fieldOffset(getDeclaredField(LOADED_APK_CLASS, "mClassLoader"));

    private static void runForApplication(EmulatedStackFrame frame) throws Throwable {
        var thiz = frame.accessor().getReference(0);

        Objects.requireNonNull(thiz);
        String package_name = (String) getPackageName.invoke(thiz);
        ClassLoader loader = (ClassLoader) AndroidUnsafe.getObject(thiz, CLASS_LOADER_OFFSET);

        EntryPoint.mainApplication(package_name, loader);
    }

    @DoNotShrink
    public static void init() throws Throwable {
        Method target = getDeclaredMethod(LOADED_APK_CLASS,
                "createOrUpdateClassLoaderLocked", List.class);

        Hooks.hook(target, EntryPointType.CURRENT, (original, frame) -> {
            Transformers.invokeExactWithFrame(original, frame);
            try {
                runForApplication(frame);
            } catch (Throwable th) {
                Log.e(Main.TAG, "Exception", th);
            }
        }, EntryPointType.DIRECT);
    }
}
