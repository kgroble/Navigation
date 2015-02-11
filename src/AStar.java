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
	
	/**
	 * Uses the A* pathfinding algorithm to find the shortest path between
	 * 	two points. Throws a RuntimeException if no path is found. 
	 * 
	 * @param start The name of the start point
	 * @param end The name of the goal destination
	 * @return A path object containing the shortest path.
	 */
	public Path findShortestPathBetween(String start, String end)
	{
		return findNShortestPaths(start, end, 1).get(0);
		
	}
	
	public ArrayList<Path> findNShortestPaths(String start, String end, int number)
	{
		System.out.printf("Searching for path between %s and %s.%n", start, end);
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path current = new Path(startCity);
		Path newPath;
		int iterations = 0;
		open.add(current);
		
		//Not sure about this
		HashMap<City, Connection> connections;
		
		while (!open.isEmpty() && possiblePaths.size() < number)
		{		
			iterations ++;
			current = open.remove();
			closed.add(current); //So the path is not checked twice. But when would that happen?
			
			if (current.getEndpoint().equals(endCity))
			{
				System.out.println("Path found.");
//				System.out.printf("%d iterations.%n", iterations);
				System.out.printf("Path is %.1f units long%n", current.getPathLength());
				System.out.printf("Path will take %.1f units of time.%n", current.getPathTravelTime());
				possiblePaths.add(current);
				continue;
			}
			
			//Gets a hashmap of the connected cities and the associated Connection objects.
			connections = this.graph.getConnectedElements(current.getEndpoint().getName());
			
			//Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				newPath = current.copy(); // TODO fix this. No good. Bad. Baaaaad.
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathLength(endCity); //for the heuristic
				
				if (!(closed.contains(newPath)))
					open.add(newPath); //TODO Is this actually necessary?
			}
			
		}
		 
		if (possiblePaths.size() == 0)
			throw new RuntimeException("Connection not found");
		
		return possiblePaths;
	}
	
	public ArrayList<Path> findPathsWithTravelDistance(String startCity, double bottomLimit, double topLimit)
	{
		City startingPoint = (City) this.graph.get(startCity);
		return null;
	}
	
	public ArrayList<Path> findPathsWithTravelTime(String startCity, double bottomLimit, double topLimit)
	{
		return null;
	}
}
