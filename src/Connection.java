public class Connection
{
	private double length;
	private double averageSpeed;
	private double timeTaken;
	
	public Connection(double length)
	{
		this(length, 1.0);
	}
	
	public Connection(double length, double averageSpeed)
	{
		this.length = Math.abs(length);
		this.averageSpeed = averageSpeed;
		
		if (this.averageSpeed == 0)
			throw new RuntimeException("Speed is equal to zero.");
//		if (this.length == 0)
//			throw new RuntimeException("Length is either negative or zero.");
		
		this.timeTaken = this.length / this.averageSpeed;
	
	}
	
	public double getConnectionDistance()
	{
		return this.length;
	}
	
	public double getConnectionTravelTime()
	{
		return this.timeTaken;
	}
	
	public double getConnectionAverageSpeed()
	{
		return this.averageSpeed;
	}
}
