import static org.junit.Assert.*;
import java.util.Random;
import java.util.ArrayList;

import org.junit.Test;

public class AStarTest {

	// // @Test
	// public void test()
	// {
	// Graph<City, Connection, String> graph = new Graph<City, Connection,
	// String>();
	// AStar pathfinder = new AStar(graph);
	// ArrayList<City> cities = new ArrayList<City>();
	// assertTrue(cities.add(new City("Detroit", 0, 0, 0, 0)));
	// assertTrue(cities.add(new City("Chicago", 0, 0, 1, 1)));
	// assertTrue(cities.add(new City("New York", 0, 0, 1, 2)));
	//
	// for (City city : cities)
	// {
	// assertTrue(graph.add(city, city.getName()));
	// }
	//
	// assertTrue(graph.addLink(cities.get(0).getName(),
	// cities.get(1).getName(),
	// new Connection(cities.get(0).distanceTo(cities.get(1)))));
	// assertTrue(graph.addLink(cities.get(0).getName(),
	// cities.get(2).getName(),
	// new Connection(cities.get(0).distanceTo(cities.get(2)))));
	// assertTrue(graph.addLink(cities.get(1).getName(),
	// cities.get(2).getName(),
	// new Connection(cities.get(1).distanceTo(cities.get(2)))));
	//
	// String path = pathfinder.findShortestPathBetween(
	// cities.get(0).getName(), cities.get(1).getName()).toString();
	//
	// System.out.println(path);
	// assertTrue(path.equals("[Detroit, Chicago]"));
	// }
	//
	// // @Test
	// public void legitTest()
	// {
	// Graph<City,Connection,String> map = new Graph<City,Connection,String>();
	// Setup setup = new Setup(map);
	// AStar pathfinder = new AStar(map);
	//
	// Path path = pathfinder.findShortestPathBetween("Arad", "Bucharest");
	// assertTrue(path.toString().equals("[Arad, Sibiu, Fagaras, Bucharest]"));
	// }
	//
	// // @Test
	// public void testTravelDistanceSearch()
	// {
	// Graph<City,Connection,String> map = new Graph<City,Connection,String>();
	// Setup setup = new Setup(map);
	// AStar pathfinder = new AStar(map);
	//
	// double len = pathfinder.findShortestPathBetween("Arad",
	// "Zerind").getPathLength();
	// // ArrayList<Path> paths = pathfinder.findPathsWithTravelDistance("Arad",
	// len-1, len+1);
	// // System.out.println(paths.size());
	// // for (Path path : paths)
	// // System.out.println(path);
	// // assertTrue(paths.size()==1);
	//
	// }
	//
	// // @Test
	// public void testTravelTimeSearch()
	// {
	// Graph<City,Connection,String> map = new Graph<City,Connection,String>();
	// Setup setup = new Setup(map);
	// AStar pathfinder = new AStar(map);
	//
	// double len = pathfinder.findShortestPathBetween("Arad",
	// "Zerind").getPathTravelTime();
	// // ArrayList<Path> paths = pathfinder.findPathsWithTravelTime("Arad",
	// len-10, len+10);
	//
	// // assertTrue(paths.size()==1);
	//
	// }
	//
	// @Test
	// public void testFindFastestRoute()
	// {
	// Graph<City,Connection,String> map = new Graph<City,Connection,String>();
	// Setup setup = new Setup(map);
	// AStar pathfinder = new AStar(map);
	//
	// Path path = pathfinder.findFastestPath("Craiova", "Pitesti");
	// System.out.println(path);
	// assertTrue(path.getCities().size() == 3);
	//
	// }
	//
	// @Test
	// public void testFindFastestRoute1()
	// {
	// Graph<City,Connection,String> map = new Graph<City,Connection,String>();
	// Setup setup = new Setup(map);
	// AStar pathfinder = new AStar(map);
	//
	// Path path1 = pathfinder.findFastestPath("Craiova", "Pitesti");
	// System.out.println(path1);
	// assertTrue(path1.getCities().size() == 3);
	//
	// }

	@Test
	public void stressTest() {
		Graph<City, Connection, String> map = new Graph<City, Connection, String>();
		Setup setup = new Setup(map, "ireland");
		AStar pathfinder = new AStar(map);
		City start;
		City end;
		Path distPath;
		Path timePath;
		int startIndex;
		int endIndex;
		Random rand = new Random();

		ArrayList<City> elements = map.getElements();

		for (int i = 0; i < 177; i++) 
		{
			for (int j = 0; j < 177; j++)
			{
				distPath = pathfinder.findShortestPathBetween(
						elements.get(i).getName(), elements.get(j).getName());
				timePath = pathfinder.findFastestPath(
						elements.get(i).getName(), elements.get(j).getName());
				assertTrue(distPath.getPathLength() <= timePath.getPathLength());
				assertTrue(distPath.getPathTravelTime() >= timePath
						.getPathTravelTime());
			}
		}
	}
	
//	@Test
	public void stressTestNodeMethod() {
		Graph<City, Connection, String> map = new Graph<City, Connection, String>();
		Setup setup = new Setup(map, "ireland");
		AStar pathfinder = new AStar(map);
		City start;
		City end;
		Path distPath;
		Path distPath2;
		int startIndex;
		int endIndex;
		Random rand = new Random();
		
		long v1Time = 0;
		long v2Time = 0;
		long temp;

		ArrayList<City> elements = map.getElements();

		for (int i = 0; i < 177; i++) 
		{
			for (int j = 0; j < 177; j++)
			{
//				temp = System.currentTimeMillis();
//				distPath = pathfinder.findShortestPathBetween(
//						elements.get(i).getName(), elements.get(j).getName());
//				v1Time += System.currentTimeMillis() - temp;
//				
//				temp = System.currentTimeMillis();
//				distPath2 = pathfinder.findShortestPathBetweenV2(
//						elements.get(i).getName(), elements.get(j).getName());
//				v2Time += System.currentTimeMillis() - temp;
//				
//				assertTrue(distPath.getPathLength() == distPath2.getPathLength());
			}
		}
		
		System.out.printf("Total Time: %d milliseconds%n", (v1Time + v2Time));
		System.out.printf("Time spent on V1: %d milliseconds%n", v1Time);
		System.out.printf("Time spent on V2: %d milliseconds%n", v2Time);
		
	}

}
