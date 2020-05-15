package fr.cnrs.swinggraph;

import javax.swing.JComponent;

public abstract class ForceBasedAlgo<N> extends Layout<N>
{
	protected double attractionFactor = 0.9;
	protected double repulsionFactor = 1.1;

	@Override
	public JComponent getControls()
	{
		return new ForceBasedControls(this);
	}
}
