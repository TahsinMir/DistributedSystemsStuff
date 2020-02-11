
public class PingPong
{
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.err.println("Usage: java PingPong <client|server> <serverHost> <port#>");
			System.exit(1);
		}
		System.out.println("begining of pingpong");
		if(args[0] == "client" || args[0].equals("client"))
		{
			System.out.println("client execution requested");
			
			Client client = new Client(Integer.parseInt(args[2]), args[1]);
			client.ExecuteClient();
			
		}
		if(args[0] == "server" || args[0].equals("server"))
		{
			System.out.println("server execution requested");
			
			Server server = new Server(Integer.parseInt(args[2]));
			server.RunServer();
		}
	}
}
