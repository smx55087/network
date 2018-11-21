package com.xs232.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * 接受信息
 *
 */
public class Receive implements Runnable{
	private DataInputStream dis;
	private Socket client;
	private boolean isRunning;
	private JTextArea ta;

	public Receive(Socket client,JTextArea ta) {
		this.client=client;
		this.isRunning = true;
		this.ta=ta;
		try {
			dis=new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			release();
		}
	}
	//接收消息
	public String receive() {
		String msg="";
		try {
			msg=dis.readUTF();
		} catch (IOException e) {
			release();
		}
		return msg;
	}
	public void run() {
		while(isRunning) {
			String msg=receive();
			if(!msg.equals(""))
				ta.append(msg+"\r\n");
		}
		
	}
	//释放资源
	private void release() {
		this.isRunning=false;
		Utils.close(dis,client);
	}
}
