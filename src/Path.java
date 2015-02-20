import java.util.ArrayList;
import java.util.Comparator;


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
	 * Creates the beginning of a path.
	 * 
	 * @param startPoint the starting point
	 */
	public Path(City startPoint, String heuristic)
	{
		this.path = new ArrayList<City>();
		this.path.add(startPoint);
		this.pathLength = 0;
		this.timeTaken = 0;
		this.interestingness = startPoint.getInterest();
		
		heuristic = heuristic.toLowerCase();
		
		if (heuristic.equals("time"))
			this.heuristic = Heuristic.TIME;
		else if (heuristic.equals("distance"))
			this.heuristic = Heuristic.DISTANCE;
		else if (heuristic.equals("interestingness"))
			this.heuristic = Heuristic.INTERESTINGNESS;
	}
	
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
	
	/**
	 * Creates an empty Path. Will really only be used if a copy
	 * is being made. See copy method.
	 */
	private Path(Heuristic heuristic)
	{
		this.path = new ArrayList<City>();
		this.pathLength = 0;
		this.timeTaken = 0;
		this.interestingness = 0;
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
	
	public void updateHeuristic(City endCity, double maxSpeed)
	{
		this.setApproximatedPathLength(endCity);
		this.setApproximatedPathTime(endCity, maxSpeed);
	}

	@Override
	public int compareTo(Path otherPath)
	{
		if (this.heuristic == Heuristic.TIME)
		{
			return (this.approximatedTimeTaken - otherPath.getApproximatedTimeTaken()) > 0 ? 1 : -1;
		}
		else if (this.heuristic == Heuristic.DISTANCE)
		{
			return (this.approximatedPathLength - otherPath.getApproximatedPathLength()) > 0 ? 1 : -1;
		}
		else if (this.heuristic == Heuristic.INTERESTINGNESS)
		{
			return (this.interestingness - otherPath.interestingness) > 0 ? 1 : -1;
		}
		
		throw new RuntimeException("Nonexistant heuristic in path");
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
	
	
	
	public Path copy()
	{
		Path copy = new Path(this.heuristic);
		
		for (City city : this.path)
		{
			copy.addToPathForCopyingPurposes(city);
		}
		
		copy.pathLength = this.pathLength;
		copy.timeTaken = this.timeTaken;
		return copy;
	}
	
	//TODO I guarantee there is a better way to do this nonsense.
		private void addToPathForCopyingPurposes(City nextCity)
		{
			this.path.add(nextCity);
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
