import static org.junit.Assert.*;

import org.junit.Test;


public class SetupTest {

	@Test
	public void test() {
		
		Graph g = new Graph();
		Setup setup = new Setup(g);
		setup.addCities();
		setup.addLinks();
		
		System.out.println(g.toString());
	}

}
