package cn.dxkite.quadrotor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import cn.dxkite.gec.connector.GecClient;
import cn.dxkite.gec.connector.GecConnector;
import cn.dxkite.gec.connector.GecMessage;

public class QuadrotorService extends Service {

    final static String TAG = "QuadrotorService";
    QuadrotorClient client;
    QuadrotorBinder binder = new QuadrotorBinder();
    List<OnMessageReceivedListener> listeners = new ArrayList<>();
    Queue<GecMessage> messages = new ArrayDeque<>();
    GecMessage simple;
    GecConnector connector = new GecConnector() {
        boolean connected = false;

        @Override
        public void onMessage(GecMessage message) {
            for (OnMessageReceivedListener listener : listeners) {
                listener.onReceived(message);
            }
        }

        @Override
        public void onError(Exception e) {
            for (OnMessageReceivedListener listener : listeners) {
                listener.onError(e);
            }
        }

        @Override
        public boolean send(GecMessage message) {
            messages.add(message);
            return true;
        }

        @Override
        public void setClient(GecClient client) {
            return;
        }

        @Override
        public void onConnected() {
            connected = true;
            for (OnMessageReceivedListener listener : listeners) {
                listener.onConnected();
            }
        }

        @Override
        public void onDisconnected() {
            connected = false;
            for (OnMessageReceivedListener listener : listeners) {
                listener.onDisconnected();
            }
        }

        @Override
        public boolean isConnected() {
            return connected;
        }

        @Override
        public void sendAsync(GecMessage message) {
            if (QuadrotorService.this.client != null) {
                send(message);
            }
        }

        @Override
        public void sendSync(GecMessage message) {
            QuadrotorService.this.client.send(message);
        }

        @Override
        public GecMessage getMessage(GecMessage message) {
            byte[] msg = QuadrotorService.this.client.sendSync(message.toBytes());

            if (msg != null) {
                if (msg[0] == GecMessage.HEAD) {
                    return new GecMessage(msg);
                }
                else{
                    Log.d(TAG,"error receive ->" + GecMessage.byte2hex(msg,true));
                }
            }
            return null;
        }
    };
    private int packageFrequency = 5;
    private int packageCount = 0;
    private long lastTime = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder.connect();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }

    protected void countPackage() {
        ++packageCount;
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime > 1000) {
            for (OnMessageReceivedListener listener : listeners) {
                listener.setPackageFrequency(packageCount);
            }
            packageCount = 0;
            lastTime = curTime;
        }
    }

    public interface OnMessageReceivedListener {
        void onReceived(GecMessage message);

        void onConnected();

        void onDisconnected();

        void onServerConnected(String name,int address,String mac);

        void onError(Exception e);

        void setPackageFrequency(int frequency);
    }

    public class QuadrotorClient extends GecClient {
        public QuadrotorClient(String host, Integer port, GecConnector connector) {
            super(host, port, connector);
        }

        @Override
        public void send(byte[] message) throws IOException {
            super.send(message);
        }

        public void send(GecMessage message) {
            countPackage();
//            Log.d(TAG, "send bytes " + message.toHexString());
            try {
                send(message.toBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  class QuadrotorBinder extends Binder {

        public void connect() {
            new Thread() {
                @Override
                public void run() {
                    Log.d(TAG, "connecting quadrotor server");
                    client = new QuadrotorClient("192.168.4.1", 333, connector);
                    if (client.connecting()) {
                        Log.d(TAG, "connected quadrotor server");
                        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = wifiMgr.getConnectionInfo();
                        String wifiName = info != null ? info.getSSID() : null;
                        for (OnMessageReceivedListener listener : listeners) {
                            listener.onServerConnected(wifiName.substring(1, wifiName.length() - 1),info.getIpAddress(),info.getMacAddress());
                        }
                    }
                }
            }.start();
        }

        public void waitingMessage() {
            new Thread() {
                @Override
                public void run() {
                    if (client.connectTest()) {
                        Log.d(TAG, "test connection success");

                        client.waitingMessage();

                        while (client.isConnect()) {
                            long startTime = System.currentTimeMillis();
                            if (messages.size() > 0) {
                                GecMessage message= messages.poll();
                                if (!message.isType(GecMessage.WRITE)) {
                                    Log.e(TAG,"send -> "+message.toHexString());
                                }
                                client.send(message);
                            } else {
                                if (simple != null) {
                                    connector.send(simple);
                                }
                            }

                            long endTime = System.currentTimeMillis();
                            int diffTime = (int) (endTime - startTime);
                            while (diffTime <= packageFrequency) {
                                diffTime = (int) (System.currentTimeMillis() - startTime);
                                Thread.yield();
                            }
                        }
                    } else {
                        Log.d(TAG, "test connection failed");
                    }
                }
            }.start();
        }


        public void disconnect() {
            client.disconnect();
        }

        public boolean send(GecMessage message) {
            if (message.isType(GecMessage.WRITE)) {
                simple.setPower(message.getPower());
            }
            return connector.send(message);
        }
        public void setPackageFrequency(int pf) {
            packageFrequency = pf;
        }

        public GecMessage sendAndReturn(GecMessage message) {
           return connector.getMessage(message);
        }

        public GecMessage getSimple() {
            return simple;
        }

        public void setSimple(GecMessage simple) {
            QuadrotorService.this.simple = simple;
        }

        public void addMessageReceivedListener(OnMessageReceivedListener listener) {
            listeners.add(listener);
        }
    }
}
