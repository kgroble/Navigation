import java.util.ArrayList;
import java.util.HashMap;

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
			return true; // I assume this should be here, it was not earlier.
		}
		return false;
	}

	/**
	 * 
	 * @return an arrayList of all the elements stored in this graph
	 */
	public ArrayList<T> getElements() {
		ArrayList<T> elements = new ArrayList<T>();
		for (Node node : nodes.values()) {
			elements.add(node.element);
		}
		return elements;
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
	 * @return a Hashmap of keys and links of elements connected to the element
	 *         corresponding to the key passed
	 */
	public HashMap<T, L> getConnectedElements(K key) {
		if (nodes.containsKey(key)) {
			HashMap<T, L> connectedElements = new HashMap<T, L>();
			Node node = nodes.get(key);

			for (Link L : node.links) {
				connectedElements.put(L.connection.element, L.link);
			}
			return connectedElements;
		}
		return null;
	}

	/**
	 * Returns the graph as a string
	 */
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

	private class Node {
		ArrayList<Link> links;
		T element;

		public Node(T element) {
			links = new ArrayList<Link>();
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

	private class Link {
		Node connection;
		L link;

		public Link(Node connection, L link) {
			this.connection = connection;
			this.link = link;
		}
	}
}
