package diy.xiaoming.com.bluetoothwetchat.holdview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import diy.xiaoming.com.bluetoothwetchat.R;

/**
 * Created by Administrator on 2017-10-20.
 */

public class ToastHolder extends RecyclerView.ViewHolder {

    private TextView tvToast;

    public ToastHolder(View itemView) {
        super(itemView);
        tvToast = itemView.findViewById(R.id.tv_toast);
    }

    public TextView getTvToast() {
        return tvToast;
    }

    public void setTvToast(TextView tvToast) {
        this.tvToast = tvToast;
    }
}
