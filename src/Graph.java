import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph<T, L, K> implements Iterable<Graph.Node> {

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

	public class Node {
		ArrayList<Link> links;
		T element;

		public Node(T element) {
			links = new ArrayList();
			this.element = element;
		}

		private boolean addLink(Link link) {
			links.add(link);
			throw new UnsupportedOperationException();
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

	@Override
	public Iterator<Graph.Node> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
