package fr.cnrs.swinggraph;

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

public class Demo1 {
	public static void main(String[] args) {
		System.out.println("Running " + Demo1.class);

		DefaultGraph<Integer> g = new DefaultGraph<>();
		int nbNodes = 100;

		for (int i = 0; i < nbNodes; ++i) {
			g.connect(i, ThreadLocalRandom.current().nextInt(nbNodes));
		}

		JFrame f = new JFrame("Just a graph");
		f.setSize(800, 600);
		f.setContentPane(g.bundleComponent());
		f.setVisible(true);
		g.start();
	}
}
