package fr.cnrs.glajasc;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Demo
{
	public static void main(String[] args)
	{
		SpringLayout l = new SpringLayout();
		double delta = 0.0005;
		l.attractionFactor = 1 - delta;
		l.repulsionFactor = 1 + delta;

		// creates a graphical graph using "wandering nodes" layout algorithm
		JGraph g = new JGraph()
		{
			@Override
			protected String getText(Node u)
			{
				return u.toString();
			}

			@Override
			protected int getSize(Node u)
			{
				return 10;
			}

			@Override
			protected ImageIcon getIcon(Node u)
			{
				return null;
			}

			@Override
			protected Color getFillColor(Node u)
			{
				return Color.white;
			}

			@Override
			protected Color getColor(Node u)
			{
				return Color.black;
			}

			@Override
			protected int getArcType(Node src, Node dest)
			{
				return 1;
			}
		};

		g.addEdge("Luc", "6K");
		g.addEdge("Luc", "Stéphane");
		g.addEdge("Danto", "6K");

		g.pauseDurationMs = 100;

		JFrame f = new JFrame("graph view");
		f.setSize(800, 600);

		f.setContentPane(g.getBundleComponent());
		f.setVisible(true);
		g.start();
		System.out.println("coucou :)");
	}
}
