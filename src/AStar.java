import java.util.ArrayList;
import java.util.HashMap;
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
	
	public Path findShortestPathBetween(String start, String end)
	{
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		Path current;
		Path newPath;
		
		//Not sure about this
		HashMap<String, City> connections;
		
		while (!open.isEmpty())
		{
			current = open.remove();
			if (current.getEndpoint().equals(end))
			{
				//path found. What to do exactly?
				return current;
			}
			
			//get the links of the city
			connections = this.graph.getConnectedElements(current.getEndpoint().getName());
			
//			for (Connection link : connections)
			for (City city : connections.values())
			{
				newPath = current;
//				newPath.addToPath(link);
				newPath.addToPath(city, 
						(Connection)this.graph.getConnectionBetween(
								newPath.getEndpoint().getName(), city));
				newPath.setApproximatedPathLength(endCity);
				open.add(newPath);
			}
			
		}
		 
		throw new RuntimeException("Connection not found");
		
	}
	
}
