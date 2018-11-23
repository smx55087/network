
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Channel extends JFrame implements Runnable{
	public  BufferedImage img;
	private volatile boolean isBoolean=true;
	private Socket client;
	private JFrame jf;
	private int i=0;
	public Channel (Socket client)
	{
		jf = new JFrame();
		jf.setBounds(0,0,960,540);
		this.client=client;
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			isBoolean=false;
			jf.dispose();
			 } 
		});
	}
	
	public void run() {
		DataInputStream dis=null;
		ByteArrayInputStream bai =null;
		try {
			while(isBoolean){
				dis = new DataInputStream(client.getInputStream());
				int len=dis.readInt();//Í¼Æ¬³¤¶È
				byte[] data=new byte[len];
				dis.readFully(data);
				bai=new ByteArrayInputStream(data);
				this.img=ImageIO.read(bai);
				MyJPanel my = new MyJPanel(this.img);
				jf.add(my);
				if(isBoolean)
					jf.setVisible(true); 
				jf.remove(my);
				dis=null;
				bai=null;
				this.img=null;
				my=null;
				System.gc();
			}
		} catch (Exception e) {
			Utils.close(dis,bai,client);
		}
		jf.dispose();
	}
}
class MyJPanel extends JPanel{	
	private Image img;
	public MyJPanel(Image img){
		this.img=img;
	}
	public void paint(Graphics g){
		g.drawImage(img,0,0,960,540,this); //»­Í¼Æ¬		
		g.dispose();
	}
}
