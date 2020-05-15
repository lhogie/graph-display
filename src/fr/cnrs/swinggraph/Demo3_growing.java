package fr.cnrs.swinggraph;

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

import toools.thread.Threads;

public class Demo3_growing {
	public static void main(String[] args) {
		System.out.println("Running " + Demo3_growing.class);

		DefaultGraph<Integer> g = new DefaultGraph<>();

		JFrame f = new JFrame();
		f.setSize(800, 600);
		f.setContentPane(g.bundleComponent());
		f.setVisible(true);
		g.start();
		g.maxEdges = 10;

		g.ensureExists(0);
		f.setTitle("1 node in graph");

		for (int nbNodes = g.nodes().size();; nbNodes *= 2) {
			for (int newNode = g.nodes().size(); newNode < nbNodes; ++newNode) {
				Node<Integer> newNodeNode = g.addNode(newNode);
				newNodeNode.dynamic = false;
				newNodeNode.size = 5;
				newNodeNode.text = newNode + "";
				newNodeNode.textBox = false;
				int alreadyInGraph = ThreadLocalRandom.current()
						.nextInt(g.nodes().size());
				g.connect(newNode, alreadyInGraph);
			}

			int waitS = 2;
			f.setTitle(nbNodes + " nodes in graph. " + g.getNbEdges() + " edge(s). Sampling factor: " + g.samplingFactor() + ". Waiting "
					+ waitS + "s.");
			Threads.sleepMs(1000 * waitS);
		}
	}
}