package fr.cnrs.swinggraph;

import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class WanderingNodes<N> extends Layout<N> {
	class MoveNode {
		int dx, dy;
	}

	Random prng = new Random();

	@Override
	public long step(GraphComponent<N> g, Rectangle r) {
		long nbChanges = 0;

		for (Node<N> u : g.getGraph().nodes()) {
			if (g.selectedNode == u) {
				continue;
			}

			MoveNode mm = (MoveNode) u.layoutSpecifics;

			mm.dx += prng.nextInt(3) - 1;
			u.x += mm.dx;

			// hits left wall
			if (u.x < r.x) {
				u.x = r.x;
				mm.dx = mm.dy = 0;
			}

			// hits right wall
			if (u.x > r.x + r.width) {
				u.x = r.x + r.width;
				mm.dx = mm.dy = 0;
			}

			mm.dy += prng.nextInt(3) - 1;
			u.y += mm.dy;

			// hits roof wall
			if (u.y < r.y) {
				u.y = r.y;
				mm.dx = mm.dy = 0;
			}

			// hits bottom wall
			if (u.y > r.y + r.height) {
				u.y = r.y + r.height;
				mm.dx = mm.dy = 0;
			}

			++nbChanges;
		}

		return nbChanges;
	}

	@Override
	public JComponent getControls() {
		return new JLabel("no algo control");
	}

	@Override
	public Object createSpecific() {
		return new MoveNode();
	}

}
