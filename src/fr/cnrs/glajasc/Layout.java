package fr.cnrs.glajasc;

import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * Describes an iterative layout algorithm
 * 
 * @author lhogie
 *
 */

public abstract class Layout
{
	/*
	 * What does each step of the algorithm. Returns the number of changes done
	 * during this step.
	 */
	public abstract long step(JGraph g, Rectangle r);

	/*
	 * Returns a Swing component which contains the graphical controllers for
	 * the given algorithm.
	 */
	public abstract JComponent getControls();

	/*
	 * create a node class specific to this algo.
	 */
	public Node createNode(Object e)
	{
		return new Node(e);
	}

	public void run(JGraph g, Rectangle r)
	{
		while (step(g, r) > 0)
			;
	}

	public void center(JGraph g, Rectangle r)
	{
		int x = 0, y = 0;
		int nbNodes = 0;

		for (Node n : g.nodes)
		{
			x += n.x;
			y += n.y;
			++nbNodes;
		}

		x /= nbNodes;
		y /= nbNodes;

		int xshift = r.width / 2 - x;
		int yshift = r.height / 2 - y;

		for (Node n : g.nodes)
		{
			n.x += xshift;
			n.y += yshift;
		}
	}

}
