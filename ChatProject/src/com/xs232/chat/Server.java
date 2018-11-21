package com.xs232.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

 
public class Server {
	private static CopyOnWriteArrayList<Channel> all=new CopyOnWriteArrayList<Channel>();
	private static List<String> names=new ArrayList<String>();
	public static void main(String[] args)throws Exception{
		System.out.println("----Server----");
		ServerSocket server = new ServerSocket(9999);
		while(true) {
			Socket client = server.accept();
			System.out.println("һ���ͻ��˽���������");
			Channel c=new Channel(client);
			all.add(c);
			new Thread(c).start();
		}
	}
	//һ���ͻ��˴���һ��Channel
	static class Channel implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket client;
		private boolean isRunning;
		private String name;
		
		
		public Channel(Socket client) {
			this.client = client;
			try {
				dis = new DataInputStream(client.getInputStream());
				dos=new DataOutputStream(client.getOutputStream());
				isRunning=true;
				this.name=receive();
				for(String s:names) {
					if(s.equals(name)) {
						this.send("false");
						this.name="";
						return;
					}	
				}
				this.send("true");
				names.add(name);
				System.out.println("name:"+name);
				sendOthers("------"+this.name+"����������------",true);
			} catch (IOException e1) {
				release();
			}
		}	
		//������Ϣ
		private String receive() {
			String msg="";
			try {
				msg=dis.readUTF();
			} catch (IOException e) {
				release();
			}
			return msg;
		}
		//������Ϣ
		private void send(String msg) {
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				release();
			}
		}
		/**
		 * Ⱥ�ģ���ȡ�Լ�����Ϣ������������
		 * 
		 */
		private void sendOthers(String msg,boolean isSys) {
			for(Channel other: all) {
				if(!isSys) {
					if(other==this)  //�Լ�
						other.send("�ң�"+msg);
					else
						other.send(this.name +"��"+msg);//Ⱥ����Ϣ
				}else 
					other.send(msg); //ϵͳ��Ϣ
			}
		}
		//�ͷ���Դ
		private void release() {
			this.isRunning=false;
			Utils.close(dis,dos,client);
			all.remove(this);
			if(this.name!="")
				sendOthers("------"+this.name+"�뿪������------",true);
		}
		//�߳�
		public void run() {
			while(isRunning) {
				String msg=receive();
				if(!msg.equals("")) 
					sendOthers(msg,false);
			}
		}
	}
}
