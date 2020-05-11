package egviz;

import javax.swing.JComponent;

public abstract class ForceBasedAlgo extends Layout
{
	protected double attractionFactor = 0.9;
	protected double repulsionFactor = 1.1;

	@Override
	public JComponent getControls()
	{
		return new ForceBasedControls(this);
	}
}
