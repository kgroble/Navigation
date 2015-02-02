import java.util.ArrayList;
import java.util.Iterator;


public class Graph<T extends Comparable<? super T>> implements
		Iterable<Graph.Node> {

	
	
	public class Node
	{
		ArrayList<Link> links;
		
		public Node()
		{
			links = new ArrayList();
			
		}
	}
	
	public class Link
	{
		
	}

	@Override
	public Iterator<Graph.Node> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
