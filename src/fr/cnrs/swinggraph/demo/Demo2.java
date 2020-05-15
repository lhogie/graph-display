package fr.cnrs.swinggraph.demo;

import java.awt.Color;
import java.awt.Stroke;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import fr.cnrs.swinggraph.DefaultGraph;
import fr.cnrs.swinggraph.Graph;
import fr.cnrs.swinggraph.GraphComponent;
import fr.cnrs.swinggraph.Node;
import toools.io.JavaResource;

public class Demo2 {
	public static void main(String[] args) {
		System.out.println("Running " + Demo2.class);

		ImageIcon animal = new ImageIcon(
				new JavaResource(Demo2.class, "animal.png").getByteArray());

		// creates a graphical graph using "wandering nodes" layout algorithm
		Graph<String> g = new DefaultGraph<String>() {
			@Override
			protected String getText(String u) {
				if (u.equals("animal")) {
					return null;
				}

				return u;
			}

			@Override
			protected int getSize(String u) {
				if (u.equals("animal")) {
					return 100;
				}
				return 5 * u.length();
			}

			@Override
			protected ImageIcon getIcon(String u) {
				if (u.equals("animal")) {
					return animal;
				}

				return null;
			}

			@Override
			protected Color getFillColor(String text) {
				if (text.equalsIgnoreCase("red")) {
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
					return Color.lightGray;
				}
			}

			@Override
			protected Color getLineColor(String u) {
				return getText(u) == null ? null : Color.black;
			}

			@Override
			protected Color getEdgeColor(Node<String> u, Node<String> v) {
				if (u.o.equalsIgnoreCase("animal")) {
					return Color.red;
				}

				return Color.black;
			}

			@Override
			protected Stroke getEdgeStroke(Node<String> u, Node<String> v) {
				if (u.o.equalsIgnoreCase("animal")) {
					return GraphComponent.dotted;
				}

				return GraphComponent.line;
			}
		};

		g.connect("Luc", "animal");
		g.connect("animal", "drums");
		g.connect("animal", "yellow");
		g.connect("animal", "red");

		JFrame f = new JFrame("Just a coloured graph");
		f.setSize(800, 600);

		f.setContentPane(new GraphComponent<>(g).bundleComponent());
		f.setVisible(true);
	}
}
