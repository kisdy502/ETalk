package cn.sdt.libnio;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public class Connection {

    private int connId;
    private String connName;
    private String connAddress;

    public int getConnId() {
        return connId;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public String getConnAddress() {
        return connAddress;
    }

    public void setConnAddress(String connAddress) {
        this.connAddress = connAddress;
    }
}
