package diy.xiaoming.com.bluetoothwetchat.util;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import diy.xiaoming.com.bluetoothwetchat.R;

/**
 * Created by Administrator on 2017-10-20.
 */

public class SwipeRefreshUtil {

    public static void init(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorBg,
                R.color.colorPrimary,
                R.color.colorGray
        );
    }
}
