public class Application {

	public Application(){
		Graph<City,Connection,String> map = new Graph<City,Connection,String>();
		Setup setup = new Setup(map); 
		new ApplicationWindow(map);
	}
	
}
