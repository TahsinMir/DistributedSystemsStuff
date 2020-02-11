import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server
{
	private ServerSocket s;
	private InputStream in;
	private OutputStream out;
	
	public Server(int port)
	{
		try
		{
			s = new ServerSocket(port);
			System.out.println("TimeServer up and running on port " + port + " " + InetAddress.getLocalHost());
		}
		catch (UnknownHostException e) {
			System.err.println(e);
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
	}
	public void RunServer()
	{
		Socket newClient;
		
		try
		{
			newClient = s.accept();
			out = newClient.getOutputStream();
			in = newClient.getInputStream();
			
			
			System.out.println("Received connect from " + newClient.getInetAddress().getHostAddress() + ": " + newClient.getPort());
			
			System.out.println("Sending sample output to client");
			
			
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(new java.util.Date());
			oout.flush();
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
}
