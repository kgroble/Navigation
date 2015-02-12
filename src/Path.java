import java.util.ArrayList;


public class Path implements Comparable<Path>
{
	private enum Heuristic {TIME, DISTANCE}
	
	private ArrayList<City> path;
	private double pathLength;
	private double approximatedPathLength;
	private double timeTaken;
	
	/**
	 * Creates the beginning of a path.
	 * 
	 * @param startPoint the starting point
	 */
	public Path(City startPoint)
	{
		this.path = new ArrayList<City>();
		this.path.add(startPoint);
		this.pathLength = 0;
		this.timeTaken = 0;
	}
	
	/**
	 * Creates an empty Path. Will really only be used if a copy
	 * is being made. See copy method.
	 */
	private Path()
	{
		this.path = new ArrayList<City>();
		this.pathLength = 0;
		this.timeTaken = 0;
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

	/**
	 * TODO does it make sense to have the approximation here?
	 */
	@Override
	public int compareTo(Path otherPath)
	{
		return (int)(this.approximatedPathLength - otherPath.getApproximatedPathLength());
	}
	
	@Override
	public String toString()
	{
		return this.path.toString();
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
	
	//TODO I guaruntee there is a better way to do this nonsense.
	private void addToPathForCopyingPurposes(City nextCity)
	{
		this.path.add(nextCity);
	}
	
	public Path copy()
	{
		Path copy = new Path();
		for (City city : this.path)
			copy.addToPathForCopyingPurposes(city);
		copy.pathLength = this.pathLength;
		copy.timeTaken = this.timeTaken;
		return copy;
	}
}
