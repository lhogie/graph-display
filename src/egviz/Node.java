package egviz;

import javax.swing.ImageIcon;

public class Node
{
	public Node(Object e)
	{
		this.data = e;
	}

	int x, y;
	public ImageIcon icon;
	public boolean icon_processed;
	boolean isSelected;
	public Object data;
	public Object text;
}