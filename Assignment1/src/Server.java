import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends Thread
{
	private ServerSocket s;
	private InputStream in;
	private OutputStream out;
	private int number;
	
	public Server(int port)
	{
		number = 0;
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
	public void run()
	{
		Socket newClient;
		
		try
		{
			newClient = s.accept();
			out = newClient.getOutputStream();
			in = newClient.getInputStream();
			
			
			System.out.println("Received connect from " + newClient.getInetAddress().getHostAddress() + ": " + newClient.getPort());
			
			
			System.out.println("Starting the play: ");
			while(true)
			{
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(++number);
				oout.flush();
				
				sleep(1000);
				
				ObjectInputStream oin = new ObjectInputStream(in);
				int rec = (int) oin.readObject();
				System.out.println("received number in server " + rec);
				number = rec;
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
