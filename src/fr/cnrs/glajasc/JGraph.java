package fr.cnrs.glajasc;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class JGraph<N> extends JPanel {
	private Layout<N> layout;
	public long pauseDurationMs = 30;
	Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	Node<N> selectedNode;
	Map<N, Node<N>> nodes = new HashMap<>();

	int mt, mr, mb, ml;
	Rectangle layoutArea;

	public static Stroke line = new BasicStroke();
	final static float dash1[] = {10.0f};
	public static Stroke dotted = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);

	public JGraph() {
		this(new WanderingNodes<N>());
	}

	void setLayout(Layout<N> l) {
		this.layout = l;

		for (Node<N> n : nodes()) {
			n.layoutSpecifics = l.createSpecific();
		}
	}

	public void connect(N src, N dest) {
		Node<N> u = ensureExists(src);
		Node<N> v = ensureExists(dest);
		u.successors.add(v);
	}

	public Node<N> add(N u) {
		return ensureExists(u);
	}

	public void remove(N u) {
		nodes.remove(u);

		for (Node<N> n : nodes()) {
			n.successors.remove(u);
		}
	}

	protected Node<N> ensureExists(N o) {
		Node<N> n = nodes.get(o);

		if (n == null) {
			nodes.put(o, n = new Node<N>(o));
			n.layoutSpecifics = layout.createSpecific();
		}

		return n;
	}

	public JGraph(Layout<N> algo) {
		this.layout = algo;
		setBorder(new EmptyBorder(mr, ml, mb, mr));

		addMouseListener(new MouseAdapter() {
			MouseMotionListener ml = new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					selectedNode.x = e.getX();
					selectedNode.y = e.getY();
				}
			};

			@Override
			public void mouseReleased(MouseEvent e) {
				removeMouseMotionListener(ml);
				selectedNode = null;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedNode = findClosestNode(e.getX(), e.getY());
				addMouseMotionListener(ml);
			}

			private Node<N> findClosestNode(int x, int y) {
				double minD = Double.MAX_VALUE;
				Node<N> minN = null;

				for (Node<N> u : nodes.values()) {
					double dx = Math.abs(x - u.x);
					double dy = Math.abs(y - u.y);
					double d = Math.sqrt(dx * dx + dy * dy);

					if (d < minD) {
						minD = d;
						minN = u;
					}
				}

				return minN;
			}
		});
	}

	public void updateValues() {
		for (Node<N> n : nodes()) {
			ImageIcon icon = getIcon(n.o);

			if (n.originalIcon != icon) {
				n.originalIcon = icon;
				n.rescaledIcon = null;
			}

			n.size = getSize(n.o);
			n.text = getText(n.o);
			n.color = getLineColor(n.o);
			n.fillColor = getFillColor(n.o);
		}
	}

	public void start() {
		new Thread(() -> {
			int nbSteps = 0;

			while (true) {
				if (isVisible()) {
					updateLayoutArea();

					if (layoutArea.width > 0 && layoutArea.height > 0) {
						if (nbSteps == 0) {
							shuffle(new Random());
						}

						layout.step(JGraph.this, layoutArea);
						layout.center(JGraph.this, layoutArea);
						++nbSteps;
					}
				}

				repaint();

				try {
					Thread.sleep(pauseDurationMs);
				}
				catch (InterruptedException e) {
				}
			}
		}).start();
	}

	private void updateLayoutArea() {
		int ml = 50;
		int mr = 50;
		int mb = 50;
		int mt = 50;

		Dimension size = getSize();
		Insets insets = getInsets();

		if (layoutArea == null)
			layoutArea = new Rectangle();

		layoutArea.x = getInsets().left + ml;
		layoutArea.y = getInsets().right + mt;
		layoutArea.width = size.width - insets.left - insets.right - ml - mr;
		layoutArea.height = size.height - insets.top - insets.bottom - mb - mt;

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	// RenderingHints rh = new
	// RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	private void doDrawing(Graphics g) {
		updateValues();
		Graphics2D g2 = (Graphics2D) g;
		// g2.setRenderingHints(rh);
		g2.setStroke(stroke);

		for (Node<N> u : nodes()) {
			for (Node<N> v : u.successors) {
				Color edgeColor = getEdgeColor(u, v);
				Stroke edgeStroke = getEdgeStroke(u, v);
				g2.setStroke(edgeStroke);
				g2.setColor(edgeColor);
				g2.drawLine(u.x, u.y, v.x, v.y);
			}
		}

		for (Node<N> u : nodes()) {
			if (u.size == 0) {
				// do nothing, node can't be seen
			}
			else if (u.size == 1) {
				// draw a point
				g.drawLine(u.x, u.y, u.x, u.y);
			}
			else if (u.originalIcon != null) {
				if (u.rescaledIcon == null) {
					u.rescaledIcon = new ImageIcon(
							u.originalIcon.getImage().getScaledInstance( - 1, u.size, 0));
				}

				g.drawImage(u.rescaledIcon.getImage(),
						(int) (u.x - u.rescaledIcon.getIconWidth() / 2),
						(int) (u.y - u.rescaledIcon.getIconHeight() / 2), this);
			}
			else if (u.text != null) {
				GlyphVector vec = GlyphVectorCache.get(u.text);

				if (vec == null) {
					FontRenderContext frc = g2.getFontRenderContext();
					Font font = getFont().deriveFont((float) u.size);
					vec = font.createGlyphVector(frc, u.text);
					GlyphVectorCache.put(u.text, vec);
				}

				int gap = 6;
				int textW = (int) vec.getVisualBounds().getWidth();
				int textH = (int) vec.getVisualBounds().getHeight();

				if (u.fillColor != null) {
					g.setColor(u.fillColor);
					g2.fillRect(u.x - textW / 2 - gap, u.y - textH / 2 - gap,
							textW + 2 * gap, textH + 2 * gap);
				}

				if (u.color != null) {
					g.setColor(u.color);
					g2.drawRect(u.x - textW / 2 - gap, u.y - textH / 2 - gap,
							textW + 2 * gap, textH + 2 * gap);
					g2.drawGlyphVector(vec, u.x - textW / 2, u.y + textH / 2 + 2);
				}

			}
			else {
				Color fillColor = u.fillColor;

				if (fillColor != null) {
					g.setColor(fillColor);
					g.fillOval(u.x - u.size / 2, u.y - u.size / 2, u.size, u.size);
				}

				Color lineColor = u.color;

				if (lineColor != null) {
					g.setColor(lineColor);
					g.drawOval(u.x - u.size / 2, u.y - u.size / 2, u.size, u.size);
				}
			}
		}
	}

	protected abstract Color getLineColor(N u);

	protected abstract Color getEdgeColor(Node<N> u, Node<N> v);

	protected abstract Stroke getEdgeStroke(Node<N> u, Node<N> v);

	protected abstract Color getFillColor(N u);

	protected abstract int getSize(N u);

	private static Map<String, GlyphVector> GlyphVectorCache = new HashMap<>();

	protected abstract String getText(N u);

	protected abstract ImageIcon getIcon(N u);

	public void shuffle(Random prng) {
		for (Node<N> u : nodes.values()) {
			u.x = prng.nextInt(layoutArea.width) + layoutArea.x;
			u.y = prng.nextInt(layoutArea.height) + layoutArea.y;
		}
	}

	public JComponent getControls() {
		JPanel p = new JPanel(new GridLayout(2, 1));
		p.add(new GraphControls(this));
		p.add(layout.getControls());
		return p;
	}

	public JComponent bundleComponent() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(this, BorderLayout.CENTER);
		p.add(getControls(), BorderLayout.SOUTH);
		return p;
	}

	public boolean isArc(Node<N> u, Node<N> v) {
		return u.successors.contains(v);
	}

	public boolean connected(Node<N> u, Node<N> v) {
		return isArc(u, v) || isArc(v, u);
	}

	public Collection<Node<N>> nodes() {
		return nodes.values();
	}

}
