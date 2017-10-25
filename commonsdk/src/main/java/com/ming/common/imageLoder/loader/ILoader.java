package com.ming.common.imageLoder.loader;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.ming.common.imageLoder.config.SingleConfig;
import com.ming.common.imageLoder.utils.DownLoadImageService;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface ILoader {

    void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD);

    void request(SingleConfig config);

    void pause();

    void resume();

    void clearDiskCache();

    void clearMemoryCache(View view);

    void clearMemory();

    boolean  isCached(String url);

    void trimMemory(int level);

    void clearAllMemoryCaches();

    void saveImageIntoGallery(DownLoadImageService downLoadImageService);
}
