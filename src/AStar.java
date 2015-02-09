import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar
{
	//This will be in the graph class I believe
	
	//probably return an arraylist of paths.
	//  where the algorithm will actually be located
	
	/*
	 * To get paths:
	 * 	graph.getConnectedElements(String cityName)
	 * 	gives back array of connected cities
	 * 	graph.getConnectionBetween(String city1, String city2)
	 * 	gives connection object
	 *
	 */
	private Graph graph;
	
	public AStar(Graph graph)
	{
		this.graph = graph;
	}
	
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
	
}
