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
		   System.out.println("һ���ͻ����ѱ����");
		   new Thread(new Channel(client)).start();
	   }
	}
}


