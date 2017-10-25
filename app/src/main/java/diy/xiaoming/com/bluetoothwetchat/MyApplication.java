package diy.xiaoming.com.bluetoothwetchat;

import android.app.Application;

import com.ming.common.CommonApplication;
import com.ming.common.imageLoder.loader.ImageLoader;

/**
 * Created by Administrator on 2017-10-20.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CommonApplication.init(this);
        ImageLoader.init(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //Called when clean memory
        ImageLoader.trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Called when low memory
        ImageLoader.clearAllMemoryCaches();
    }
}
