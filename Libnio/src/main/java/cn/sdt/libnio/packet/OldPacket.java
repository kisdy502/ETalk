package cn.sdt.libnio.packet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public class OldPacket implements Parcelable {

    protected int packetType;
    PacketHeader pHeader;
    PacketBody pBody;

    protected OldPacket(Parcel in) {
        packetType = in.readInt();
    }

    public static final Creator<OldPacket> CREATOR = new Creator<OldPacket>() {
        @Override
        public OldPacket createFromParcel(Parcel in) {
            return new OldPacket(in);
        }

        @Override
        public OldPacket[] newArray(int size) {
            return new OldPacket[size];
        }
    };

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(packetType);
    }

    public static class PacketHeader {
    }

    public static class PacketBody {

    }
}
