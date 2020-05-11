package egviz;

import java.awt.Rectangle;
import java.util.Random;

import mdsj.MDSJ;

public class SpringLayout2 extends ForceBasedAlgo
{

	@Override
	public long step(JGraph g, Rectangle r)
	{

		int n = g.nodes.size();
		Node[] nodes = new Node[n];
		int i = 0;

		for (Node nn : g.nodes)
		{
			nodes[i] = nn;
		}

		System.out.println(g.layoutArea);
		g.shuffle(new Random());
		double[][] input = new double[n][n];

		for (int u = 0; u < n; ++u)
		{
			for (int v = 0; v < n; ++v)
			{
				if (u != v)
				{
					boolean neighbors = g.getArcType(nodes[u], nodes[v]) != 0
							|| g.getArcType(nodes[v], nodes[u]) != 0;
					input[u][v] = neighbors ? 1 : 2;
				}
			}
		}

		double[][] output = MDSJ.classicalScaling(input);

		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE,
				maxY = Double.MIN_VALUE;

		for (double x : output[0])
		{
			minX = x < minX ? x : minX;
			maxX = x > maxX ? x : maxX;
		}

		for (double y : output[0])
		{
			minY = y < minY ? y : minY;
			maxY = y > maxY ? y : maxY;
		}

		for (int u = 0; u < n; ++u)
		{
			double x = output[0][u];
			nodes[u].x = (int) (x * r.width / (maxX - minX)
					- minX * (r.width / (maxX - minX)));

			double y = output[1][u];
			nodes[u].y = (int) (y * r.height / (maxY - minY)
					- minY * (r.height / (maxY - minY)));
		}
		return 0;
	}

}
