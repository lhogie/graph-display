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

		// create 100 edges
		for (int i = 0; i < 100; ++i) {
			g.connect(i, ThreadLocalRandom.current().nextInt(100));
		}

		JFrame f = new JFrame("Just a graph of ints");
		f.setSize(800, 600);
		f.setContentPane(new GraphComponent<>(g).bundleComponent());
		f.setVisible(true);
	}
}
