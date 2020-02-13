import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class Server
{
	private ServerSocket serverSocket;
	private int numOfClients;
	
	public Server(int port)
	{
		numOfClients = 0;
		try
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Server is up and running on port " + port + " " + InetAddress.getLocalHost());
			System.out.println("Server is waiting for clients to connect.....");
		}
		catch (UnknownHostException e)
		{
			System.err.println("Unknown Host Exception occured: " + e);
		}
		catch (IOException e)
		{
			System.err.println("IO Exception occured: " + e);
		}
	}
	public void RunServer()
	{
		Socket client;
		try
		{
			while (true) {
				client = serverSocket.accept();
				System.out.println("Received connection from " + client.getInetAddress().getHostName() + " [ "
						+ client.getInetAddress().getHostAddress() + " ] ");
				new ServerConnection(client, numOfClients+1).start();
				//this is the variable that gives the games/ clients a number
				//by design of this, this value is being sent to the threads through the ServerConnection constructor
				//After that the threads access its own game no, and not this value directly
				numOfClients++;
			}
		}
		catch (IOException e)
		{
			System.err.println("IO Exception occured: " + e);
		}
	}
}

class ServerConnection extends Thread {
	private Socket newClient;
	private int clientNo;
	private int gameNo;
	private InputStream in;
	private OutputStream out;
	private Ball ball;
	private Random random;
	private int clientToss;
	private int serverToss;
	private String serverMessage;

	ServerConnection(Socket client, int clientNo) throws SocketException 
	{
		this.newClient = client;
		// A particular thread of the server keeps track of its own game and client no
		this.clientNo = clientNo;
		this.gameNo = this.clientNo;
		setPriority(NORM_PRIORITY - 1);
		System.out.println("Created thread " + this.getName());
	}

	public void run()
	{
		try
		{
			//coin toss
			boolean isDecided = false;
			random = new Random();
			while(!isDecided)
			{
				in = newClient.getInputStream();
				out = newClient.getOutputStream();
				
				//client goes first, and then goes the server, the one with the higher value wins
				ObjectInputStream oin = new ObjectInputStream(in);
				int res = (int) oin.readObject();
				clientToss = res;
				
				sleep(1000);
				
				serverToss = Math.abs(random.nextInt()) % 2;
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(serverToss);
				oout.flush();
				
				
				System.out.println("Server coin toss: my number: " + serverToss + ", " + "Client#" + clientNo + " number: " + clientToss);
				if(serverToss > clientToss)
				{
					serverMessage = "Ping";
					ball = new Ball(serverMessage);
					isDecided = true;
				}
				else if(clientToss > serverToss)
				{
					serverMessage = "Pong";
					isDecided = true;
				}
				else
				{
					System.out.println("There is a tie, toss again!");
					sleep(1000);
				}
			}
			
			if(serverMessage == "Ping")
			{
				System.out.println("Getting the ball from the Client: ");
				sleep(500);
				
				in = newClient.getInputStream();
				out = newClient.getOutputStream();
				
				ObjectInputStream oin = new ObjectInputStream(in);
				Ball rec = (Ball) oin.readObject();
				System.out.println("Ball received from Client#" + clientNo + ", test: " + ball.message);
				ball = rec;
			}
			
			
			System.out.println("The game is starting!");
			while(true)
			{
				in = newClient.getInputStream();
				out = newClient.getOutputStream();
				
				// if message is ping, that means server starts the game
				if(serverMessage == "Ping")
				{
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(serverMessage);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Game#"+ gameNo + " Server sent to Client#" + clientNo + ": " + serverMessage);
					
					sleep(1000);
					
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Game#"+ gameNo + " Server received from Client#" + clientNo + ": " + rec.GetMessage());
					ball = rec;
				}
				else if(serverMessage == "Pong")
				{
					ObjectInputStream oin = new ObjectInputStream(in);
					Ball rec = (Ball) oin.readObject();
					System.out.println("Game#"+ gameNo + " Server received from Client#" + clientNo + ": " + rec.GetMessage());
					ball = rec;
					
					sleep(1000);
					
					ObjectOutputStream oout = new ObjectOutputStream(out);
					ball.SetMessage(serverMessage);
					oout.writeObject(ball);
					oout.flush();
					System.out.println("Game#"+ gameNo + " Server sent to Client#" + clientNo + ": " + serverMessage);
				}
				
			}
		}
		catch(IOException e)
		{
			System.err.println("IO Exception occured: " + e);
			System.out.println("Game Over!");
		}
		catch (InterruptedException e)
		{
			System.err.println("Interrupted Exception occured: " + e);
			System.out.println("Game Over!");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Class Not Found Exception occured: " + e);
		}
	}
}
