import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStar
{
	private Graph<City, Connection, String> graph;
	private double maxSpeed = 100;

	public AStar(Graph<City, Connection, String> graph)
	{
		this.graph = graph;
	}

	public Path findFastestPathWithWayPoints(ArrayList<String> waypoints)
	{
		if (waypoints.size() < 2)
			return null;

		// basically initializing the path
		Path path = this.findFastestPath(waypoints.get(0),
				waypoints.get(1));

		for (int i = 1; i < waypoints.size()-1; i++)
		{
			path.addToEndOfPath(this.findFastestPath
					(waypoints.get(i), waypoints.get(i + 1)));
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

		for (int i = 1; i < waypoints.size()-1; i++)
		{
			path.addToEndOfPath(this.findShortestPathBetween
					(waypoints.get(i), waypoints.get(i + 1)));
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
		System.out
				.printf("Searching for path between %s and %s.%n", start, end);
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path current = new Path(startCity, false); // false means it isnt using
													// a time heuristic
		Path newPath;
		open.add(current);

		// Not sure about this
		HashMap<City, Connection> connections;

		while (!open.isEmpty() && possiblePaths.size() < number)
		{
			current = open.remove();
			closed.add(current); // So the path is not checked twice. But when
									// would that happen?

			if (current.getEndpoint().equals(endCity))
			{
				System.out.println("Path found.");
				// System.out.printf("%d iterations.%n", iterations);
				System.out.printf("Path is %.1f units long%n",
						current.getPathLength());
				System.out.printf("Path will take %.1f units of time.%n",
						current.getPathTravelTime());
				possiblePaths.add(current);
				continue;
			}

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.getEndpoint()
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				newPath = current.copy(); // TODO fix this. No good. Bad.
											// Baaaaad.
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathLength(endCity); // for the heuristic

				if (!(closed.contains(newPath)))
					open.add(newPath); // TODO Is this actually necessary?
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
		System.out
				.printf("Searching for path between %s and %s.%n", start, end);
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		PriorityQueue<Path> open = new PriorityQueue<Path>();
		ArrayList<Path> closed = new ArrayList<Path>();
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path current = new Path(startCity, true);
		Path newPath;
		open.add(current);

		// Not sure about this
		HashMap<City, Connection> connections;

		while (!open.isEmpty() && possiblePaths.size() < number)
		{
			System.out.println("Iteration");
			
			current = open.remove();
			closed.add(current); // So the path is not checked twice. But when
									// would that happen?
			
			System.out.println(current);

			if (current.getEndpoint().equals(endCity))
			{
				System.out.println("Path found.");
				System.out.printf("Path is %.1f units long%n",
						current.getPathLength());
				System.out.printf("Path will take %.1f units of time.%n",
						current.getPathTravelTime());
				possiblePaths.add(current);
				continue;
			}

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.getEndpoint()
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				if (current.containsCity(city.getName()))
					continue;
				newPath = current.copy();
				newPath.addToPath(city, connections.get(city));
				newPath.setApproximatedPathTime(endCity, this.maxSpeed); // for
																			// the
																			// heuristic

				if (!(closed.contains(newPath)))
					open.add(newPath); // TODO Is this actually necessary?
			}
		}

		if (possiblePaths.size() == 0)
			throw new RuntimeException("Connection not found");

		return possiblePaths;
	}

	public ArrayList<Path> findPathsWithTravelDistance(String startCity,
			double bottomLimit, double topLimit)
	{
		City startingPoint = (City) this.graph.get(startCity);
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path start = new Path(startingPoint, false);

		breadthFirstSearchDistance(startCity, "DOES NOT EXIST", bottomLimit,
				topLimit, possiblePaths, start);

		return possiblePaths;
	}

	public ArrayList<Path> findPathsWithTravelTime(String startCity,
			double bottomLimit, double topLimit)
	{
		City startingPoint = (City) this.graph.get(startCity);
		ArrayList<Path> possiblePaths = new ArrayList<Path>();
		Path start = new Path(startingPoint, true);
		breadthFirstSearchTime(startCity, "DOES NOT EXIST", bottomLimit,
				topLimit, possiblePaths, start);

		return possiblePaths;
	}

	private void breadthFirstSearchDistance(String cityName,
			String previousCityName, double bottomLimit, double topLimit,
			ArrayList<Path> possiblePaths, Path path)
	{
		HashMap<City, Connection> connections = this.graph
				.getConnectedElements(cityName);
		Path newPath;

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
			ArrayList<Path> possiblePaths, Path path)
	{
		HashMap<City, Connection> connections = this.graph
				.getConnectedElements(cityName);
		Path newPath;

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
	
	
}
