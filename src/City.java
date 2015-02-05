import java.util.ArrayList;

/**
 * A simplistic representation of a city on a 2-d coordinate system.
 */
public class City{
	private String name;
	private int population;
	private double xCoord;
	private double yCoord;
	private ArrayList<Connection> connections;
	
	/**
	 * Constructs a new city from the given parameters.
	 * 
	 * @param name
	 * @param population
	 * @param xCoord
	 * @param yCoord
	 */
	public City(String name, int population, double xCoord, double yCoord, ArrayList<Connection> connections){
		this.name = name;
		this.population = population;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.connections = connections;
	}
	
	/**
	 * @return the name of the city
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return the population of the city
	 */
	public int getPopulation(){
		return population;
	}
	
	/**
	 * @return the x-coordinate of the city
	 */
	public double getXCoord(){
		return xCoord;
	}
	
	/**
	 * @return the y-coordinate of the city
	 */
	public double getYCoord(){
		return yCoord;
	}
	
	/**
	 * Calculates the straight-line distance to another city.
	 * 
	 * @param c the target city
	 * @return straight-line distance between this and c
	 */
	public double distanceTo(City c){
		return Math.sqrt(Math.pow(c.xCoord - this.xCoord, 2) + Math.pow(c.yCoord - this.yCoord, 2));
	}
	
	public String toString(){
		return name;
	}
		public ArrayList<Connection> getConnections()
	{
		return this.connections;
	}
}
