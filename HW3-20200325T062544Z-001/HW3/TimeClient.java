import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

public class TimeClient
{
	public static void main(String [] args)
	{
		if(args.length < 2)
		{
			System.err.println("Usage: java TimeClient <port> <serverhost> [<serverhost>...]");
			System.exit(1);
		}
		
		
		int port = Integer.parseInt(args[0]);
		
		System.out.println("Going through the server list to connect...");
		for(int i=1;i<args.length;i++)
		{
			System.out.println("Trying to connect to the host: " + args[i]);
			
			String host = args[i];
			try
			{
				Socket s  = new Socket(host, port);
				
				InputStream in = s.getInputStream();
				ObjectInput oin = new ObjectInputStream(in);

				Date date = (Date) oin.readObject();
				System.out.println("Time on host " + host + " is " + date);
				
				return;
			}
			catch (IOException e)
			{
				System.out.println("Failed to connect to the server: " + host);
			}
			catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException occured: " + e.getStackTrace());
			}
		}
	}

}
