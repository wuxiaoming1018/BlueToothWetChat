package diy.xiaoming.com.bluetoothwetchat.holdview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import diy.xiaoming.com.bluetoothwetchat.R;


/**
 * Created by ZengZeHong on 2017/5/16.
 */

public class ChatRightHolder extends RecyclerView.ViewHolder {
    private TextView tvContent;
    private TextView tvName;

    public ChatRightHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.tv_right);
        tvName = (TextView) itemView.findViewById(R.id.tv_device);
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public void setTvContent(TextView tvContent) {
        this.tvContent = tvContent;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }
}
