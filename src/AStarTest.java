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
	
	@Test
	public void legitTest()
	{
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar pathfinder = new AStar(map);
		
		Path path = pathfinder.findShortestPathBetween("Arad", "Bucharest");
		assertTrue(path.toString().equals("[Arad, Sibiu, Fagaras, Bucharest]"));
	}	
}
