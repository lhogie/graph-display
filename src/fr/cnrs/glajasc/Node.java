package fr.cnrs.glajasc;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.ImageIcon;

public class Node<N> {
	Node(N e) {
		this.o = e;
	}

	public final N o;
	final Collection<Node<N>> successors = new HashSet<>();
	int x, y;
	Object layoutSpecifics;
	public boolean dynamic = true;

	public ImageIcon originalIcon;
	public ImageIcon rescaledIcon;
	public int size;
	public String text;
	public Color fillColor;
	public Color color;
	public boolean textBox = true;

}
