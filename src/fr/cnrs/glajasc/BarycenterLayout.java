package fr.cnrs.glajasc;

import java.awt.Rectangle;

public class BarycenterLayout<N> extends ForceBasedAlgo<N> {

	@Override
	public long step(JGraph<N> g, Rectangle r) {
		long n = 0;

		for (Node<N> u : g.nodes()) {
			double xA = 0, yA = 0, xR = 0, yR = 0;
			int nbA = 0, nbR = 0;

			for (Node<N> v : g.nodes()) {
				if (u != v) {
					boolean neighbors = g.connected(u, v);

					if (neighbors) {
						xA += v.x;
						yA += v.y;
						++nbA;
					}
					else {
						xR += v.x;
						yR += v.y;
						++nbR;
					}

					double fX = (xA + xR);
					double fY = (yA + yR);

					if (fX != u.x || fY != u.y)
						++n;

					u.x = (int) fX;
					u.y = (int) fY;
					System.out.println(u.x + " ," + u.y);
				}
			}
		}

		return n;
	}

	private double ensureBounds(double x, double lb, double up) {
		if (x < lb)
			return lb;

		if (x > up)
			return up;

		return x;
	}

	@Override
	public Object createSpecific() {
		return null;
	}
}
