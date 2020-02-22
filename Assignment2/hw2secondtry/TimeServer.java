
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetAddress;

/**
 * A single-threaded time server that sends Date objects to clients. You may
 * need to open port 5005 in the firewall on the host machine.
 * 
 * @author amit
 */

public class TimeServer
{
	private int port = 5005;
	public ServerSocket server;
	public Socket sock;
	private Timer timer;
	private TimerTask timerTask;
	public static TimeServer timeServer;
	private OutputStream out;
	private boolean isFirstTime;
	public static boolean isTimerExpired;


	public static void main(String args[]) {
		timeServer = new TimeServer();
		timeServer.serviceClients();
	}


	public TimeServer()
	{
		try
		{
			server = new ServerSocket(port);
			System.out.println("TimeServer up and running on port " + port + " " + InetAddress.getLocalHost());
			
			//we need to handle the first time a bit differently
			isFirstTime = true;
			
			isTimerExpired = true;
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
	}


	/**
	 * The method that handles the clients, one at a time.
	 */
	public void serviceClients()
	{
		while(true)
		{
			try
			{
				//if this is not the first time, you first wait then accept connection
				if(isFirstTime == false)
				{
					synchronized(TimeServer.timeServer) 
		            { 
						try {
							isTimerExpired = true;
			            	TimeServer.timeServer.wait();
						}
						catch (InterruptedException e)
						{
							System.out.println("Interrupted Exception Occured" + e);
						} 
		            }
				}
				
				sock = server.accept();
				out = sock.getOutputStream();
				
				//if this the first time, then you first accept the connection, run the timer and then you wait
				if(isFirstTime == true)
				{
					isFirstTime = false;
					synchronized(TimeServer.timeServer) 
		            { 
						timer = new Timer();
						timerTask = new Execute();
						timer.scheduleAtFixedRate(timerTask, 0, 5000);
		            }
				}
				
				// Note that client gets a temporary/transient port on it's side
				// to talk to the server on its well known port
				System.out.println(
				        "Received connect from " + sock.getInetAddress().getHostAddress() + ": " + sock.getPort());

				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(new java.util.Date());
				oout.flush();

				/* Thread.sleep(4000); //4 secs */
				sock.close();
				
			}
			catch (IOException e)
			{
				System.err.println(e);
			}
		}
	}
}

class Execute extends TimerTask 
{ 
    public void run() 
    { 
    	synchronized(TimeServer.timeServer) 
        {
    		if(TimeServer.isTimerExpired)
    		{
    			System.out.println("Timer expired!");
    			TimeServer.isTimerExpired = false;
    		}
    		TimeServer.timeServer.notify();
        }
    } 
} 
