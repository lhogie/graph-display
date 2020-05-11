package egviz;

import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class WanderingNodes extends Layout
{
	class MoveNode extends Node
	{
		int dx, dy;

		public MoveNode(Object e)
		{
			super(e);
		}
	}

	Random prng = new Random();

	@Override
	public long step(JGraph g, Rectangle r)
	{
		for (Node u : g.nodes)
		{
			if (u.isSelected)
				continue;

			MoveNode mm = (MoveNode) u;

			mm.dx += prng.nextInt(3) - 1;
			u.x += mm.dx;

			// hits left wall
			if (u.x < r.x)
			{
				u.x = r.x;
				mm.dx = mm.dy = 0;
			}

			// hits right wall
			if (u.x > r.x + r.width)
			{
				u.x = r.x + r.width;
				mm.dx = mm.dy = 0;
			}

			mm.dy += prng.nextInt(3) - 1;
			u.y += mm.dy;

			// hits roof wall
			if (u.y < r.y)
			{
				u.y = r.y;
				mm.dx = mm.dy = 0;
			}

			// hits bottom wall
			if (u.y > r.y + r.height)
			{
				u.y = r.y + r.height;
				mm.dx = mm.dy = 0;
			}
		}

		return g.nodes.size();
	}

	@Override
	public JComponent getControls()
	{
		return new JLabel("no algo control");
	}

	@Override
	public Node createNode(Object e)
	{
		return new MoveNode(e);
	}

}
