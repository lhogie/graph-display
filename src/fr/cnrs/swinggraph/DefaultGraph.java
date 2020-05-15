package fr.cnrs.swinggraph;

import java.awt.Color;
import java.awt.Stroke;

import javax.swing.ImageIcon;

public class DefaultGraph<N> extends Graph<N> {

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

}
