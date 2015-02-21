import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * The pathfinding class with the processing operations needed to get
 * from point A to point B.
 * 
 * @author gibsonjc
 *
 */
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

		//can't really find the shortest path with one point
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
		Path path = this.findShortestPath(waypoints.get(0),
				waypoints.get(1));

		for (int i = 1; i < waypoints.size() - 1; i++)
		{
			path.addToEndOfPath(this.findShortestPath(waypoints.get(i),
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
	 *            
	 * @return A path object containing the shortest path.
	 */
	public Path findShortestPath(String start, String end)
	{
		//getting the cities once so I don't have to repeat this method call
		City startCity = (City) this.graph.get(start);
		City endCity = (City) this.graph.get(end);
		
		//where the possible nodes are stored, representing paths
		PriorityQueue<AStarNode> open = new PriorityQueue<AStarNode>();
		
		//cities that have already been expanded
		ArrayList<City> closed = new ArrayList<City>();
		
		//the node representing the current city
		AStarNode current = new AStarNode(startCity, null, Heuristic.DISTANCE, null);
		AStarNode next;
		
		//priming the method
		open.add(current);
		closed.add(startCity);

		//the connections of a city, gotten from the graph.
		HashMap<City, Connection> connections;

		//if there are no available nodes left all have been visited and there
		//is no possible path between the given cities.
		while (!open.isEmpty())
		{
			current = open.remove();

			if (current.city.equals(endCity))
			{
				return current.reconstructPath();
			}
			
			//this is only added to the closed section AFTER it has been expanded
			closed.add(current.city);

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

	/**
	 * Uses the A* pathfinding algorithm to find the fastest 
	 * path between two points. Throws a RuntimeException if 
	 * no path is found. Uses the distance approximation with
	 * a maxiumum speed traveled to make sure the heuristic
	 * is at worst an underestimate. Works exactly the same
	 * as the findShortestPath method except for the heuristic.
	 * It probably would have probably been better to combine 
	 * them into a single method.
	 * 
	 * @param start
	 *            The name of the start point
	 * @param end
	 *            The name of the goal destination
	 *            
	 * @return A path object containing the fastest path.
	 */
	public Path findFastestPath(String start, String end)
	{
		// Identical to the findShortestPath method besides the 
		// heuristic. See that method for documentation. 
		
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

			connections = this.graph.getConnectedElements(current.city
					.getName());

			for (City city : connections.keySet())
			{
				if (closed.contains(city))
					continue;
				
				// Note time and not distance is given as the heuristic. 
				next = new AStarNode(city, current, Heuristic.TIME, connections.get(city));
				
				// Setting the time heuristic instead of the distance heuristic. 
				next.setApproximatedPathTime(endCity, this.maxSpeed);
				
				open.add(next);
			}
		}

		throw new RuntimeException("Connection not found");
	}

	/**
	 * 
	 * @param startCity the starting point.
	 * @param bottomLimit the bottom limit of path distance desired
	 * @param topLimit the top limit desired
	 * @param numberWanted how many options desired
	 * @return an array containing the path options sorted by interestingness.
	 */
	public Path[] findPathsWithTravelDistance(String startCity,
			double bottomLimit, double topLimit, int numberWanted)
	{
		Path[] returnPaths = new Path[numberWanted];
		City startingPoint = this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();
		Path path;

		for(int i = 0; i < REASONABLE_LIMIT; i++){
			path = getRandomPathDistance(startingPoint, bottomLimit);
			if (path != null) //returns null if it fails
				possiblePaths.add(path);
		}
		
		for (int i = 0; i < numberWanted; i++)
		{
			returnPaths[i] = possiblePaths.poll();
		}
		
		return returnPaths;
	}

	/**
	 * 
	 * @param startCity the starting point.
	 * @param bottomLimit the bottom limit of path time desired
	 * @param topLimit the top limit desired
	 * @param numberWanted how many options desired
	 * @return an array containing the path options sorted by interestingness
	 */
	public Path[] findPathsWithTravelTime(String startCity,
			double bottomLimit, double topLimit, int numberWanted)
	{
		Path path;
		Path[] returnPaths = new Path[numberWanted];
		City startingPoint = this.graph.get(startCity);
		PriorityQueue<Path> possiblePaths = new PriorityQueue<Path>();

		for(int i = 0; i < REASONABLE_LIMIT; i++){
			path = getRandomPathTime(startingPoint, bottomLimit);
			if (path != null) //returns null if it fails
				possiblePaths.add(path);
		}
		
		for (int i = 0; i < numberWanted; i++)
		{
			returnPaths[i] = possiblePaths.poll();
		}
		
		return returnPaths;
	}

	private Path getRandomPathDistance(City start, double distance){
		
		
		Path path = new Path(start, Heuristic.INTERESTINGNESS);
		Random r = new Random();
		HashMap<City, Connection> connectedCities;
		City currentCity;
		Object[] citiesArray;
		int next;
		ArrayList<City> closed = new ArrayList<City>();
		closed.add(start);
		ArrayList<City> open;
		
		while(path.getPathLength() < distance){	
			open = new ArrayList<City>();
			currentCity = path.getEndpoint();
			connectedCities = this.graph.getConnectedElements(currentCity.getName());
			citiesArray = connectedCities.keySet().toArray();
			
			for (int i = 0; i < citiesArray.length; i++)
			{
				if (!(closed.contains((City)citiesArray[i])))
				{
					open.add((City)citiesArray[i]);
				}
			}
			
			if (open.size() == 0)
				return null;
			
			next = r.nextInt(open.size());
			
			closed.add(open.get(next));
			
			path.addToPath(open.get(next), 
					connectedCities.get(this.graph.get(open.get(next).toString())));
		}
				
		return path;
		
	}
	
	private Path getRandomPathTime(City start, double time){
		Path path = new Path(start, Heuristic.INTERESTINGNESS);
		Random r = new Random();
		HashMap<City, Connection> connectedCities;
		City currentCity;
		Object[] citiesArray;
		int next;
		ArrayList<City> closed = new ArrayList<City>();
		closed.add(start);
		ArrayList<City> open;
		
		while(path.getPathTravelTime() < time){	
			open = new ArrayList<City>();
			currentCity = path.getEndpoint();
			connectedCities = this.graph.getConnectedElements(currentCity.getName());
			citiesArray = connectedCities.keySet().toArray();
			
			for (int i = 0; i < citiesArray.length; i++)
			{
				if (!(closed.contains((City)citiesArray[i])))
				{
					open.add((City)citiesArray[i]);
				}
			}
			
			if (open.size() == 0)
				return null;
			
			next = r.nextInt(open.size());
			
			closed.add(open.get(next));
			
			path.addToPath(open.get(next), 
					connectedCities.get(this.graph.get(open.get(next).toString())));
		}
				
		return path;
		
	}
	
	
	/**
	 * A class containing path information and the ability to reconstruct a path.
	 * This saves the computer the effort of copying Path objects which would be
	 * needed due to pointer issues. 
	 *
	 */
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
		
		/**
		 * Needed so that the nodes can be sorted in a priority queue. 
		 */
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
		
		/**
		 * This and setApproximatedPathTime are needed to set the heuristic for the algorithm.
		 */
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