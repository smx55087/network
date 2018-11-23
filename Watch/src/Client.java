import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
public class Client extends Thread{

	private Socket socket = null;
	private BufferedImage image;
	
	
	public Client(){
		  System.out.println("正在连接服务器");
		   try {
		    socket = new Socket("192.168.0.106",8989);
		   } catch (Exception e) {
			   System.out.println("找不到服务器....");
			   e.printStackTrace();
		   }
		  System.out.println("服务器连接成功");
	}


	public void run()
	{
	  int i=0;
	  while(true)
	  {
	   try {
		Thread.sleep(100);
	    System.out.println(i++);
	    image = getDeskTop();
	    sendImage(image);
	   } catch (Exception e) {
	
	    e.printStackTrace();
	   }
	  }
	}

	public BufferedImage getDeskTop()throws Exception
	{
	  Robot robot = new Robot();
	  Rectangle re = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	  
	  return robot.createScreenCapture(re);
	}
	
	
	public void sendImage(BufferedImage image)throws Exception
	{
		ByteArrayOutputStream  baos= new ByteArrayOutputStream(); 
	    ImageIO.write(image,"jpg",baos);
	    DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
	    byte[] data = baos.toByteArray();
		dos.writeInt(data.length);
		dos.write(data);
	    dos.flush();
	}
	
	
	public static void main(String[] args)throws Exception
	{
		Client client = new Client();
		new Thread(client).start();
		
	}
}