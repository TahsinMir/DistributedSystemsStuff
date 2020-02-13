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
	private Socket server;
	private Ball ball;
	private int clientToss;
	private int serverToss;
	private Random random;
	private String clientMessage;
	
	public Client(int port, String host)
	{
		this.port = port;
		this.host = host;
		
		try
		{
			server = new Socket(this.host, this.port);
			System.out.println("Connected to server!");
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
				in = server.getInputStream();
				out = server.getOutputStream();
				
				//By design client goes first, then goes the server, and then the outputs are compared
				clientToss = Math.abs(random.nextInt()) % 2;
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(clientToss);
				oout.flush();
				
				sleep(1000);
				
				ObjectInputStream oin = new ObjectInputStream(in);
				int res = (int) oin.readObject();
				serverToss = res;
				
				System.out.println("Client coin toss: my number: " + clientToss + ", server number: " + serverToss);
				if(serverToss > clientToss)
				{
					clientMessage = "Pong";
					isDecided = true;
				}
				else if(clientToss > serverToss)
				{
					clientMessage = "Ping";
					ball = new Ball(clientMessage);
					isDecided = true;
				}
				else
				{
					System.out.println("There is a tie, tossing again!");
					sleep(1000);
				}
			}
			
			if(clientMessage == "Pong")
			{
				System.out.println("Server won the toss");
				System.out.println("Therefore passing the ball to server to start the game...");
				
				in = server.getInputStream();
				out = server.getOutputStream();
				
				ObjectOutputStream oout = new ObjectOutputStream(out);
				ball = new Ball();
				oout.writeObject(ball);
				oout.flush();
			}
			while(true)
			{
				//If client won the toss, client makes the first move
				if(clientMessage == "Ping")
				{
					in = server.getInputStream();
					out = server.getOutputStream();
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(clientMessage);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Client sent: " + clientMessage);
					
					sleep(1000);
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Client received: " + rec.GetMessage());
					ball = rec;
				}
				//If client lost the toss, client waits for the server to make the first move
				else if(clientMessage == "Pong")
				{
					in = server.getInputStream();
					out = server.getOutputStream();
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Client received: " + rec.GetMessage());
					ball = rec;
					
					sleep(1000);
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(clientMessage);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Client sent: " + clientMessage);
				}
				
			}
		}
		catch (IOException e)
		{
			System.out.println("IO Exception occured: " + e);
			System.out.println("Game Over!");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Class Not Found Exception occured: " + e);
		}
		catch (InterruptedException e)
		{
			System.out.println("Interrupted Exception occured: " + e);
			System.out.println("Game Over!");
		}
	}

}
