package egviz;

import java.awt.Rectangle;

public class SpringLayout extends ForceBasedAlgo
{

	@Override
	public long step(JGraph g, Rectangle r)
	{
		long n = 0;
		double maxDistance = Math.sqrt(r.width * r.width + r.height * r.height);

		for (Node u : g.nodes)
		{
			for (Node v : g.nodes)
			{
				if (u != v)
				{
					double dx = v.x - u.x;
					double dy = v.y - u.y;
					double d = Math.sqrt(dx * dx + dy * dy);

					boolean neighbors = g.getArcType(u, v) != 0;

					double factor = neighbors ? attractionFactor : repulsionFactor;

					// the force decreases by the distance
					factor = (factor - 1) / (d / maxDistance) + 1;

					double xshift = dx * factor - dx;
					u.x = (int) ensureBounds(u.x + xshift, r.x, r.x + r.width);

					double yshift = dy * factor - dy;
					u.y = (int) ensureBounds(u.y + yshift, r.y, r.y + r.height);

					if (xshift > 0 || yshift > 0)
						++n;
				}
			}
		}

		return n;
	}

	private double ensureBounds(double x, double lb, double up)
	{
		if (x < lb)
			return lb;

		if (x > up)
			return up;

		return x;
	}
}
