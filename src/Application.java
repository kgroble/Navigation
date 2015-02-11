public class Application {
	
	public Application(){
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map);
		AStar a = new AStar(map);
		new ApplicationWindow(map, a);
	}
	
}
