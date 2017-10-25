package com.ming.common.imageLoder.loader;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.ming.common.imageLoder.config.GlobalConfig;
import com.ming.common.imageLoder.config.SingleConfig;
import com.ming.common.imageLoder.utils.DownLoadImageService;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ImageLoader {

    public static Context context;

    //默认最大缓存
    public static int CACHE_IMAGE_SIZE = 250;

    public static void init(Context context) {
        init(context, CACHE_IMAGE_SIZE);
    }

    private static void init(Context context, int cacheImageSize) {
        init(context, cacheImageSize, MemoryCategory.NORMAL);
    }

    private static void init(Context context, int cacheImageSize, MemoryCategory normal) {
        init(context, cacheImageSize, normal, true);
    }

    /**
     * 初始化
     *
     * @param context        上下文
     * @param cacheImageSize Glide默认磁盘缓存最大容量
     * @param normal         调整内存缓存大小 LOW(0.5f) ／ NORMAL(1f) ／ HIGH(1.5f);
     * @param isInternalCD   true 磁盘缓存到应用内部目录  false磁盘缓存到外部内存
     */
    private static void init(Context context, int cacheImageSize, MemoryCategory normal, boolean isInternalCD) {
        ImageLoader.context = context;
        GlobalConfig.init(context, cacheImageSize, normal, isInternalCD);
    }

    /**
     * 获取当前的Loader
     *
     * @return
     */
    public static ILoader getActualLoader() {
        return GlobalConfig.getLoader();
    }

    /**
     * 加载普通图片
     *
     * @param context
     * @return
     */
    public static SingleConfig.ConfigBuilder with(Context context) {
        return new SingleConfig.ConfigBuilder(context);
    }

    /**
     * 在内存清理的时候调用
     * Application里面的trimMemory(int level)里面调用
     *
     * @param level
     */
    public static void trimMemory(int level) {
        getActualLoader().trimMemory(level);
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllMemoryCaches() {
        getActualLoader().clearAllMemoryCaches();
    }

    /**
     * 取消请求
     */
    public static void pauseRequests() {
        getActualLoader().pause();

    }

    /**
     * 恢复请求
     * 在列表滑动的时候，调用pauseRequests()取消请求，滑动停止的时候调用resumeRequests()恢复请求
     */
    public static void resumeRequests() {
        getActualLoader().resume();
    }

    /**
     * 清除指定view的缓存
     *
     * @param view 目标view
     */
    public static void clearMemoryCache(View view) {
        getActualLoader().clearMemoryCache(view);
    }


    /**
     * 清除磁盘缓存(必须在后台线程中执行)
     * 否则会出现异常:java.lang.IllegalArgumentException: You must call this method on a background thread
     */
    public static void clearDiskCache() {
        getActualLoader().clearDiskCache();
    }

    /**
     * 清除内存缓存(必须在UI主线程中调用)
     * 否则会出现异常:java.lang.IllegalArgumentException: You must call this method on the main thread
     */
    public static void clearMemory() {
        getActualLoader().clearMemory();
    }

    /**
     * 图片保存到相册
     *
     * @param downLoadImageService
     */
    public static void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        getActualLoader().saveImageIntoGallery(downLoadImageService);
    }

}
