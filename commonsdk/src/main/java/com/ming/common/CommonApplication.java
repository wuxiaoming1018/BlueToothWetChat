package com.ming.common;

import android.app.Application;
import android.os.StrictMode;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Administrator on 2017\7\26 0026.
 */

public class CommonApplication {

    public static void init(Application application) {
        Utils.init(application);
        solveUriException();
    }

    /**
     * 解决android.os.FileUriExposedException 的方法之一
     * http://blog.csdn.net/a1018875550/article/details/53745107
     */
    private static void solveUriException() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
