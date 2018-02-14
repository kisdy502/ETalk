package cn.sdt.etalk;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class ChatMsg {
    String userName;
    boolean isMe;
    String msgBody;

    public ChatMsg(String userName, boolean isMe, String msgBody) {
        this.userName = userName;
        this.isMe = isMe;
        this.msgBody = msgBody;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
}
