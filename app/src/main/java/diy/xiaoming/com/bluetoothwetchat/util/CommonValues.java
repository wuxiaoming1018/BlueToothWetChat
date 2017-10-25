package diy.xiaoming.com.bluetoothwetchat.util;

/**
 * Created by Administrator on 2017-10-20.
 */

public class CommonValues {

    public static int BLUETOOTH_TAG_NORMAL = 0x1000;

    public static int BLUETOOTH_TAG_TOAST = 0x1001;

    public final static int OPEN_BLUETOOTH = 0x1002;

    public final static int CONNECT_SUCCESS = 0X1003;

    public final static int CONNECT_FAILURE = 0X1004;

    public final static int CONNECT_BROKEN = 0x1005;

    //the state of connect
    public final static int STATE_NONE = 0x1006; //nothing to do

    public final static int STATE_LISTEN = 0x1007;// listener to connect

    public final static int STATE_CONNECTING = 0x1008;// connecting

    public final static int STATE_TRANSFER = 0x1009;// connect remote device

    public final static int BLUE_TOOTH_DIALOG = 0x1010;

    public final static int BLUE_TOOTH_TOAST = 0x1011;

    public final static int BLUE_TOOTH_WRITE = 0x1012;

    public final static int BLUE_TOOTH_READ = 0x1013;

    public final static int BLUE_TOOTH_SUCCESS = 0x1014;

    public final static int BLUE_TOOTH_CHAT_UPDATE = 0x1015;

    public static int TAG_LEFT = 0X1016;

    public static int TAG_RIGHT = 0X1017;

    public static int CHAT_READ_DATA = 0x1018;

    public static int CHAT_WRITE_DATA = 0x1019;

    public static String CHART_EXTRA = "chart_extra";

    public static String DB_NAME = "bluetooth_chat_db";

    public static String TABLE_NAME = "chat_table";

    public static String COLUMN_ID = "id";

    public static String COLUMN_NAME = "device_name";

    public static String COLUMN_TAG = "tag";

    public static String COLUMN_CONTENT = "content";
}
