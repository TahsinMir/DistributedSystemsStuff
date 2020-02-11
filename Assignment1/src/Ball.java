import java.io.Serializable;

public class Ball extends Object implements Serializable
{
	String color;
	String message;

	public Ball(String color)
	{
		this.color = color;
	}
	
	public String GetColor()
	{
		return color;
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
