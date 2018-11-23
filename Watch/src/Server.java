import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	public static void main(String[] args) throws Exception
	{
	   System.out.println("----Server----");
	   
	   ServerSocket server = new ServerSocket(8989);
	   while(true)
	   {
		   Socket client = server.accept();
		   System.out.println("一个客户端已被监控");
		   new Thread(new Channel(client)).start();
	   }
	}
}


