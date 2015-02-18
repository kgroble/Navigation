import java.util.ArrayList;


public class Path implements Comparable<Path>
{
	private ArrayList<City> path;
	private double pathLength;
	private double approximatedPathLength;
	private double timeTaken;
	private double approximatedTimeTaken;
	private boolean usingTimeHeuristic;
	
	/**
	 * Creates the beginning of a path.
	 * 
	 * @param startPoint the starting point
	 */
	public Path(City startPoint, boolean timeHeuristic)
	{
		this.path = new ArrayList<City>();
		this.path.add(startPoint);
		this.pathLength = 0;
		this.timeTaken = 0;
		this.usingTimeHeuristic = timeHeuristic;
	}
	
	/**
	 * Creates an empty Path. Will really only be used if a copy
	 * is being made. See copy method.
	 */
	private Path(boolean timeHeuristic)
	{
		this.path = new ArrayList<City>();
		this.pathLength = 0;
		this.timeTaken = 0;
		this.usingTimeHeuristic = timeHeuristic;
	}
	
	public ArrayList<City> getCities()
	{
		return path;
	}
	
	public double getPathLength()
	{
		return this.pathLength;
	}
	
	public double getPathTravelTime()
	{
		return this.timeTaken;
	}
	
	
	public double getApproximatedPathLength()
	{
		return this.approximatedPathLength;
	}
	
	public void setApproximatedPathLength(City endPoint)
	{
		this.approximatedPathLength = this.pathLength + 
				this.getEndpoint().distanceTo(endPoint);
	}
	
	public void setApproximatedPathTime(City endCity, double maxSpeed)
	{
		double dist = this.getEndpoint().distanceTo(endCity);
		this.approximatedTimeTaken = this.timeTaken + dist / maxSpeed;
	}
	
	/**
	 * We will probably need to add some heuristic stuff here.
	 * @param link
	 * @param city
	 */
	public void addToPath(City nextCity, Connection link)
	{
		this.path.add(nextCity);
		this.pathLength += link.getConnectionDistance();
		this.timeTaken += link.getConnectionTravelTime();
	}
	
	public void updateHeuristic(City endCity, double maxSpeed)
	{
		this.setApproximatedPathLength(endCity);
		this.setApproximatedPathTime(endCity, maxSpeed);
	}

	/**
	 * TODO does it make sense to have the approximation here?
	 */
	@Override
	public int compareTo(Path otherPath)
	{
		if (this.usingTimeHeuristic)
		{
			return (this.approximatedTimeTaken - otherPath.getApproximatedTimeTaken()) > 0 ? 1 : -1;
		}
		return (this.approximatedPathLength - otherPath.getApproximatedPathLength()) > 0 ? 1 : -1;
	}
	
	private double getApproximatedTimeTaken()
	{
		return this.approximatedTimeTaken;
	}
	
	public City getEndpoint()
	{
		return this.path.get(this.path.size()-1);
	}
	
	
	public boolean containsCity(String cityName)
	{
		for (City city : this.path)
		{
			if (city.getName().equals(cityName))
				return true;
		}
		return false;
	}
	
	//TODO I guarantee there is a better way to do this nonsense.
	private void addToPathForCopyingPurposes(City nextCity)
	{
		this.path.add(nextCity);
	}
	
	private boolean isUsingTimeHeuristic()
	{
		return this.usingTimeHeuristic;
	}
	
	public Path copy()
	{
		Path copy = new Path(this.usingTimeHeuristic);
//		for (City city : this.path)
		for (int i = 0; i < this.path.size(); i++)
		{
			copy.addToPathForCopyingPurposes(this.path.get(i));
		}
		copy.pathLength = this.pathLength;
		copy.timeTaken = this.timeTaken;
		return copy;
	}

	
	
	public void addToEndOfPath(Path otherPath)
	{
		ArrayList<City> cities = otherPath.getCities();
		
		for (int i = 1; i < cities.size(); i++)
		{
			this.path.add(cities.get(i));
		}
		
		this.pathLength += otherPath.getPathLength();
		this.timeTaken += otherPath.getPathTravelTime();
	}
	
	public String toString()
	{
		return String.format("Path start: %s%nPath Endpoint: %s%n"
				+ "Path: %s%nPath Length: %.3f%nPath time: %.3f%n"
				+ "Approximate Distance: %.3f%nApproximate Time: %.3f%n"
				+ "Using time heuristic: %b%n", 
				this.path.get(0).toString(), this.getEndpoint(), 
				this.path.toString(), this.pathLength, 
				this.timeTaken, this.approximatedPathLength,
				this.approximatedTimeTaken, this.usingTimeHeuristic);
	}
}
