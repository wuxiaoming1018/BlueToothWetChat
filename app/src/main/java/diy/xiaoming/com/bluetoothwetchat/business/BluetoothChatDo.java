package diy.xiaoming.com.bluetoothwetchat.business;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

/**
 * Created by Administrator on 2017-10-24.
 */

public class BluetoothChatDo {
    //新增修改
    private int mState;
    private static Handler mHandler;
    public static BluetoothChatDo instance;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String NAME_SECURE = "BluetoothChatDo";
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-abcd-11de-8a39-0800200c9a66");

    //用来连接端口的线程
    private AcceptThread mAcceptThread;
    private TransferThread mTransferThread;
    private ConnectThread mConnectThread;
    private boolean isTransferError = false;
    private int count = 1;

    public static BluetoothChatDo getInstance(Handler handler) {
        mHandler = handler;
        if (instance == null) {
            synchronized (BluetoothChatDo.class) {
                if (instance == null) {
                    instance = new BluetoothChatDo();
                }
            }
        }
        return instance;
    }

    private BluetoothChatDo() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = CommonValues.STATE_NONE;
    }

    //开启监听
    public synchronized void start() {
        if (mTransferThread != null) {
            mTransferThread.cancel();
            mTransferThread = null;
        }
        setState(CommonValues.STATE_LISTEN);
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        if (mTransferThread != null) {
            mTransferThread.cancel();
            mTransferThread = null;
        }
        setState(CommonValues.STATE_NONE);
    }

    public synchronized void connectDevice(String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (mState == CommonValues.STATE_CONNECTING) {
            if (mTransferThread != null) {
                mTransferThread.cancel();
                mTransferThread = null;
            }
        }

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        sendMessageToUi(CommonValues.BLUE_TOOTH_DIALOG, "正在与" + device.getName() + "链接");
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(CommonValues.STATE_CONNECTING);
    }

    private void setState(int stateListen) {
        mState = stateListen;
    }

    public void sendData(byte[] bytes) {
        TransferThread transferThread;
        synchronized (BluetoothChatDo.class) {
            if (mState != CommonValues.STATE_TRANSFER) {
                return;
            }
            transferThread = mTransferThread;
            transferThread.write(bytes);
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        private AcceptThread() {
            LogUtils.e("AcceptThread类的构造函数");
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* try {
                Method method = mBluetoothAdapter.getClass().getMethod("listenUsingRfcommOn",new Class[]{int.class});
                try {
                    tmp = (BluetoothServerSocket) method.invoke(mBluetoothAdapter,new Object[]{29});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }*/
            serverSocket = tmp;
        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket socket = null;
            while (mState != CommonValues.STATE_TRANSFER) {
                LogUtils.e("已进入AcceptThread.run()方法里面的死循环");
                try {
                    if (serverSocket != null) {
                        socket = serverSocket.accept();
                        LogUtils.e("已进入AcceptThread.run()方法里面的死循环,name="+socket.getRemoteDevice().getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (socket != null) {
                    LogUtils.e("已进入AcceptThread.run()方法里面的socket非空判断");
                    synchronized (BluetoothChatDo.class) {
                        LogUtils.e("已进入AcceptThread.run()方法里面的程序锁");
                        switch (mState) {
                            case CommonValues.STATE_LISTEN:
                            case CommonValues.STATE_CONNECTING:
                                sendMessageToUi(CommonValues.BLUE_TOOTH_DIALOG, "正在与" + socket.getRemoteDevice().getName() + "通信");
                                dataTransfer(socket, socket.getRemoteDevice());
                                LogUtils.e("已进入AcceptThread.run()方法里面switch()判断");
                                break;
                            case CommonValues.STATE_NONE:
                            case CommonValues.STATE_TRANSFER:
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    LogUtils.e("Could not close this socket,error:" + e.getMessage());
                                }
                                LogUtils.e("已进入AcceptThread.run()方法里面switch()判断");
                                break;
                        }
                    }
                }
            }
            LogUtils.e("跳出已进入AcceptThread.run()方法里面的死循环,mState:"+mState);
        }

        public void cancel() {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private class TransferThread extends Thread {
        private final BluetoothSocket socket;
        private OutputStream out = null;
        private InputStream in = null;

        public TransferThread(BluetoothSocket socket) {
            this.socket = socket;
            LogUtils.e("TransferThread类的构造函数");
            OutputStream mOutputStream = null;
            InputStream mInputStream = null;
            if (socket != null) {
                try {
                    mOutputStream = socket.getOutputStream();
                    mInputStream = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out = mOutputStream;
            in = mInputStream;
            isTransferError = false;
        }

        @Override
        public void run() {
            super.run();
            LogUtils.e("TransferThread.run()方法");
            //读取数据
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                LogUtils.e("TransferThread.run()方法里面的死循环");
                try {
                    bytes = in.read(buffer);
                    mHandler.obtainMessage(CommonValues.BLUE_TOOTH_READ, bytes, -1, buffer).sendToTarget();
                    LogUtils.e("read:" + new String(buffer, 0, bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                    if (count < 3) {
                        BluetoothChatDo.this.start();
                    } else {
                        sendMessageToUi(CommonValues.BLUE_TOOTH_TOAST, "设备连接失败/传输关闭");
                    }
                    count++;
                    isTransferError = true;
                    break;
                }
            }
            LogUtils.e("跳出TransferThread.run()方法里面的死循环");
        }

        public void write(byte[] buffer) {
            try {
                out.write(buffer);
                mHandler.obtainMessage(CommonValues.BLUE_TOOTH_WRITE, -1, -1, buffer).sendToTarget();
                LogUtils.e("write:" + new String(buffer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            if (socket != null) {
                try {
                    socket.close();
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private BluetoothDevice device;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
//                Method method = device.getClass().getMethod("createRfcommSocket",new Class[]{int.class});
//                tmp = (BluetoothSocket) method.invoke(device,new Object[]{29});
            } catch (Exception e) {
                e.printStackTrace();
                sendMessageToUi(CommonValues.BLUE_TOOTH_TOAST, "链接失败,请重新连接");
            }
            socket = tmp;
        }

        @Override
        public void run() {
            super.run();
            //建立连接后，停止扫描设备
            mBluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    LogUtils.e("Could not to close the socket,Error:" + e.getMessage());
                }
                sendMessageToUi(CommonValues.BLUE_TOOTH_TOAST, "连接失败，重新开始链接");
                BluetoothChatDo.this.start();
            }
            synchronized (BluetoothChatDo.class) {
                mConnectThread = null;
            }
            LogUtils.e("connectThread已经连接成功，准备传输数据");
            dataTransfer(socket, device);
        }

        public void cancel() {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void dataTransfer(BluetoothSocket socket, final BluetoothDevice device) {
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        //启动管理链接线程和开启传输
        mTransferThread = new TransferThread(socket);
        mTransferThread.start();
        setState(CommonValues.STATE_TRANSFER);//标记状态为链接
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTransferError) {
                    sendMessageToUi(CommonValues.BLUE_TOOTH_SUCCESS, device.getName());
                }
            }
        }, 300);
    }

    private void sendMessageToUi(int what, String content) {
        Message message = Message.obtain();
        message.what = what;
        message.obj = content;
        mHandler.sendMessage(message);
    }
}
