package cn.sdt.libniocommon;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public abstract class NIOAcceptor {

    protected int port;
    protected volatile boolean isRunning = true;
    protected Selector mSelector;

    protected abstract void start() throws IOException;

    protected abstract void stop();

    public boolean isRunning() {
        return isRunning;
    }
}
