package fr.cnrs.swinggraph;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Iterator;

import javax.swing.ImageIcon;

public abstract class Graph<N> {
	private Layout<N> layout;

	public Graph() {
		this(new WanderingNodes<N>());
	}

	public Graph(Layout<N> algo) {
		this.layout = algo;
	}

	public Layout<N> getLayout() {
		return layout;
	}

	void setLayout(Layout<N> l) {
		this.layout = l;

		for (Node<N> n : nodes()) {
			n.layoutSpecifics = l.createSpecific();
		}
	}

	public abstract void connect(N src, N dest);

	public abstract int getNbEdges();

	public abstract Node<N> addNode(N u);

	public abstract void removeNode(N u);

	public void updateValues() {
		for (Node<N> n : nodes()) {
			if (n.dynamic) {
				ImageIcon icon = getIcon(n.o);

				if (n.originalIcon != icon) {
					n.originalIcon = icon;
					n.rescaledIcon = null;
				}

				n.size = getSize(n.o);
				n.text = getText(n.o);
				n.color = getLineColor(n.o);
				n.fillColor = getFillColor(n.o);
			}
		}
	}

	protected abstract Color getLineColor(N u);

	protected abstract Color getEdgeColor(Node<N> u, Node<N> v);

	protected abstract Stroke getEdgeStroke(Node<N> u, Node<N> v);

	protected abstract Color getFillColor(N u);

	protected abstract int getSize(N u);

	protected abstract String getText(N u);

	protected abstract ImageIcon getIcon(N u);

	public abstract boolean isArc(Node<N> u, Node<N> v);

	public boolean connected(Node<N> u, Node<N> v) {
		return isArc(u, v) || isArc(v, u);
	}

	public Iterable<Node<N>> nodes() {
		return () -> nodeIterator();
	}

	protected abstract Iterator<Node<N>> nodeIterator();

	public Iterable<EdgeCursor<N>> successors(Node<N> n) {
		return () -> successorIterator(n);
	}

	protected abstract Iterator<EdgeCursor<N>> successorIterator(Node<N> n);

	public abstract int nbNodes();
}