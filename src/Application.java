
public class Application {

	public Application(){
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		new ApplicationWindow(map);
	}
	
}
