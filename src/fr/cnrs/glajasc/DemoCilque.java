package fr.cnrs.glajasc;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class DemoCilque {
	public static void main(String[] args) {

		int side = 4;
		int n = side * side;

		// creates a graphical graph using "wandering nodes" layout algorithm
		JGraph g = new JGraph(new WanderingNodes()) {
			@Override
			public int getArcType(Node src, Node dest) {
//				return 1;
				int u = (int) src.data;
				int v = (int) dest.data;

				if (u < v) {
					if (u + 1 == v && (u + 1) % side != 0)
						return 1;

					if (u + side == v && u % side != 0)
						return 1;
				}

				return 0;
			}

			@Override
			public String getText(Node u) {
				return null;
			}

			@Override
			public Color getLineColor(Node u) {
				return Color.black;
			}

			@Override
			public Color getFillColor(Node u) {
				return Color.white;
			}

			@Override
			public int getSize(Node u) {
				return 10;
			}

			@Override
			public ImageIcon getIcon(Node u) {
				return null;
			}
		};


		for (int i = 0; i < n; ++i) {
			g.addNode(i);
		}

		g.pauseDurationMs = 1000;
		JFrame f = new JFrame("graph view");
		f.setSize(800, 600);

		f.setContentPane(g.getBundleComponent());
		f.setVisible(true);
		g.start();
		System.out.println("coucou :)");
	}
}
