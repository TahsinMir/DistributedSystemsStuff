
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
	public static ServerSocket server;
	public static Socket sock;
	private Timer timer;
	private TimerTask timerTask;
	public static TimeServer timeServer;


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
			
			
			timer = new Timer();
			timerTask = new Execute();
			timer.scheduleAtFixedRate(timerTask, 1000, 5000);
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
			synchronized(timeServer) 
	        { 
	            try {
	            	timeServer.wait();
				} catch (InterruptedException e) {
					System.out.println("Interrupted Exception Occured" + e);
				} 
	        }
		} 
	}
}

class Execute extends TimerTask 
{ 
	private OutputStream out;
	
    public void run() 
    { 
    	try
		{
			TimeServer.sock = TimeServer.server.accept();
			out = TimeServer.sock.getOutputStream();
			
			// Note that client gets a temporary/transient port on it's side
			// to talk to the server on its well known port
			System.out.println(
			        "Received connect from " + TimeServer.sock.getInetAddress().getHostAddress() + ": " + TimeServer.sock.getPort());

			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(new java.util.Date());
			oout.flush();

			/* Thread.sleep(4000); //4 secs */
			TimeServer.sock.close();
			
			synchronized(TimeServer.timeServer) 
            { 
				TimeServer.timeServer.notify(); 
            } 
			/* } catch (InterruptedException e) { */
			/* System.err.println(e); */
		}
		catch (IOException e) {
			System.err.println(e);
		} 
    } 
      
} 
