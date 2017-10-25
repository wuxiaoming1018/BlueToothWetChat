package diy.xiaoming.com.bluetoothwetchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import diy.xiaoming.com.bluetoothwetchat.R;
import diy.xiaoming.com.bluetoothwetchat.bean.BlueTooth;
import diy.xiaoming.com.bluetoothwetchat.holdview.BluetoothHolder;
import diy.xiaoming.com.bluetoothwetchat.holdview.ToastHolder;
import diy.xiaoming.com.bluetoothwetchat.interfaces.OnItemClickListener;
import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

/**
 * Created by Administrator on 2017-10-20.
 */

public class RecyclerBlueToothAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnItemClickListener listener;
    private Context context;
    private List<BlueTooth> mList;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setData(List<BlueTooth> mList){
        this.mList = mList;
    }

    public RecyclerBlueToothAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType== CommonValues.BLUETOOTH_TAG_NORMAL) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.recycler_item_blue_tooth, parent, false);
            return new BluetoothHolder(inflate);
        }else /*if(viewType==CommonValues.BLUETOOTH_TAG_TOAST)*/{
            View inflate2 = LayoutInflater.from(context).inflate(R.layout.recycler_item_toast, parent, false);
            return new ToastHolder(inflate2);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getTag();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mList.get(position).getTag()== CommonValues.BLUETOOTH_TAG_NORMAL){
            BlueTooth result = mList.get(position);
            BluetoothHolder bluetoothHolder = (BluetoothHolder) holder;
            bluetoothHolder.getTvLevel().setText(result.getRssi());
            bluetoothHolder.getTvName().setText(result.getName());
            bluetoothHolder.getTvMac().setText(result.getMac());
            bluetoothHolder.getRlClick().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }else {
            ToastHolder toastHolder = (ToastHolder) holder;
            BlueTooth result = mList.get(position);
            toastHolder.getTvToast().setText(result.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }
}
