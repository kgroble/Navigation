/**
 * Sets up the underlying graph from information
 * read from file.
 *
 */
public class Setup {
	private Graph g;
	
	public Setup(Graph g){
		this.g = g;
		addCities();
		addLinks();
	}
	
	/**
	 * Loads cities from file.
	 */
	public void addCities(){
		
	}
	
	/**
	 * Loads links from file.
	 */
	public void addLinks(){
		
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
		
	}
	
	/**
	 * Loads an individual link (may be unnecessary here, instead in Graph class)
	 * 
	 * @param c1
	 * @param c2
	 */
	public void addLink(City c1, City c2){
		
	}
}
