import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Sets up the underlying graph from information
 * read from file.
 *
 */
public class Setup {
	private Graph<City, Connection, String> g;
	
	public Setup(Graph g){
		this.g = g;
		addCities();
		addLinks();
	}
	
	/**
	 * Loads cities from file.
	 */
	public void addCities(){
		Scanner sc;
		try {
			sc = new Scanner(new File("cities.txt"));
			while(sc.hasNextLine()){
				String city = sc.nextLine();
				String[] cityInfo = city.split("--");
				if(cityInfo.length != 4){
					System.out.println("The line \"" + city + "\" is of invalid format.");
					System.out.println("Please use format: name--population--xCoordinate--yCoordinate");
				} else {
					String name = cityInfo[0];
					int population = Integer.parseInt(cityInfo[1]);
					double xCoord = Double.parseDouble(cityInfo[2]);
					double yCoord = Double.parseDouble(cityInfo[3]);
					addCity(name, population, xCoord, yCoord);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("cities.txt does not exist.");
		}
	}
	
	/**
	 * Loads links from file.
	 */
	public void addLinks(){
		Scanner sc;
		try{
			sc = new Scanner(new File("links.txt"));
			while(sc.hasNextLine()){
				String cityLink = sc.nextLine();
				String[] linkInfo = cityLink.split("--");
				if(linkInfo.length != 3){
					System.out.println("The line \"" + cityLink + "\" is of invalid format.");
					System.out.println("Please use format: startCity--stopCity--distance");
				} else {
					String startCity = linkInfo[0];
					String endCity = linkInfo[1];
					double distance = Double.parseDouble(linkInfo[3]);
					addLink(startCity, endCity, distance);
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("links.txt does not exist.");
		}
	}
	
	/**
	 * Loads an individual city (may be unnecessary here, instead in Graph class)
	 * 
	 * @param name
	 * @param pop
	 * @param xCoord
	 * @param yCoord
	 */
	public void addCity(String name, int pop, double xCoord, double yCoord){
		//System.out.println("Adding city: " + name + ", population " + pop + " at (" + xCoord + ", " + yCoord + ").");
		g.add(new City(name, pop, xCoord, yCoord, new ArrayList<Connection>()), name);
	}
	
	/**
	 * Loads an individual link (may be unnecessary here, instead in Graph class)
	 * 
	 * @param c1
	 * @param c2
	 */
	public void addLink(String start, String end, double distance){
		Connection c = new Connection(distance);
		g.addLink(start, end, c);
	}
}
