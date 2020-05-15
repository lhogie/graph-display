package fr.cnrs.swinggraph;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;

public class DefaultGraph<N> extends Graph<N> {
	Map<N, Node<N>> nodes = new HashMap<>();
	
	private int nbEdges = 0;

	public DefaultGraph() {
		this(new WanderingNodes<N>());
	}

	public DefaultGraph(Layout<N> layout) {
		super(layout);
	}

	@Override
	public void connect(N src, N dest) {
		Node<N> u = ensureExists(src);
		Node<N> v = ensureExists(dest);
		int formerDegree = u.successors.size();
		u.successors.add(v);
		int newDegree = u.successors.size();
		nbEdges += newDegree - formerDegree;
	}

	@Override
	public int getNbEdges() {
		return nbEdges;
	}

	@Override
	public Node<N> addNode(N u) {
		return ensureExists(u);
	}

	@Override
	public void removeNode(N u) {
		synchronized (this) {
			nodes.remove(u);
		}

		for (Node<N> n : nodes()) {
			int formerDegree = n.successors.size();
			n.successors.remove(u);
			int newDegree = n.successors.size();
			nbEdges -= newDegree - formerDegree;
		}
	}

	public Node<N> ensureExists(N o) {
		Node<N> n = nodes.get(o);

		if (n == null) {
			n = new Node<N>(o);
			n.layoutSpecifics = getLayout().createSpecific();

			synchronized (this) {
				nodes.put(o, n);
			}
		}

		return n;
	}

	@Override
	protected Color getLineColor(N u) {
		return Color.black;
	}

	@Override
	protected Color getEdgeColor(Node<N> u, Node<N> v) {
		return Color.black;
	}

	@Override
	protected Stroke getEdgeStroke(Node<N> u, Node<N> v) {
		return GraphComponent.line;
	}

	@Override
	protected Color getFillColor(N u) {
		return Color.lightGray;
	}

	@Override
	protected int getSize(N u) {
		return 10;
	}

	@Override
	protected String getText(N u) {
		return u.toString();
	}

	@Override
	protected ImageIcon getIcon(N u) {
		return null;
	}

	public boolean isArc(Node<N> u, Node<N> v) {
		return u.successors.contains(v);
	}

	public boolean connected(Node<N> u, Node<N> v) {
		return isArc(u, v) || isArc(v, u);
	}

	public Collection<Node<N>> nodes() {
		return nodes.values();
	}

	@Override
	protected Iterator<Node<N>> nodeIterator() {
		return nodes.values().iterator();
	}

	@Override
	protected Iterator<EdgeCursor<N>> successorIterator(Node<N> u) {
		Iterator<Node<N>> i = u.successors.iterator();
		final EdgeCursor<N> cursor = new EdgeCursor<N>();
		cursor.src = u;

		return new Iterator<EdgeCursor<N>>() {

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public EdgeCursor<N> next() {
				cursor.dest = i.next();
				cursor.color = getEdgeColor(u, cursor.dest);
				cursor.stroke = getEdgeStroke(u, cursor.dest);
				return cursor;
			}
		};
	}

	@Override
	public int nbNodes() {
		return nodes.size();
	}
}
