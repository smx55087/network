package com.xs232.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 发送信息
 * 
 */
public class Send implements Runnable{
	private DataOutputStream dos;
	private Socket client;
	private boolean isRunning;
	private String name,msg="";
	
	public Send(Socket client,String name) {
		this.client=client;
		this.isRunning=true;
		this.name=name;
		try {
			dos=new DataOutputStream(client.getOutputStream());
			//发送名称
			send(name);
		} catch (IOException e) {
			this.release();
		}
	}
	public void setMsg(String msg) {
		this.msg=msg;
	}
	public void run() {
		while(isRunning) {	
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!msg.equals("")) {
				send(msg);
				msg="";
			}
		}
		
	}
	//发送消息
	private void send(String msg) {
		try {
			dos.writeUTF(msg);
			dos.flush();
		}catch(IOException e){
			release();
		}
	}
	//释放资源
	private void release() {
		this.isRunning=false;
		Utils.close(dos,client);
	}
}
