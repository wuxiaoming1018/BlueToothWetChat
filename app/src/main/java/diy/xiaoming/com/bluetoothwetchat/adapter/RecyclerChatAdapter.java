package diy.xiaoming.com.bluetoothwetchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import diy.xiaoming.com.bluetoothwetchat.R;
import diy.xiaoming.com.bluetoothwetchat.bean.ChatInfo;
import diy.xiaoming.com.bluetoothwetchat.holdview.ChatLeftHolder;
import diy.xiaoming.com.bluetoothwetchat.holdview.ChatRightHolder;
import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

/**
 * Created by Administrator on 2017-10-24.
 */

public class RecyclerChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatInfo> mList;

    public void setData(List<ChatInfo> mList){
        this.mList = mList;
    }

    public RecyclerChatAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType== CommonValues.TAG_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_chat_left,parent,false);
            return new ChatLeftHolder(view);
        }else if(viewType== CommonValues.TAG_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_chat_right,parent,false);
            return new ChatRightHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position).getTag()== CommonValues.TAG_LEFT) {
            ChatLeftHolder leftHolder = (ChatLeftHolder) holder;
            leftHolder.getTvName().setText(mList.get(position).getName());
            leftHolder.getTvContent().setText(mList.get(position).getContent());
        }else if(mList.get(position).getTag()==CommonValues.TAG_RIGHT){
            ChatRightHolder rightHolder = (ChatRightHolder) holder;
            rightHolder.getTvName().setText(mList.get(position).getName());
            rightHolder.getTvContent().setText(mList.get(position).getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getTag();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

}
