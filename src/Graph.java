import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Graph<T extends Comparable<? super T>, K> implements
		Iterable<Graph.Node> {

	HashMap<K, Node> nodes;
	
	public Graph()
	{
		nodes = new HashMap<K, Node>();
	}
	
	public boolean add(T element, K key)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean addLink(K from, K to)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean remove(T element)
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean removeLink(K from, K to)
	{
		throw new UnsupportedOperationException();
	}
	
	
	public class Node
	{
		ArrayList<Link> links;
		T element;
		
		public Node()
		{
			links = new ArrayList();
		}
		
		private boolean addLink(Link link){
			links.add(link);
			throw new UnsupportedOperationException();
		}
	}
	
	public class Link
	{
		Node connection;
		public Link(Node connection)
		{
			this.connection = connection;
		}
	}

	@Override
	public Iterator<Graph.Node> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
