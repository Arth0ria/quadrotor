package cn.dxkite.gec.connector;

import java.io.OutputStream;

/**
 * 信息连接器
 * 
 * 发送/接收 无人机 信息
 * @author DXkite
 *
 */
public interface GecConnector {
	
	/**
	 * 无人机发送信息时被调用
	 * @param message
	 */
	void onMessage(GecMessage message);
	
	/**
	 * 产生错误时被调用
	 * @param e
	 */
	void onError(Exception e);
	
	/**
	 * 发送信息到无人机
	 * @param message
	 * @return
	 */
	boolean send(GecMessage message);
	
	/**
	 * 设置客服端
	 * @param client
	 */
	void setClient(GecClient client);
	/**
	 * 连接成功
	 */
	void onConnected();
	void onDisconnected();
	boolean isConnected();
	void sendAsync(GecMessage message);
	void sendSync(GecMessage message);
	GecMessage getMessage(GecMessage message);
}
