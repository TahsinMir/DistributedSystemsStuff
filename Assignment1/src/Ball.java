import java.io.Serializable;

public class Ball extends Object implements Serializable
{
	String color;

	public Ball(String color)
	{
		this.color = color;
	}
	
	public String GetColor()
	{
		return color;
	}
}
