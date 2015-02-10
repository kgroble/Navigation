import java.util.ArrayList;


public class Path implements Comparable<Path>
{
	private ArrayList<City> path;
	private double pathLength;
	private double approximatedPathLength;
	//for comparing to other paths, for use in the priority queue
	//base off distance, use another form of ranking to pick after 
	//  shortest routes are found
	
	public Path(City startPoint)
	{
		this.path = new ArrayList<City>();
		this.path.add(startPoint);
		this.pathLength = 0;
	}
	
	public double getPathLength()
	{
		return this.pathLength;
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
}
