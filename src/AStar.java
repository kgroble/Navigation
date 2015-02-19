import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStar
{
	private Graph<City, Connection, String> graph;
	private double maxSpeed = 100;
	private final int REASONABLE_LIMIT = 500000;

	public AStar(Graph<City, Connection, String> graph)
	{
		this.graph = graph;
	}

	public Path findFastestPathWithWayPoints(ArrayList<String> waypoints)
	{

		if (waypoints.size() < 2)
			return null;

		// basically initializing the path
		Path path = this.findFastestPath(waypoints.get(0), waypoints.get(1));

		for (int i = 1; i < waypoints.size() - 1; i++)
		{
			path.addToEndOfPath(this.findFastestPath(waypoints.get(i),
					waypoints.get(i + 1)));
		}
		
		return path;
	}

	public Path findShortestPathWithWayPoints(ArrayList<String> waypoints)
	{
		if (waypoints.size() < 2)
			return null;

		// basically initializing the path
		Path path = this.findShortestPathBetween(waypoints.get(0),
				waypoints.get(1));

		for (int i = 1; i < waypoints.size() - 1; i++)
		{
			path.addToEndOfPath(this.findShortestPathBetween(waypoints.get(i),
					waypoints.get(i + 1)));
		}

		return path;
	}

	/**
	 * Uses the A* pathfinding algorithm to find the shortest path between two
	 * points. Throws a RuntimeException if no path is found.
	 * 
	 * @param start
	 *            The name of the start point
	 * @param end
	 *            The name of the goal destination
	 * @return A path object containing the shortest path.
	 */
	public Path findShortestPathBetween(String start, String end)
	{
		return findNShortestPaths(start, end, 1).get(0);
	}

	public ArrayList<Path> findNShortestPaths(String start, String end,
			int number)
	{
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<City> closed = new ArrayList<City>();
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path current = new Path(startCity, "Distance");
		Path newPath;
		
		open.add(current);
		closed.add(startCity);

		// Not sure about this
		HashMap<City, Connection> connections;

		while (!open.isEmpty() && possiblePaths.size() < number)
		{
			current = open.remove();

			if (current.getEndpoint().equals(endCity))
			{
				possiblePaths.add(current);
				continue;
			}
			
			closed.add(current.getEndpoint());

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.getEndpoint()
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				if (closed.contains(city))
					continue;
				newPath = current.copy();
				
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathLength(endCity);
				
				open.add(newPath);
			}
		}

		if (possiblePaths.size() == 0)
			throw new RuntimeException("Connection not found");
		
		return possiblePaths;

	}

	public Path findFastestPath(String start, String end)
	{
		return findNFastestPaths(start, end, 1).get(0);
	}

	public ArrayList<Path> findNFastestPaths(String start, String end,
			int number)
	{
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<City> closed = new ArrayList<City>();
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path current = new Path(startCity, "Time");
		Path newPath;
		open.add(current);

		// Not sure about this
		HashMap<City, Connection> connections;

		while (!open.isEmpty() && possiblePaths.size() < number)
		{

			current = open.remove();

			if (current.getEndpoint().equals(endCity))
			{
				possiblePaths.add(current);
				continue;
			}
			
			closed.add(current.getEndpoint());

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.getEndpoint()
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				if (closed.contains(city))
					continue;
				newPath = current.copy();
				
				
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathTime(endCity, this.maxSpeed);
				
				
				open.add(newPath);
			}
		}

		if (possiblePaths.size() == 0)
			throw new RuntimeException("Connection not found");

		return possiblePaths;
	}

	public Path[] findPathsWithTravelDistance(String startCity,
			double bottomLimit, double topLimit, int numberWanted)
	{
		Path[] returnPaths = new Path[numberWanted];
		City startingPoint = (City) this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();
		Path start = new Path(startingPoint, "Interestingness");

		breadthFirstSearchDistance(startCity, "DOES NOT EXIST", bottomLimit,
				topLimit, possiblePaths, start);

		
		for (int i = 0; i < numberWanted; i++)
		{
			returnPaths[i] = possiblePaths.remove();
		}
		
		return returnPaths;
	}

	public Path[] findPathsWithTravelTime(String startCity,
			double bottomLimit, double topLimit, int numberWanted)
	{
		Path[] returnPaths = new Path[numberWanted];
		City startingPoint = (City) this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();
		Path start = new Path(startingPoint, "Interestingness");
		breadthFirstSearchTime(startCity, "DOES NOT EXIST", bottomLimit,
				topLimit, possiblePaths, start);

		for (int i = 0; i < numberWanted; i++)
		{
			returnPaths[i] = possiblePaths.remove();
		}
		
		return returnPaths;
	}

	private void breadthFirstSearchDistance(String cityName,
			String previousCityName, double bottomLimit, double topLimit,
			PriorityQueue<Path> possiblePaths, Path path)
	{
		HashMap<City, Connection> connections = this.graph
				.getConnectedElements(cityName);
		Path newPath;
		
		if (path.getPathLength() > topLimit || possiblePaths.size() > REASONABLE_LIMIT)
			return;

		if (path.getPathLength() <= topLimit)
		{
			if (path.getPathLength() >= bottomLimit)
			{
				possiblePaths.add(path);
			}

			for (City nextCity : connections.keySet())
			{
				if (path.containsCity(nextCity.getName()))
					continue;

				newPath = path.copy();

				newPath.addToPath(nextCity, connections.get(nextCity));
				breadthFirstSearchDistance(nextCity.getName(), cityName,
						bottomLimit, topLimit, possiblePaths, newPath);
			}
		}
	}

	private void breadthFirstSearchTime(String cityName,
			String previousCityName, double bottomLimit, double topLimit,
			PriorityQueue<Path> possiblePaths, Path path)
	{
		HashMap<City, Connection> connections = this.graph
				.getConnectedElements(cityName);
		Path newPath;
		
		if (path.getPathTravelTime() > topLimit || possiblePaths.size() > REASONABLE_LIMIT)
			return;
		
		if (path.getPathTravelTime() <= topLimit)
		{
			if (path.getPathTravelTime() >= bottomLimit)
			{
				possiblePaths.add(path);
			}

			for (City nextCity : connections.keySet())
			{
				if (path.containsCity(nextCity.getName()))
					continue;

				newPath = path.copy();

				newPath.addToPath(nextCity, connections.get(nextCity));
				breadthFirstSearchTime(nextCity.getName(), cityName,
						bottomLimit, topLimit, possiblePaths, newPath);
			}
		}
	}
	
	private class AStarNode
	{
		private AStarNode previousNode;
	}

}

//TODO add node class to avoid copying the entire path
//Add a reconstruct path method.