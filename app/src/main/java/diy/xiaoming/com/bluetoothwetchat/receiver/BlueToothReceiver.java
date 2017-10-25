package diy.xiaoming.com.bluetoothwetchat.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;

import diy.xiaoming.com.bluetoothwetchat.interfaces.BlueToothReceiverListener;

/**
 * Created by Administrator on 2017-10-20.
 */

public class BlueToothReceiver extends BroadcastReceiver {

    private BlueToothReceiverListener listener;

    public void setListener(BlueToothReceiverListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                LogUtils.e("蓝牙名称:"+device.getName()+"--MAC地址 "+device.getAddress()+"--强度:"+rssi);
                if (listener != null) {
                    listener.getDevices(device,rssi);
                }
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                if (listener != null) {
                    listener.searchFinish();
                }
                break;
        }
    }
}
