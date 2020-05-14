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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class JGraph extends JPanel {
	private final Layout algo;
	public long pauseDurationMs = 30;
	Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private Node selectedNode;

	int mt, mr, mb, ml;
	Rectangle layoutArea;
	public final List<Node> nodes = new ArrayList<>();
	public final Map<Node, Set<Node>> adj = new HashMap<>();

	public JGraph() {
		this(new SpringLayout());
	}

	Map<Object, Node> value_node = new HashMap<>();

	public Node lookupNode(Object value) {
		return value_node.get(value);
	}

	public JGraph(Layout algo) {
		this.algo = algo;
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
				selectedNode.isSelected = false;
				removeMouseMotionListener(ml);
				selectedNode = null;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedNode = findClosestNode(e.getX(), e.getY());
				selectedNode.isSelected = true;
				addMouseMotionListener(ml);
			}

			private Node findClosestNode(int x, int y) {
				double minD = Double.MAX_VALUE;
				Node minN = null;

				for (Node n : nodes) {
					double dx = Math.abs(x - n.x);
					double dy = Math.abs(y - n.y);
					double d = Math.sqrt(dx * dx + dy * dy);

					if (d < minD) {
						minD = d;
						minN = n;
					}
				}

				return minN;
			}
		});
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int nbSteps = 0;

				while (true) {
					if (isVisible()) {
						updateLayoutArea();

						if (layoutArea.width > 0 && layoutArea.height > 0) {
							if (nbSteps == 0) {
								shuffle(new Random());
							}

							algo.step(JGraph.this, layoutArea);
							algo.center(JGraph.this, layoutArea);
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
		Graphics2D g2 = (Graphics2D) g;
		// g2.setRenderingHints(rh);

		for (EdgeCursor e : edges()) {
			int t = getArcType(e.src, e.dest);

			if (t != 0) {
				g2.drawLine((int) e.src.x, (int) e.src.y, (int) e.dest.x, (int) e.dest.y);
			}
		}

		g2.setStroke(stroke);

		for (Node u : nodes) {
			int usize = getSize(u);

			if (usize == 0) {
				// do nothing, node is invisible
			}
			else if (usize == 1) {
				// draw a point
				g.drawLine(u.x, u.y, u.x, u.y);
			}
			else {
				if ( ! u.icon_rescaled) {
					u.icon = getIcon(u);

					if (u.icon != null) {
						u.icon = new ImageIcon(
								u.icon.getImage().getScaledInstance( - 1, usize, 0));
					}

					u.icon_rescaled = true;
				}

				if (u.icon != null) {
					g.drawImage(u.icon.getImage(), (int) u.x - u.icon.getIconWidth() / 2,
							(int) u.y - u.icon.getIconHeight() / 2, this);
				}

				String text = getText(u);

				if (text == null) {
					Color fillColor = getFillColor(u);

					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillOval(u.x - usize / 2, u.y - usize / 2, usize, usize);
					}

					Color lineColor = getLineColor(u);

					if (lineColor != null) {
						g.setColor(getLineColor(u));
						g.drawOval(u.x - usize / 2, u.y - usize / 2, usize, usize);
					}
				}
				else {
					text = text.trim();

					if (text.length() > 0) {
						GlyphVector vec = GlyphVectorCache.get(text);

						if (vec == null) {
							FontRenderContext frc = g2.getFontRenderContext();
							Font font = getFont().deriveFont((float) usize);
							vec = font.createGlyphVector(frc, text);
							GlyphVectorCache.put(text, vec);
						}

						g.setColor(getFillColor(u));
						int gap = 6;
						int textW = (int) vec.getVisualBounds().getWidth();
						int textH = (int) vec.getVisualBounds().getHeight();
						g2.fillRect(u.x - textW / 2 - gap, u.y - textH / 2 - gap,
								textW + 2 * gap, textH + 2 * gap);
						g.setColor(getLineColor(u));
						g2.drawRect(u.x - textW / 2 - gap, u.y - textH / 2 - gap,
								textW + 2 * gap, textH + 2 * gap);

						g2.drawGlyphVector(vec, u.x - textW / 2, u.y + textH / 2 + 2);
					}
				}

			}
		}
	}

	protected abstract Color getLineColor(Node u);

	protected abstract Color getFillColor(Node u);

	protected abstract int getArcType(Node src, Node dest);

	protected abstract int getSize(Node u);

	private static Map<String, GlyphVector> GlyphVectorCache = new HashMap<>();

	protected abstract String getText(Node u);

	protected abstract ImageIcon getIcon(Node u);

	public void shuffle(Random prng) {
		for (Node u : nodes) {
			u.x = prng.nextInt(layoutArea.width) + layoutArea.x;
			u.y = prng.nextInt(layoutArea.height) + layoutArea.y;
		}
	}

	public JComponent getControls() {
		JPanel p = new JPanel(new GridLayout(2, 1));
		p.add(new GraphControls(this));
		p.add(algo.getControls());
		return p;
	}

	public JComponent getBundleComponent() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(this, BorderLayout.CENTER);
		p.add(getControls(), BorderLayout.SOUTH);
		return p;
	}

	public Node createNode(Object e) {
		return algo.createNode(e);
	}

	public void removedge(Node src, Node dest) {
		adj.get(dest).remove(dest);
	}

	protected Iterable<EdgeCursor> edges() {
		return () -> new Iterator<EdgeCursor>() {
			final EdgeCursor ec = new EdgeCursor();
			boolean hasNext;
			Iterator<Entry<Node, Set<Node>>> entryIterator = adj.entrySet().iterator();
			Iterator<Node> currentListIterator;
			Node nextSrc, nextDest;

			{
				if (entryIterator.hasNext()) {
					Entry<Node, Set<Node>> e = entryIterator.next();
					nextSrc = e.getKey();
					currentListIterator = e.getValue().iterator();
					findNext();
				}
			}

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			private void findNext() {
				if (currentListIterator.hasNext()) {
					nextDest = currentListIterator.next();
					hasNext = true;
				}
				else {
					if (entryIterator.hasNext()) {
						Entry<Node, Set<Node>> currentEntry = entryIterator.next();
						nextSrc = currentEntry.getKey();
						currentListIterator = currentEntry.getValue().iterator();
						findNext();
					}
					else {
						hasNext = false;
					}
				}
			}

			@Override
			public EdgeCursor next() {
				ec.src = nextSrc;
				ec.dest = nextDest;
				findNext();
				return ec;
			}
		};
	}

	public void addNodes(Iterable nodes) {
		for (Object o : nodes) {
			addNode(o);
		}

	}

	public void addNodes(Object... values) {
		for (Object o : values) {
			addNode(o);
		}

	}

	public Node addNode(Object o) {
		Node n = algo.createNode(o);
		nodes.add(n);
		return n;
	}

	public void addEdge(Object src, Object dest) {
		addEdge(ensureExists(src), ensureExists(dest));
	}

	private Node ensureExists(Object e) {
		Node n = lookupNode(e);

		if (n == null) {
			return addNode(e);
		}
		else {
			return null;
		}
	}

	private void addEdge(Node src, Node dest) {
		Set<Node> adjList = adj.get(src);

		if (adjList == null) {
			adj.put(src, adjList = new HashSet<>());
		}

		adjList.add(dest);
	}
}
