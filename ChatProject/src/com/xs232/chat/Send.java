package com.xs232.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * ������Ϣ
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
			//��������
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
	//������Ϣ
	private void send(String msg) {
		try {
			dos.writeUTF(msg);
			dos.flush();
		}catch(IOException e){
			release();
		}
	}
	//�ͷ���Դ
	private void release() {
		this.isRunning=false;
		Utils.close(dos,client);
	}
}
