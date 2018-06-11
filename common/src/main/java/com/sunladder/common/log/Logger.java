package com.sunladder.common.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Sun on 2018/5/26.
 */

public final class Logger {

    private static final String CLASS_NAME = Logger.class.getName();
    private static final String LOG_PREFFIX = "Logger|";
    private static final boolean DEBUG = true;

    public static void printMsg(String msg) {
        if (!DEBUG) {
            return;
        }
        print(getDefaultTag(), msg);
    }

    public static void printVar(String valueName, Object value) {
        if (!DEBUG) {
            return;
        }
        String msg = String.format("%s:%s", valueName, value != null ? value.toString() : "null");
        print(getDefaultTag(), msg);
    }

    public static void printCurrentMethod() {
        if (!DEBUG) {
            return;
        }
        printCurrentMethod("----------");
    }

    public static void printCurrentMethod(String preffix) {
        if (!DEBUG) {
            return;
        }
        StackTraceElement element = getOuterInvokeElement();
        String className = element.getClassName();
        String methodInfo = String.format("%s%s->%s:%d", preffix, getSimpleClassName(className), element.getMethodName(), element.getLineNumber());
        print(getPkgName(className), methodInfo);
    }

    public static void printCurrentThread(String tag) {
        if (!DEBUG) {
            return;
        }
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        String state = currentThread.getState().name();
        print(tag, name + " " + state);
    }

    private static void print(String tag, String msg) {
        Log.i(LOG_PREFFIX + tag, msg);
    }

    private static String getDefaultTag() {
        return getPkgName(getOuterInvokeElement().getClassName());
    }

    private static String getSimpleClassName(String className) {
        return !TextUtils.isEmpty(className) ? className.substring(className.lastIndexOf('.'), className.length()) : "";
    }

    private static String getPkgName(String className) {
        return !TextUtils.isEmpty(className) ? className.substring(0, className.lastIndexOf('.')) : "";
    }

    private static StackTraceElement getOuterInvokeElement() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement target = null;
        int index = 0;
        while (index < stackTrace.length) {
            StackTraceElement element = stackTrace[index];
            if (CLASS_NAME.equals(element.getClassName())) {
                break;
            }
            ++index;
        }
        while (index < stackTrace.length) {
            StackTraceElement element = stackTrace[index];
            if (!CLASS_NAME.equals(element.getClassName())) {
                target = element;
                break;
            }
            ++index;
        }
        if (target == null) {
            throw new RuntimeException("Fail to find StackTraceElement!");
        }
        return target;
    }
}
