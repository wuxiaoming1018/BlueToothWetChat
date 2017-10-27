package diy.xiaoming.com.bluetoothwetchat.activity;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ming.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import diy.xiaoming.com.bluetoothwetchat.R;
import diy.xiaoming.com.bluetoothwetchat.adapter.RecyclerBlueToothAdapter;
import diy.xiaoming.com.bluetoothwetchat.bean.BlueTooth;
import diy.xiaoming.com.bluetoothwetchat.business.BluetoothChatDo;
import diy.xiaoming.com.bluetoothwetchat.interfaces.BlueToothReceiverListener;
import diy.xiaoming.com.bluetoothwetchat.interfaces.BluetoothStateListener;
import diy.xiaoming.com.bluetoothwetchat.interfaces.OnItemClickListener;
import diy.xiaoming.com.bluetoothwetchat.receiver.BlueToothReceiver;
import diy.xiaoming.com.bluetoothwetchat.receiver.BluetoothStateReceiver;
import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

/**
 * Created by Administrator on 2017-10-20.
 */

public class MainActivity extends BaseActivity implements OnRefreshListener, OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private Switch mSwitch;
    private SwipeRefreshLayout mRefresh;
    private RecyclerBlueToothAdapter mAdapter;
    private List<BlueTooth> mList;
    private BluetoothAdapter mBluetoothAdapter;
    private BlueToothReceiver mReceiver;
    private BluetoothStateReceiver stateReceiver;
    private TextView showText;
    private ProgressDialog progressDialog;
    private BluetoothChatDo mDao;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommonValues.BLUE_TOOTH_DIALOG:
                    showProgressDialog((String) msg.obj);
                    break;
                case CommonValues.BLUE_TOOTH_TOAST:
                    dismissProgressDialog();
                    ToastUtils.showShort((String) msg.obj);
                    break;
                case CommonValues.BLUE_TOOTH_SUCCESS:
                    dismissProgressDialog();
                    String name = (String) msg.obj;
                    ToastUtils.showShort("连接设备" + name + "成功");
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra(CommonValues.CHART_EXTRA, name);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void showProgressDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(content);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        mContext = this;
        mDao = BluetoothChatDo.getInstance(mHandler);
        mSwitch = bindWidget(R.id.mSwitch);
        mRecyclerView = bindWidget(R.id.recyclerView);
        mRefresh = bindWidget(R.id.swipeRefreshLayout);
        showText = bindWidget(R.id.show);
        showText.setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        stateReceiver = new BluetoothStateReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(stateReceiver, intentFilter);
        stateReceiver.setOnStateChangeListener(new BluetoothStateListener() {
            @Override
            public void stateOn() {
                mSwitch.setChecked(true);
                LogUtils.e("蓝牙已经开启");
                mSwitch.setText("蓝牙已经开启");
            }

            @Override
            public void stateOff() {
                mSwitch.setChecked(false);
                LogUtils.e("蓝牙已经关闭");
                mSwitch.setText("蓝牙已经关闭");
            }
        });
    }

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new RecyclerBlueToothAdapter(this);
        mAdapter.setData(mList);
        mRecyclerView.setAdapter(mAdapter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            LogUtils.e("此设备不支持蓝牙功能");
            ToastUtils.showShort("此设备不支持蓝牙功能");
            return;
        }
        if (mBluetoothAdapter.isEnabled()) {
            mSwitch.setChecked(true);
            mSwitch.setText("蓝牙已开启");
        } else {
            mSwitch.setChecked(false);
            mSwitch.setText("蓝牙未开启");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new BlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);
        mReceiver.setListener(new BlueToothReceiverListener() {
            @Override
            public void getDevices(BluetoothDevice device, int position) {
                mList.add(new BlueTooth(device.getName(), device.getAddress(), position + ""));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void searchFinish() {
                mRefresh.setRefreshing(false);
                showText.setVisibility(View.GONE);
                LogUtils.e("扫描完成");
                ToastUtils.showShort("扫描完成");
            }
        });
    }

    @Override
    public void initArgument(Bundle bundle) {

    }

    @Override
    public void setListener() {
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mSwitch.setOnCheckedChangeListener(this);
        onRefresh();
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {
        //dynamic register broadcastReceiver for ACTION_FOUND
    }

    /**
     * the drop-down refresh listening
     */
    @Override
    public void onRefresh() {
        if (mBluetoothAdapter.isEnabled()) {
            mList.clear();
            showBluetooth();
        } else {
            mRefresh.setRefreshing(false);
            ToastUtils.showShort("请先开启蓝牙");
        }
    }

    @Override
    public void onItemClick(int position) {
        BlueTooth blueTooth = mList.get(position);
        mDao.connectDevice(blueTooth.getMac());
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            showText.setVisibility(View.VISIBLE);
            if (!mBluetoothAdapter.isEnabled()) {
                //open bluetooth with dialog
//                Intent en = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(en, CommonValues.OPEN_BLUETOOTH);
                //open bluetooth without dialog
                mBluetoothAdapter.enable();
                mSwitch.setText("蓝牙已经开启");
                while (true) {
                    if (mBluetoothAdapter.isEnabled()) {
                        break;
                    } else {
                        SystemClock.sleep(500);
                    }
                }
                showBluetooth();
            } else {
                showBluetooth();
            }
        } else {
            mDao.stop();
            mBluetoothAdapter.cancelDiscovery();
            mList.clear();
            mAdapter.notifyDataSetChanged();
            showText.setVisibility(View.GONE);
            mBluetoothAdapter.disable();//close bluetooth
            mSwitch.setText("蓝牙已经关闭");
            LogUtils.e("蓝牙已经关闭");
        }
    }

    private void showBluetooth() {
        ToastUtils.showShort("蓝牙已经开启,开始扫描设备");
        LogUtils.e("000主线程");
        mDao.start();
        showText.setVisibility(View.VISIBLE);
        mBluetoothAdapter.startDiscovery();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            //已经匹配的蓝牙 already matched
            mList.add(new BlueTooth("已经匹配的设备", CommonValues.BLUETOOTH_TAG_TOAST));
            for (BluetoothDevice device : devices) {
                mList.add(new BlueTooth(device.getName(), device.getAddress(), ""));
            }
            mList.add(new BlueTooth("已扫描的设备", CommonValues.BLUETOOTH_TAG_TOAST));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommonValues.OPEN_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    LogUtils.e("蓝牙开启成功");
                } else {
                    LogUtils.e("蓝牙开启失败");
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDao.stop();
        unregisterReceiver(stateReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        close();
    }

    private void close() {
        mBluetoothAdapter.cancelDiscovery();
        mRefresh.setRefreshing(false);
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() == 0) {
            if (stateReceiver != null) {
                unregisterReceiver(stateReceiver);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
