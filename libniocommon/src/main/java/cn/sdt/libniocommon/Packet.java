package cn.sdt.libniocommon;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class Packet implements Parcelable {

    protected final static int BASE_ID = 100;


    public final static int HEART_ID = 1;
    public final static int HEART_RESPONSE_ID = HEART_ID + 1;

    public final static int REGIST_ID = BASE_ID + 1;
    public final static int REGIST_RESPONSE_ID = BASE_ID + 2;

    public final static int LOGIN_ID = BASE_ID + 11;
    public final static int LOGIN_RESPONSE_ID = BASE_ID + 12;

    public final static int CHAT_ID = BASE_ID + 21;
    public final static int CHAT_RESPONSE_ID = BASE_ID + 22;


    protected int pId;
    protected ArrayMap<String, String> params;

    public Packet(int pId) {
        this.pId = pId;
        params = new ArrayMap<>();
    }

    protected Packet(Parcel in) {
        pId = in.readInt();
    }

    public static final Creator<Packet> CREATOR = new Creator<Packet>() {
        @Override
        public Packet createFromParcel(Parcel in) {
            return new Packet(in);
        }

        @Override
        public Packet[] newArray(int size) {
            return new Packet[size];
        }
    };

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }


    public ArrayMap<String, String> getParams() {
        return params;
    }

    public void setParams(ArrayMap<String, String> params) {
        this.params = params;
    }

    public void add(String key, String value) {
        params.put(key, value);
    }


    public String getValue(String key) {
        return params.get(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pId);
    }


    public String toString() {
        return super.toString();
    }
}
