package diy.xiaoming.com.bluetoothwetchat.interfaces;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2017-10-20.
 */

public interface BlueToothReceiverListener {

    void getDevices(BluetoothDevice device, int position);

    void searchFinish();
}
