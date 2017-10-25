package diy.xiaoming.com.bluetoothwetchat.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;

import diy.xiaoming.com.bluetoothwetchat.interfaces.BluetoothStateListener;

/**
 * Listen to the bluetooth state changes
 * Created by Administrator on 2017-10-20.
 */

public class BluetoothStateReceiver extends BroadcastReceiver {

    private BluetoothStateListener listener;

    public void setOnStateChangeListener(BluetoothStateListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int code = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,0);
                switch (code){
                    case BluetoothAdapter.STATE_ON:
                        LogUtils.e("蓝牙已经开启0");
                        listener.stateOn();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        LogUtils.e("蓝牙正在开启0");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        LogUtils.e("蓝牙已经关闭0");
                        listener.stateOff();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        LogUtils.e("蓝牙正在关闭0");
                        break;
                }
                break;
        }
    }
}
