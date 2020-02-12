
public class PingPong
{
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.err.println("Usage: java PingPong <client|server> <serverHost> <port#>");
			System.exit(1);
		}
		
		else if(args[0] == "client" || args[0].equals("client"))
		{	
			Client client = new Client(Integer.parseInt(args[2]), args[1]);
			client.start();	
		}
		else if(args[0] == "server" || args[0].equals("server"))
		{
			Server server = new Server(Integer.parseInt(args[2]));
			server.RunServer();
		}
		else
		{
			System.err.println("Invalid arguments");
			System.err.println("Usage: java PingPong <client|server> <serverHost> <port#>");
			System.exit(1);
		}
	}
}
