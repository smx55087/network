import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server extends JFrame{
	public  BufferedImage img;
    private JPanel jPanel=new JPanel();
	public Server ()
	{
		this.setBounds(0,0,768,432);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	public static void main(String[] args) throws Exception
	{
	   Server s = new Server();
	   System.out.println("打开服务器…………");
	   ServerSocket server = new ServerSocket(8989);
	   int i=0;
	   System.out.println("服务器开启正常…………");
	   System.out.println("服务器正在接收桌面图片…………");
	   
	   while(true)
	   {
		   Socket client = server.accept();
		   new Thread(new Channel(client)).start();
	   }
	}
}
class Channel extends JFrame implements Runnable{
	public  BufferedImage img;
	private Socket client;
    private JPanel jPanel=new JPanel();
	public Channel (Socket client)
	{
		this.client=client;
		this.setBounds(0,0,768,432);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void run() {
		try {
			while(true){
				DataInputStream dis = new DataInputStream(client.getInputStream());
				int len=dis.readInt();//图片长度
				byte[] data=new byte[len];
				dis.readFully(data);
				ByteArrayInputStream bai=new ByteArrayInputStream(data);
				this.img=ImageIO.read(bai);
				MyJPanel my = new MyJPanel(this.img);
				this.add(my);
				this.setVisible(true); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class MyJPanel extends JPanel{	
	private Image img;
	public MyJPanel(Image img){
		this.img=img;
	}
	public void paint(Graphics g){
		g.drawImage(img,0,0,768,432,this); //画图片
	}
}
