
public class Connection
{
	private double connectionLength;
	private City endPoint;
	
	public Connection(City endPoint, double length)
	{
		this.endPoint = endPoint;
		this.connectionLength = length;
	}
	
	public double getConnectionDistance()
	{
		return this.connectionLength;
	}
	
	public City getEndPoint()
	{
		return this.endPoint;
	}
}
