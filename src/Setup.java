import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Sets up the underlying graph from information
 * read from file.
 *
 */
public class Setup {
	private Graph<City, Connection, String> g;
	private String txt;
	
	public Setup(Graph<City, Connection, String> g, String txt){
		this.g = g;
		this.txt=txt;
		addCities();
		try {
			addLinks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads cities from file.
	 */
	public void addCities(){
		Scanner sc = null;
		try {
			sc = new Scanner(new File(txt+"cities.txt"));
			while(sc.hasNextLine()){
				String city = sc.nextLine();
				String[] cityInfo = city.split("\\|\\|");
				if(cityInfo.length != 5){
					System.out.println("The line \"" + city + "\" is of invalid format.");
					System.out.println("Please use format: name||population||interest||xCoordinate||yCoordinate");
				} else {
					String name = cityInfo[0];
					int population = Integer.parseInt(cityInfo[1]);
					double interest = Double.parseDouble(cityInfo[2]);
					double xCoord = Double.parseDouble(cityInfo[3]);
					double yCoord = Double.parseDouble(cityInfo[4]);
					addCity(name, population, interest, xCoord, yCoord);
				}
			}
		} catch (FileNotFoundException e) { 
			System.out.println(txt+"cities.txt does not exist.");
		}
		if (sc != null)
			sc.close();
	}
	
	/**
	 * Loads links from file.
	 * @throws IOException 
	 */
	public void addLinks() throws IOException{
		BufferedReader sc = null;
		
		try{
			sc = new BufferedReader(new FileReader(txt+"links.txt"));
			String cityLink = sc.readLine();
			while(cityLink != null){
//				System.out.println(cityLink);
				String[] linkInfo = cityLink.split("--");
				if(linkInfo.length != 4){
					System.out.println("The line \"" + cityLink + "\" is of invalid format.");
					System.out.println("Please use format: startCity--stopCity--distance--speed");
				} else {
					String startCity = linkInfo[0];
					String endCity = linkInfo[1];
					double distance = Double.parseDouble(linkInfo[2]);
					double speed = Double.parseDouble(linkInfo[3]);
					addLink(startCity, endCity, distance, speed);
				}
				cityLink = sc.readLine();
			}
			if (sc != null)
				sc.close();

		} catch (FileNotFoundException e) {
			System.out.println(txt+"links.txt does not exist.");
		}
		
		
		if (sc != null)
			sc.close();
	}
	
	/**
	 * Loads an individual city (may be unnecessary here, instead in Graph class)
	 * 
	 * @param name
	 * @param pop
	 * @param xCoord
	 * @param yCoord
	 */
	public void addCity(String name, int pop, double interest, double xCoord, double yCoord){
		//System.out.println("Adding city: " + name + ", population " + pop + " at (" + xCoord + ", " + yCoord + ").");
		g.add(new City(name, pop, interest, xCoord, yCoord), name);
	}
	
	/**
	 * Loads an individual link (may be unnecessary here, instead in Graph class)
	 * 
	 * @param c1
	 * @param c2
	 */
	public void addLink(String start, String end, double distance, double speed){
		Connection c = new Connection(distance, speed);
		g.addLink(start, end, c);
	}
}
