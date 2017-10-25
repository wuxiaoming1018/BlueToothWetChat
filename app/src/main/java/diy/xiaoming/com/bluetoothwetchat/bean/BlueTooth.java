package diy.xiaoming.com.bluetoothwetchat.bean;

import diy.xiaoming.com.bluetoothwetchat.util.CommonValues;

/**
 * Created by Administrator on 2017-10-20.
 */

public class BlueTooth {
    private int tag;
    private String name;
    private String mac;
    private String rssi;

    public BlueTooth(String name,int tag){
        this.name = name;
        this.tag = tag;
    }

    public BlueTooth(String name, String mac, String rssi){
        tag = CommonValues.BLUETOOTH_TAG_NORMAL;
        this.name = name;
        this.mac = mac;
        this.rssi = rssi;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}
