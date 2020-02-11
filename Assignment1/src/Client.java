import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client
{
	private int port;
	private String host;
	private Socket s;
	
	public Client(int port, String host)
	{
		this.port = port;
		this.host = host;
		
		try {
			s = new Socket(this.host, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ExecuteClient()
	{
		try
		{
			InputStream in = s.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);

			Date date = (Date) oin.readObject();
			System.out.println("Time on host is " + date);
		}
		catch (IOException e1)
		{
			System.out.println(e1);
		}
		catch (ClassNotFoundException e2)
		{
			System.out.println(e2);
		}
	}

}
