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
		System.out.printf("Searching for path between %s and %s.%n", start, end);
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		Path current = new Path(startCity);
		Path newPath;
		
		open.add(current);
		
		//Not sure about this
		HashMap<City, Connection> connections;
		
		while (!open.isEmpty())
		{
			System.out.println("Iteration");
			
			current = open.remove();
			System.out.println(current);
//			System.out.println(current.getEndpoint());
//			System.out.println(endCity);
			if (current.getEndpoint().equals(endCity))
			{
				//path found. What to do exactly?
				System.out.println("Path found.");
				return current;
			}
			
			//get the links of the city
			connections = this.graph.getConnectedElements(current.getEndpoint().getName());
//			System.out.println(connections.keySet());
			
//			for (Connection link : connections)
			for (City city : connections.keySet())
			{
//				System.out.println(city);
				newPath = current;
//				newPath.addToPath(link);
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathLength(endCity);
				open.add(newPath);
			}
			
		}
		 
		throw new RuntimeException("Connection not found");
	}
	
}
