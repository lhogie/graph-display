package fr.cnrs.swinggraph.demo;

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

import fr.cnrs.swinggraph.DefaultGraph;
import fr.cnrs.swinggraph.Graph;
import fr.cnrs.swinggraph.GraphComponent;
import fr.cnrs.swinggraph.Node;
import toools.thread.Threads;

public class Demo3_growing {
	public static void main(String[] args) {
		System.out.println("Running " + Demo3_growing.class);

		Graph<Integer> g = new DefaultGraph<>();
		GraphComponent<Integer> c = new GraphComponent<>(g);
		JFrame f = new JFrame();
		f.setSize(800, 600);
		f.setContentPane(c.bundleComponent());
		f.setVisible(true);
		c.maxEdges = 500;

		g.addNode(0);
		f.setTitle("1 node in graph");

		for (int nbNodes = g.nbNodes();; nbNodes *= 2) {
			for (int newNode = g.nbNodes(); newNode < nbNodes; ++newNode) {
				int hook = ThreadLocalRandom.current().nextInt(g.nbNodes());
				Node<Integer> newNodeNode = g.addNode(newNode);
				newNodeNode.dynamic = false;
				newNodeNode.size = 5;
				newNodeNode.text = newNode + "";
				newNodeNode.textBox = false;
				g.connect(newNode, hook);
			}

			int waitS = 2;
			f.setTitle(nbNodes + " nodes in graph. " + g.getNbEdges()
					+ " edge(s). Waiting " + waitS + "s.");
			Threads.sleepMs(1000 * waitS);
		}
	}
}
