import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;


public class AStar
{
	private Graph<City, Connection, String> graph;
	private double maxSpeed = 60;
	private final int REASONABLE_LIMIT = 10000;

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
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		
		PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>();
		ArrayList<City> closed = new ArrayList<City>();
		AStarNode current = new AStarNode(startCity, null, Heuristic.DISTANCE, null);
		AStarNode next;
		
		open.add(current);
		closed.add(startCity);

		HashMap<City, Connection> connections;

		while (!open.isEmpty())
		{
			current = open.remove();

			if (current.city.equals(endCity))
			{
				return current.reconstructPath();
			}
			
			closed.add(current.city);

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.city
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				if (closed.contains(city))
					continue;
				
				
				next = new AStarNode(city, current, Heuristic.DISTANCE, connections.get(city));
				
				next.setApproximatedPathLength(endCity);
				
				open.add(next);
			}
		}

		throw new RuntimeException("Connection not found");
	}

	public Path findFastestPath(String start, String end)
	{
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		
		PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>();
		ArrayList<City> closed = new ArrayList<City>();
		AStarNode current = new AStarNode(startCity, null, Heuristic.DISTANCE, null);
		AStarNode next;
		
		open.add(current);
		closed.add(startCity);

		HashMap<City, Connection> connections;

		while (!open.isEmpty())
		{
			current = open.remove();

			if (current.city.equals(endCity))
			{
				return current.reconstructPath();
			}
			
			closed.add(current.city);

			// Gets a hashmap of the connected cities and the associated
			// Connection objects.
			connections = this.graph.getConnectedElements(current.city
					.getName());

			// Adding possible paths to the PriorityQueue
			for (City city : connections.keySet())
			{
				if (closed.contains(city))
					continue;
				
				
				next = new AStarNode(city, current, Heuristic.TIME, connections.get(city));
				
				next.setApproximatedPathTime(endCity, this.maxSpeed);
				
				open.add(next);
			}
		}

		throw new RuntimeException("Connection not found");
	}

	public Path[] findPathsWithTravelDistance(String startCity,
			double bottomLimit, double topLimit, int numberWanted)
	{
		Path[] returnPaths = new Path[numberWanted];
		City startingPoint = this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();

		for(int i = 0; i < REASONABLE_LIMIT; i++){
			possiblePaths.add(getRandomPathDistance(startingPoint, bottomLimit));
		}
		
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
		City startingPoint = this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();

		for(int i = 0; i < REASONABLE_LIMIT; i++){
			possiblePaths.add(getRandomPathTime(startingPoint, bottomLimit));
		}
		
		for (int i = 0; i < numberWanted; i++)
		{
			returnPaths[i] = possiblePaths.remove();
		}
		
		return returnPaths;
	}

	private Path getRandomPathDistance(City start, double distance){
		Path path = new Path(start, Heuristic.INTERESTINGNESS);
		Random r = new Random();
		while(path.getPathLength() < distance){
			City currentCity = path.getEndpoint();
			HashMap<City, Connection> connectedCities = this.graph.getConnectedElements(currentCity.getName());
			Object[] citiesArray = connectedCities.keySet().toArray();
			int next = r.nextInt(citiesArray.length);
			path.addToPath(this.graph.get(citiesArray[next].toString()), connectedCities.get(this.graph.get(citiesArray[next].toString())));
		}
		return path;
		
	}
	
	private Path getRandomPathTime(City start, double time){
		Path path = new Path(start, Heuristic.INTERESTINGNESS);
		Random r = new Random();
		while(path.getPathTravelTime() < time){
			City currentCity = path.getEndpoint();
			HashMap<City, Connection> connectedCities = this.graph.getConnectedElements(currentCity.getName());
			Object[] citiesArray = connectedCities.keySet().toArray();
			int next = r.nextInt(citiesArray.length);
			path.addToPath(this.graph.get(citiesArray[next].toString()), connectedCities.get(this.graph.get(citiesArray[next].toString())));
		}
		return path;
		
	}
	
	
	
	private class AStarNode implements Comparable<AStarNode>
	{		
		private City city;
		private AStarNode previousNode;
		private HashMap<City, Connection> connections;
		private double approximatedPathLength;
		private double approximatedTimeTaken;
		private double pathLength;
		private double interestingness;
		private double travelTime;
		private Heuristic heuristic;
		
		private AStarNode(City city, AStarNode previousNode, Heuristic heuristic, Connection connection)
		{
			this.previousNode = previousNode;
			this.heuristic = heuristic;
			this.city = city;
			
			if (previousNode != null)
			{
				this.pathLength = this.previousNode.pathLength + 
						connection.getConnectionDistance();
				this.travelTime = this.previousNode.travelTime + 
						connection.getConnectionTravelTime();
				this.interestingness = this.previousNode.interestingness + 
						this.city.getInterestingness();
				this.connections = AStar.this.graph.getConnectedElements(this.city.getName());
			}
			else
			{
				this.pathLength = 0;
				this.travelTime = 0;
				this.interestingness = this.city.getInterestingness();
			}
		}
		
		private Path reconstructPath()
		{
			if (this.previousNode == null)
			{
				return new Path(this.city, this.heuristic);
			}
			
			Path path = this.previousNode.reconstructPath();
			
			path.addToPath(this.city, this.connections.get(this.previousNode.city));
			
			
			return path;
		}
		
		@Override
		public int compareTo(AStarNode otherPath)
		{
			if (this.heuristic == Heuristic.TIME)
			{
				return (this.approximatedTimeTaken - otherPath.approximatedTimeTaken) > 0 ? 1 : -1;
			}
			else if (this.heuristic == Heuristic.DISTANCE)
			{
				return (this.approximatedPathLength - otherPath.approximatedPathLength) > 0 ? 1 : -1;
			}
			else if (this.heuristic == Heuristic.INTERESTINGNESS)
			{
				return (this.interestingness - otherPath.interestingness) > 0 ? 1 : -1;
			}
			
			throw new RuntimeException("Nonexistant heuristic in AStarNode");
		}
		
		
		private void setApproximatedPathLength(City endPoint)
		{
			this.approximatedPathLength = this.pathLength + 
					this.city.distanceTo(endPoint);
		}
		
		private void setApproximatedPathTime(City endCity, double maxSpeed)
		{
			double dist = this.city.distanceTo(endCity);
			this.approximatedTimeTaken = this.travelTime + dist / maxSpeed;
		}
	}

}