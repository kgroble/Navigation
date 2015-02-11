public class Connection
{
	private double length;
	private double averageSpeed;
	
	public Connection(double length)
	{
		this(length, 1.0);
	}
	
	public Connection(double length, double averageSpeed)
	{
		this.length = length;
		this.averageSpeed = averageSpeed;
	}
	
	public double getConnectionDistance()
	{
		return this.length;
	}
	
	public double getConnectionTravelTime()
	{
		return this.length / this.averageSpeed;
	}
	
	public double getConnectionAverageSpeed()
	{
		return this.averageSpeed;
	}
}
