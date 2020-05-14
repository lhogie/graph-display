package fr.cnrs.glajasc;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import toools.io.JavaResource;

public class Demo {
	public static void main(String[] args) {
		ImageIcon animal = new ImageIcon(
				new JavaResource(Demo.class, "animal.png").getByteArray());

		// creates a graphical graph using "wandering nodes" layout algorithm
		JGraph g = new JGraph(new WanderingNodes()) {
			@Override
			protected String getText(Node u) {
				if (u.data.equals("animal")) {
					return null;
				}

				return u.data.toString();
			}

			@Override
			protected int getSize(Node u) {
				String text = getText(u);

				if (text == null) {
					return 100;
				}

				return 5 * text.length();
			}

			@Override
			protected ImageIcon getIcon(Node u) {
				if (u.data.equals("animal")) {
					return animal;
				}

				return null;
			}

			@Override
			protected Color getFillColor(Node u) {
				String text = getText(u);

				if (text == null) {
					return null;
				}
				else if (text.equalsIgnoreCase("red")) {
					return Color.red;
				}
				else if (text.equalsIgnoreCase("yellow")) {
					return Color.yellow;
				}
				else if (text.equalsIgnoreCase("green")) {
					return Color.green;
				}
				else if (text.equalsIgnoreCase("white")) {
					return Color.white;
				}
				else {
					return null;
				}
			}

			@Override
			protected Color getLineColor(Node u) {
				return getText(u) == null ? null : Color.black;
			}

			@Override
			protected int getArcType(Node src, Node dest) {
				return 1;
			}
		};

		g.addEdge("red", "yellow");
		g.addEdge("red", "green");
		g.addEdge("white", "yellow");
		g.addEdge("red", "animal");

		g.pauseDurationMs = 100;

		JFrame f = new JFrame("Just a coloured graph");
		f.setSize(800, 600);

		f.setContentPane(g.getBundleComponent());
		f.setVisible(true);
		g.start();
		System.out.println("coucou :)");
	}
}
