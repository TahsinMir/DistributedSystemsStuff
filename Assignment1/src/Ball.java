import java.io.Serializable;

public class Ball extends Object implements Serializable
{
	String message;

	public Ball()
	{
		
	}
	public Ball(String message)
	{
		this.message = message;
	}
	public void SetMessage(String message)
	{
		this.message = message;
	}
	public String GetMessage()
	{
		return message;
	}
}
