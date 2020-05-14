package fr.cnrs.glajasc;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.ImageIcon;

public class Node<N> {
	Node(N e) {
		this.o = e;
	}

	Collection<Node<N>> successors = new HashSet<>();

	Object layoutSpecifics;
	boolean stillHere;
	public final N o;
	int x, y;
	public ImageIcon originalIcon;
	public ImageIcon rescaledIcon;
	public int size;
	public String text;
	public Color fillColor;
	public Color color;
}
