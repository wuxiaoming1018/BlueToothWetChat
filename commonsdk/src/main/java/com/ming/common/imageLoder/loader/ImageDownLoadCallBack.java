package com.ming.common.imageLoder.loader;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed(Exception e);

    void onDownLoadFailed();
}
