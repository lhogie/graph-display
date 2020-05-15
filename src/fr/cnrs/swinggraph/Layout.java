package fr.cnrs.swinggraph;

import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * Describes an iterative layout algorithm
 * 
 * @author lhogie
 *
 */

public abstract class Layout<N> {
	/*
	 * What does each step of the algorithm.
	 */
	public abstract long step(Graph<N> g, Rectangle r);

	/*
	 * Returns a Swing component which contains the graphical controllers for
	 * the given algorithm.
	 */
	public abstract JComponent getControls();

	/*
	 * create an instance holding specific layout data for a node.
	 */
	public abstract Object createSpecific();

	public void run(Graph<N> g, Rectangle r) {
		while (step(g, r) > 0)
			;
	}

	public void center(Graph<N> g, Rectangle r) {
		int x = 0, y = 0;
		int nbNodes = 0;

		for (Node<N> n : g.nodes()) {
			x += n.x;
			y += n.y;
			++nbNodes;
		}

		if (nbNodes == 0) {
			return;
		}

		x /= nbNodes;
		y /= nbNodes;

		int xshift = r.width / 2 - x;
		int yshift = r.height / 2 - y;

		for (Node<N> n : g.nodes()) {
			n.x += xshift;
			n.y += yshift;
		}
	}
}
