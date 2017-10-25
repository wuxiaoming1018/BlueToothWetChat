package diy.xiaoming.com.bluetoothwetchat.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.ming.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import diy.xiaoming.com.bluetoothwetchat.R;
import diy.xiaoming.com.bluetoothwetchat.adapter.RecyclerChatAdapter;
import diy.xiaoming.com.bluetoothwetchat.bean.ChatInfo;
import diy.xiaoming.com.bluetoothwetchat.business.BluetoothChatDo;
import diy.xiaoming.com.bluetoothwetchat.database.SQLHelper;
import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;
import diy.xiaoming.com.bluetoothwetchat.util.MD5Util;

import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_CONTENT;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_ID;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_NAME;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.COLUMN_TAG;
import static diy.xiaoming.com.bluetoothwetchat.util.CommonValues.TABLE_NAME;

/**
 * Created by Administrator on 2017-10-24.
 */

public class ChatActivity extends BaseActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private RecyclerChatAdapter mChatAdapter;
    private List<ChatInfo> mList;
    private SQLHelper helper;
    private BluetoothChatDo mDo;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommonValues.BLUE_TOOTH_TOAST:
                    ToastUtils.showShort((String) msg.obj);
                    finish();
                    break;
                case CommonValues.BLUE_TOOTH_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mList.add(new ChatInfo(CommonValues.TAG_LEFT, deviceName, readMessage));
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mList.size());
                    break;
                case CommonValues.BLUE_TOOTH_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mList.add(new ChatInfo(CommonValues.TAG_RIGHT, "我", writeMessage));
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mList.size());
                    break;
                case CommonValues.BLUE_TOOTH_CHAT_UPDATE:
                    mChatAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mList.size());
                    break;
            }
        }
    };
    private String deviceName;

    @Override
    public int bindLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initView(View view) {
        mContext = this;
        mEditText = bindWidget(R.id.et_write);
        mRecyclerView = bindWidget(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void initData() {
        deviceName = getIntent().getStringExtra(CommonValues.CHART_EXTRA);
        setTitle(deviceName);
        mList = new ArrayList<>();
        mChatAdapter = new RecyclerChatAdapter(mContext);
    }

    @Override
    public void initArgument(Bundle bundle) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {
        mChatAdapter.setData(mList);
        mRecyclerView.setAdapter(mChatAdapter);
        helper = new SQLHelper(mContext);
        new ModelThread(CommonValues.CHAT_READ_DATA).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDo = BluetoothChatDo.getInstance(mHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDo.stop();
        new ModelThread(CommonValues.CHAT_WRITE_DATA).start();
    }

    public void send(View view) {
        String content = mEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            if (mDo == null) {
                mDo = BluetoothChatDo.getInstance(mHandler);
            }
            mDo.sendData(content.getBytes());
            mEditText.setText("");
        } else {
            ToastUtils.showShort("发送的消息不能为空");
        }
    }

    class ModelThread extends Thread {

        private int tag;

        public ModelThread(int tag) {
            this.tag = tag;
        }

        @Override
        public void run() {
            super.run();
            if (tag == CommonValues.CHAT_READ_DATA) {
                readData();
            } else if (tag == CommonValues.CHAT_WRITE_DATA) {
                saveData();
            }
        }
    }

    private void saveData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = ? ", new String[]{MD5Util.stringToMD5(deviceName + "我")});
        for (ChatInfo chatInfo : mList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, MD5Util.stringToMD5(deviceName + "我"));
            values.put(COLUMN_TAG, chatInfo.getTag());
            values.put(COLUMN_NAME, chatInfo.getName());
            values.put(COLUMN_CONTENT, chatInfo.getContent());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    private void readData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ID + " = ?",
                new String[]{MD5Util.stringToMD5(deviceName + "我")});
        if (cursor.moveToFirst()) {
            do {
                ChatInfo chatInfo = new ChatInfo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TAG))),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                mList.add(chatInfo);
            } while (cursor.moveToNext());
        }
        mHandler.sendEmptyMessage(CommonValues.BLUE_TOOTH_CHAT_UPDATE);
        cursor.close();
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
