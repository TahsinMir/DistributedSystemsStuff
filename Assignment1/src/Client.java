import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client extends Thread
{
	private int port;
	private String host;
	private Socket s;
	private int number;
	
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
	
	public void run()
	{
		try
		{
			while(true)
			{
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				ObjectInputStream oin = new ObjectInputStream(in);

				int rec = (int) oin.readObject();
				System.out.println("received number in client" + rec);
				number = rec;
				
				sleep(1000);
				
				// now sending the number back after incrementing
				number++;
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(number);
				oout.flush();
			}
		}
		catch (IOException e1)
		{
			System.out.println(e1);
		}
		catch (ClassNotFoundException e2)
		{
			System.out.println(e2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
