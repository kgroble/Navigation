import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStar
{
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
			current = open.remove();
			if (current.getEndpoint().equals(endCity))
			{
				System.out.println("Path found.");
				return current;
			}
			
			//get the links of the city
			connections = this.graph.getConnectedElements(current.getEndpoint().getName());
			
			for (City city : connections.keySet())
			{
				newPath = current.copy(); // TODO fix this
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathLength(endCity);
				open.add(newPath);
			}
			
		}
		 
		throw new RuntimeException("Connection not found");
	}
	
}
