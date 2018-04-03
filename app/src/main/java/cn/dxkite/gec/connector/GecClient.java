package cn.dxkite.gec.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 * 无人机客户端
 *
 * @author DXkite
 */
public class GecClient {

    protected Socket socket;
    protected GecConnector connector;
    protected boolean running = true;
    InputStream input;
    OutputStream output;
    final String test = "GEC\r\n";
    final int dataLength = 34;
    protected String host;
    protected int port;

    public GecClient() {
        this("192.168.4.1", 333);
    }

    public GecClient(String host, Integer port) {
        this(host, port, new DefaultGecConnector());
    }

    public GecClient(String host, Integer port, GecConnector connector) {
        this.host = host;
        this.port = port;
        this.connector = connector;
    }

    /**
     * 测试链接是否可用
     *
     * @return 连接状态
     */
    public boolean connectTest() {
        try {
            if (output == null) {
                return false;
            }
            output.write(test.getBytes());
            byte[] data = new byte[dataLength];
            if (input.read(data) == dataLength) {
                if (data[1] == 0x50) {
                    connector.onConnected();
                } else {
                    return false;
                }
                return true;
            }
            return false;
        } catch (IOException e) {
            connector.onError(e);
            return false;
        }
    }

    public boolean connecting() {
        try {
            System.out.println("connecting...");
            socket = new Socket(host, port);
            System.out.println("connected");
            socket.setSoTimeout(3000);
            input = socket.getInputStream();
            output = socket.getOutputStream();
            connector.setClient(this);
        } catch (Exception e) {
            connector.onError(e);
            return false;
        }
        return true;
    }

    public void waitingMessage() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("listen message start ...");
                while (running) {
                    readIfAvailable();
                }
                System.out.println("listen message closed ...");
            }
        }.start();
    }

    public boolean isConnect() {
        return socket != null;
    }

    private void readIfAvailable() {
        try {

            if (input.available() > 0) {
                byte[] data = new byte[34];
                int length = input.read(data);
                byte head = data[0];
                byte allow = (byte) 0xAA;
                if (head == allow) {
                    connector.onMessage(new GecMessage(data));
                } else if (length > 1) {
                    String read = GecMessage.byte2hex(data, true, length);
                    System.err.println("received " + length + " -> " + read);
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("read message timeout");
        } catch (IOException e) {
            System.out.println("read message error");
            connector.onError(e);
        }
    }

    public void sendAsync(final byte[] message) {
        if (output != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        send(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void send(byte[] message) throws IOException {
        if (output != null) {
            try {
                output.write(message);
                readIfAvailable();
            } catch (SocketException e) {
                disconnect();
            }
        }
    }

    public byte[] sendSync(byte[] message) {
        try {
            send(message);
            byte[] data = new byte[34];
            int length = input.read(data);
            if (length > 0) {
                return data;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                connector.onDisconnected();
                running = false;
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
