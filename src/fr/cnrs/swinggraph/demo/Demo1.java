package fr.cnrs.swinggraph.demo;

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

import fr.cnrs.swinggraph.DefaultGraph;
import fr.cnrs.swinggraph.Graph;
import fr.cnrs.swinggraph.GraphComponent;

public class Demo1 {
	public static void main(String[] args) {
		System.out.println("Running " + Demo1.class);

		Graph<Integer> g = new DefaultGraph<>();
		int nbNodes = 100;

		for (int i = 0; i < nbNodes; ++i) {
			g.connect(i, ThreadLocalRandom.current().nextInt(nbNodes));
		}

		JFrame f = new JFrame("Just a graph");
		f.setSize(800, 600);
		f.setContentPane(new GraphComponent<>(g).bundleComponent());
		f.setVisible(true);
	}
}
