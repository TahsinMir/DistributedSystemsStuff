import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

public class Client extends Thread
{
	private int port;
	private InputStream in;
	private OutputStream out;
	private String host;
	private Socket s;
	private Ball ball;
	private int clientToss;
	private int serverToss;
	private Random random;
	private String message;
	
	public Client(int port, String host)
	{
		this.port = port;
		this.host = host;
		
		try
		{
			s = new Socket(this.host, this.port);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Unknown Host Exception: " + e);
		}
		catch (IOException e)
		{
			System.out.println("IO Exception: " + e);
		}
	}
	
	public void run()
	{
		try
		{
			//Coin toss
			boolean isDecided = false;
			random = new Random();
			while(!isDecided)
			{
				in = s.getInputStream();
				out = s.getOutputStream();
				
				//By design client goes first for the coin toss part
				clientToss = Math.abs(random.nextInt()) % 2;
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(clientToss);
				oout.flush();
				
				sleep(1000);
				
				ObjectInputStream oin = new ObjectInputStream(in);
				int res = (int) oin.readObject();
				serverToss = res;
				
				System.out.println("Client coin toss: my number: " + clientToss + ", their number: " + serverToss);
				if(serverToss > clientToss)
				{
					message = "Pong";
					isDecided = true;
				}
				else if(clientToss > serverToss)
				{
					message = "Ping";
					ball = new Ball(message);
					isDecided = true;
				}
				else
				{
					System.out.println("There is a tie, tossing again!");
					sleep(1000);
				}
			}
			while(true)
			{
				//If client won the toss, client makes the first move
				if(message == "Ping")
				{
					in = s.getInputStream();
					out = s.getOutputStream();
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(message);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Client sent: " + message);
					
					sleep(1000);
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Client received: " + rec.GetMessage());
					ball = rec;
				}
				//If client lost the toss, client waits for the server to make the first move
				else if(message == "Pong")
				{
					in = s.getInputStream();
					out = s.getOutputStream();
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Client received: " + rec.GetMessage());
					ball = rec;
					
					sleep(1000);
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(message);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Client sent: " + message);
				}
				
			}
		}
		catch (IOException e)
		{
			System.out.println("IO Exception occured: " + e);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Class Not Found Exception occured: " + e);
		} catch (InterruptedException e)
		{
			System.out.println("Interrupted Exception occured: " + e);
		}
	}

}
