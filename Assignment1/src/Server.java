import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Server extends Thread
{
	private ServerSocket s;
	private InputStream in;
	private OutputStream out;
	private Ball ball;
	private Random random;
	private int clientToss;
	private int serverToss;
	private String message;
	
	public Server(int port)
	{
		ball = new Ball("Red");
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
			System.out.println("Received connect from " + newClient.getInetAddress().getHostAddress() + ": " + newClient.getPort());
			
			
			//
			//
			//sample coin toss
			boolean isDecided = false;
			random = new Random();
			while(!isDecided)
			{
				in = newClient.getInputStream();
				out = newClient.getOutputStream();
				
				//client goes first, and server receives first
				ObjectInputStream oin = new ObjectInputStream(in);
				int res = (int) oin.readObject();
				clientToss = res;
				
				sleep(1000);
				
				serverToss = Math.abs(random.nextInt()) % 2;
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(serverToss);
				oout.flush();
				
				
				System.out.println("Server coin toss: my number: " + serverToss + ", their number: " + clientToss);
				if(serverToss > clientToss)
				{
					message = "Ping";
					ball = new Ball("Red");
					ball.SetMessage(message);
					isDecided = true;
				}
				else if(clientToss > serverToss)
				{
					message = "Pong";
					isDecided = true;
				}
				else
				{
					System.out.println("There is a tie, toss again!");
					sleep(1000);
				}
			}
			//
			//
			
			System.out.println("Starting the play: ");
			while(true)
			{
				in = newClient.getInputStream();
				out = newClient.getOutputStream();
				
				if(message == "Ping")
				{
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(message);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Server sent: " + message);
					
					sleep(1000);
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Server received: " + rec.GetMessage());
					ball = rec;
				}
				else if(message == "Pong")
				{
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Server received: " + rec.GetMessage());
					ball = rec;
					
					sleep(1000);
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(message);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Server sent: " + message);
				}
				
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
