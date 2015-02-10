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
	private Graph<City, Connection, String> graph;
	
	public AStar(Graph<City, Connection, String> graph)
	{
		this.graph = graph;
	}
	
	public Path findShortestPathBetween(String start, String end)
	{
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		Path current = new Path(startCity);
		Path newPath;
		
		open.add(current);
		
		//Not sure about this
		HashMap<String, City> connections;
		
		while (!open.isEmpty())
		{
			System.out.println("Iteration");
			
			current = open.remove();
			if (current.getEndpoint().equals(end))
			{
				//path found. What to do exactly?
				return current;
			}
			
			//get the links of the city
			connections = this.graph.getConnectedElements(current.getEndpoint().getName());
			System.out.println(connections);
			
//			for (Connection link : connections)
			for (City city : connections.values())
			{
				System.out.println(city);
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
