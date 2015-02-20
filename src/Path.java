import java.util.ArrayList;


public class Path implements Comparable<Path>
{
//	private enum Heuristic {DISTANCE, TIME, INTERESTINGNESS}
	
	private ArrayList<City> path;
	private double pathLength;
	private double approximatedPathLength;
	private double timeTaken;
	private double approximatedTimeTaken;
	private Heuristic heuristic;
	private double interestingness;
		
	/**
	 * TODO see if the one above this is even needed, I don't think
	 * paths are really made outside of AStar which I just gave heuristic.
	 * 
	 * 
	 * @param startPoint
	 * @param heuristic
	 */
	public Path(City startPoint, Heuristic heuristic)
	{
		this.path = new ArrayList<City>();
		this.path.add(startPoint);
		this.pathLength = 0;
		this.timeTaken = 0;
		this.interestingness = startPoint.getInterest();
		this.heuristic = heuristic;
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
	
	/**
	 * 
	 * @param link
	 * @param city
	 */
	public void addToPath(City nextCity, Connection link)
	{
		this.path.add(nextCity);
		this.pathLength += link.getConnectionDistance();
		this.timeTaken += link.getConnectionTravelTime();
	}

	@Override
	public int compareTo(Path otherPath)
	{
		if (this.heuristic == Heuristic.TIME)
		{
			return (this.timeTaken - otherPath.timeTaken) > 0 ? 1 : -1;
		}
		else if (this.heuristic == Heuristic.DISTANCE)
		{
			return (this.pathLength - otherPath.pathLength) > 0 ? 1 : -1;
		}
		else if (this.heuristic == Heuristic.INTERESTINGNESS)
		{
			return (this.interestingness - otherPath.interestingness) > 0 ? 1 : -1;
		}
		
		throw new RuntimeException("Nonexistant heuristic in path");
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
	
	public void addToEndOfPath(Path otherPath)
	{
		ArrayList<City> cities = otherPath.getCities();
		
		for (int i = 1; i < cities.size(); i++)
		{
			this.path.add(cities.get(i));
		}
		
		this.pathLength += otherPath.pathLength;
		this.timeTaken += otherPath.timeTaken;
		this.interestingness += otherPath.interestingness;
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
				this.approximatedTimeTaken, this.heuristic);
	}
}
