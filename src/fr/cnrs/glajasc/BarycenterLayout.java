package fr.cnrs.glajasc;

import java.awt.Rectangle;

public class BarycenterLayout extends ForceBasedAlgo
{

	@Override
	public long step(JGraph g, Rectangle r)
	{
		long n = 0;

		for (Node u : g.nodes)
		{
			double xA = 0, yA = 0, xR = 0, yR = 0;
			int nbA = 0, nbR = 0;

			for (Node v : g.nodes)
			{
				if (u != v)
				{
					boolean neighbors = g.getArcType(u, v) != 0 || g.getArcType(v, u) != 0;

					if (neighbors)
					{
						xA += v.x;
						yA += v.y;
						++nbA;
					}
					else
					{
						xR += v.x;
						yR += v.y;
						++nbR;
					}

					double fX = (xA + xR) ;
					double fY = (yA + yR);

					if (fX != u.x || fY != u.y)
						++n;

					u.x = (int) fX;
					u.y = (int) fY;
					System.out.println(u.x  + " ," + u.y);
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
