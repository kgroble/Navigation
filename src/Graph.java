import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

public class Graph<T, L, K> {

	HashMap<K, Node> nodes;

	public Graph() {
		nodes = new HashMap<K, Node>();
	}

	/**
	 * Inserts the element into the Graph as an unlinked node
	 * 
	 * @param element
	 *            The element of type T to add
	 * @param key
	 *            Used as a key/value pair
	 * @return True is the element was inserted
	 */
	public boolean add(T element, K key) {
		if (nodes.containsKey(key))
			return false;
		Node node = new Node(element);
		nodes.put(key, node);
		return true;
	}

	/**
	 * Adds a one way connection between two nodes
	 * 
	 * @param from
	 *            the key of the object the link is from
	 * @param to
	 *            the key of the object the link is to
	 * @param connection
	 *            the link object to be stored with this link
	 * @return True if the link is added
	 */
	public boolean addLink(K from, K to, L connection) {
		if (nodes.containsKey(from) && nodes.containsKey(to)) {
			Link link = new Link(nodes.get(to), connection);
			nodes.get(from).addLink(link);
		}
		return false;
	}

	public boolean remove(T element) {
		throw new UnsupportedOperationException();
	}

	public boolean removeLink(K from, K to) {
		throw new UnsupportedOperationException();
	}

	public Collection<Node> getNodes() {
		return nodes.values();
	}

	/**
	 * gets an element from the graph
	 * 
	 * @param key
	 *            The key of the element
	 * @return the element
	 */
	public T get(K key) {
		if (nodes.containsKey(key)) {
			return nodes.get(key).element;
		}
		return null;
	}
	
	
	/**
	 * @return an arrayList of the elements that are linked to e
	 */
	public ArrayList<T> getConnectedElements(K key)
	{
		if (nodes.containsKey(key))
		{
			ArrayList<T> connectedElement = new ArrayList<T>();
			Node node = nodes.get(key);
			for (Link L : node.links)
			{
				connectedElement.add(L.connection.element);
			}
			return connectedElement;
		}
		return null;
	}
	
	//TODO: This search is slow.  Might want to improve prerformance here
	/**
	 * 
	 * throws an exception if there is no link between those two nodes if if one of the nodes doesn't exist
	 * 
	 * @param element1 the 'from' element
	 * @param element2 the 'to' element
	 * @return L object that links the two elements
	 */
	public L getConnectionBetween(K element1, K element2)
	{
		if (nodes.containsKey(element1) && nodes.containsKey(element2))
		{
			Node node1 = nodes.get(element1);
			Node node2 = nodes.get(element2);
			for (Link link : node1.links)
			{
				if (link.connection == node2)
				{
					return link.link;
				}
			}
		}
		throw new RuntimeException("No link between elements");
	}

	public String toString() {
		String s = "";
		String linkStr = " ---> ";

		for (Node node : nodes.values()) {
			s += node.toString();
			s += linkStr;
			int spaceLen = node.toString().length() + linkStr.length();
			int i = 0;
			for (Link link : node.getLinks()) {
				if (i != 0) {
					for (int k = 0; k < spaceLen; k++) {
						s += " ";
					}
				}
				s += link.connection.toString();
				s += "\n";
				i++;
			}
			s += "\n";
		}
		return s;
	}

	public class Node {
		ArrayList<Link> links;
		T element;

		public Node(T element) {
			links = new ArrayList();
			this.element = element;
		}

		private boolean addLink(Link link) {
			links.add(link);
			// throw new UnsupportedOperationException();
			return true;
		}

		/**
		 * @return All the links from this node
		 */
		public ArrayList<Link> getLinks() {
			return links;
		}

		public String toString() {
			return element.toString();
		}
	}

	public class Link {
		Node connection;
		L link;

		public Link(Node connection, L link) {
			this.connection = connection;
			this.link = link;
		}
	}
}
