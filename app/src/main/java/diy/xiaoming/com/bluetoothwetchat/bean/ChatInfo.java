package diy.xiaoming.com.bluetoothwetchat.bean;

/**
 * Created by Administrator on 2017-10-24.
 */

public class ChatInfo {
    private int tag;
    private String name;
    private String content;

    public ChatInfo(int tag,String name,String content){
        this.tag = tag;
        this.name = name;
        this.content = content;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
