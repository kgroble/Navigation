import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar
{
	//This will be in the graph class I believe
	
	//probably return an arraylist of paths.
	//  where the algorithm will actually be located
	public Path findShortestPathBetween(City start, City end)
	{
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		Path current;
		Path newPath;
		ArrayList<Connection> connections;
		
		while (!open.isEmpty())
		{
			current = open.remove();
			if (current.getEndpoint().equals(end))
			{
				//path found. What to do exactly?
				return current;
			}
			
			//get the links of the city
			connections = current.getEndpoint().getConnections();
			
			for (Connection link : connections)
			{
				newPath = current;
				newPath.addToPath(link);
				newPath.setApproximatedPathLength(end);
				open.add(newPath);
			}
			
		}
		 
		throw new RuntimeException("Connection not found");
		
	}
	
	//I think it would make sense to have the distanceBetweenCities method in 
	//  the city class. So I am not currently going to add it here.
}
