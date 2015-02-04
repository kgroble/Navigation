import java.util.ArrayList;


public class Path<T> implements Comparable<T>
{
	private ArrayList<City> path;
	private double pathLength;
	//for comparing to other paths, for use in the priority queue
	//base off distance, use another form of ranking to pick after 
	//  shortest routes are found
	
	//
	@Override
	public int compareTo(T o)
	{
		if (! (o instanceof Path))
			throw new RuntimeException("You should not be comparing these.");
		
		return (int)(this.pathLength - ((Path)(o)).getPathLength());
	}
	
	public double getPathLength()
	{
		return this.pathLength;
	}
	
	/**
	 * We will probably need to add some heuristic stuff here.
	 * @param link
	 * @param city
	 */
	public void addToPath(Connection link, City city)
	{
		this.path.add(city);
		this.pathLength += link.getConnectionDistance();
	}
}
