package fr.cnrs.swinggraph.demo;

import javax.swing.JFrame;

import fr.cnrs.swinggraph.DefaultGraph;
import fr.cnrs.swinggraph.Graph;
import fr.cnrs.swinggraph.GraphComponent;

public class Demo4_stringgraph {
	public static void main(String[] args) {
		System.out.println("Running " + Demo4_stringgraph.class);

		String s = "my graph";
		Graph<Character> g = new DefaultGraph<>();

		for (int i = 1; i < s.length(); ++i) {
			g.connect(s.charAt(i - 1), s.charAt(i));
		}

		JFrame f = new JFrame("Just a graph");
		f.setSize(800, 600);
		f.setContentPane(new GraphComponent<>(g).bundleComponent());
		f.setVisible(true);
	}
}
