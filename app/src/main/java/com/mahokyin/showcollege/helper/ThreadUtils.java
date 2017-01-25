package com.mahokyin.showcollege.helper;

import android.os.Looper;

import timber.log.Timber;


/**
 * @author Joe Ma
 */
public final class ThreadUtils {
    public static long getThreadId() {
        Thread t = Thread.currentThread();
        long threadID = t.getId();
        return threadID;
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static String getThreadSignature() {
        Thread t = Thread.currentThread();
        long l = t.getId();
        String name = t.getName();
        long p = t.getPriority();
        String gname = t.getThreadGroup().getName();
        String result = (name
                + ":(id)" + l
                + ":(priority)" + p
                + ":(group)" + gname);
        Timber.d(".getThreadSignature(): " + result);
        return result;
    }
}
