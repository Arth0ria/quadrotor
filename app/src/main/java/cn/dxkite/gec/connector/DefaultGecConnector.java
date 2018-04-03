package cn.dxkite.gec.connector;

import java.io.IOException;

/**
 * 默认信息边界
 *
 * @author DXkite
 */
public class DefaultGecConnector implements GecConnector {
    GecClient client;
    boolean connected  = false;

    public DefaultGecConnector() {

    }

    @Override
    public void onMessage(GecMessage message) {
        System.out.println(message);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public boolean send(GecMessage message) {
        try {
            client.send(message.toBytes());
            return true;
        } catch (IOException e) {
            onError(e);
            return false;
        }
    }

    public GecClient getClient() {
        return client;
    }

    public void setClient(GecClient client) {
        this.client = client;
    }

    @Override
    public void onConnected() {
        System.out.println("Connected");
        this.connected =true;
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void sendAsync(GecMessage message) {
        client.sendAsync(message.toBytes());
    }

    @Override
    public void sendSync(GecMessage message) {

    }

    @Override
    public GecMessage getMessage(GecMessage message) {
        return null;
    }
}
