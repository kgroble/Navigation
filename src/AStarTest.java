import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AStarTest
{
	
//	@Test
	public void test()
	{
		Graph<City, Connection, String> graph = new Graph<City, Connection, String>();
		AStar pathfinder = new AStar(graph);
		ArrayList<City> cities = new ArrayList<City>();
		assertTrue(cities.add(new City("Detroit", 0, 0, 0, 0)));
		assertTrue(cities.add(new City("Chicago", 0, 0, 1, 1)));
		assertTrue(cities.add(new City("New York", 0, 0, 1, 2)));
		
		for (City city : cities)
		{
			assertTrue(graph.add(city, city.getName()));
		}
		
		assertTrue(graph.addLink(cities.get(0).getName(), cities.get(1).getName(), 
				new Connection(cities.get(0).distanceTo(cities.get(1)))));
		assertTrue(graph.addLink(cities.get(0).getName(), cities.get(2).getName(), 
				new Connection(cities.get(0).distanceTo(cities.get(2)))));
		assertTrue(graph.addLink(cities.get(1).getName(), cities.get(2).getName(), 
				new Connection(cities.get(1).distanceTo(cities.get(2)))));
		
		String path = pathfinder.findShortestPathBetween(
				cities.get(0).getName(), cities.get(1).getName()).toString();
		
		System.out.println(path);
		assertTrue(path.equals("[Detroit, Chicago]"));
	}
	
//	@Test
	public void legitTest()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		Path path = pathfinder.findShortestPathBetween("Arad", "Bucharest");
		assertTrue(path.toString().equals("[Arad, Sibiu, Fagaras, Bucharest]"));
	}	
	
//	@Test
	public void testTravelDistanceSearch()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		double len = pathfinder.findShortestPathBetween("Arad", "Zerind").getPathLength();
//		ArrayList<Path> paths = pathfinder.findPathsWithTravelDistance("Arad", len-1, len+1);
//		System.out.println(paths.size());
//		for (Path path : paths)
//			System.out.println(path);
//		assertTrue(paths.size()==1);
		
	}
	
//	@Test
	public void testTravelTimeSearch()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		double len = pathfinder.findShortestPathBetween("Arad", "Zerind").getPathTravelTime();
//		ArrayList<Path> paths = pathfinder.findPathsWithTravelTime("Arad", len-10, len+10);
		
//		assertTrue(paths.size()==1);
		
	}
	
	@Test
	public void testFindFastestRoute()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		Path path = pathfinder.findFastestPath("Craiova", "Pitesti");
		System.out.println(path);
		assertTrue(path.getCities().size() == 3);
		
	}
	
	@Test
	public void testFindFastestRoute1()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		Path path1 = pathfinder.findFastestPath("Craiova", "Pitesti");
		System.out.println(path1);
		assertTrue(path1.getCities().size() == 3);
		
	}
}
