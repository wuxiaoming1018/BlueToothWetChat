package diy.xiaoming.com.bluetoothwetchat.holdview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import diy.xiaoming.com.bluetoothwetchat.R;

/**
 * Created by Administrator on 2017-10-20.
 */

public class BluetoothHolder extends RecyclerView.ViewHolder {
    private TextView tvName;
    private TextView tvLevel;
    private TextView tvMac;
    private RelativeLayout rlClick;

    public BluetoothHolder(View itemView) {
        super(itemView);
        tvMac = itemView.findViewById(R.id.tv_mac);
        tvLevel = itemView.findViewById(R.id.tv_level);
        tvName = itemView.findViewById(R.id.tv_name);
        rlClick = itemView.findViewById(R.id.rl_click);
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    public TextView getTvLevel() {
        return tvLevel;
    }

    public void setTvLevel(TextView tvLevel) {
        this.tvLevel = tvLevel;
    }

    public TextView getTvMac() {
        return tvMac;
    }

    public void setTvMac(TextView tvMac) {
        this.tvMac = tvMac;
    }

    public RelativeLayout getRlClick() {
        return rlClick;
    }

    public void setRlClick(RelativeLayout rlClick) {
        this.rlClick = rlClick;
    }
}
