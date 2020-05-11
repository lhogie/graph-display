package egviz;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ForceBasedControls extends JPanel
{
	JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
	JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);

	public ForceBasedControls(ForceBasedAlgo algo)
	{

		add(new JLabel("Attract"));
		add(attractionSlider);
		add(new JLabel("Repulse"));
		add(repulsionSlider);

		attractionSlider.setValue((int) ((1 - algo.attractionFactor) * 100));
		repulsionSlider.setValue((int) ((algo.repulsionFactor - 1) * 100));

		attractionSlider.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				algo.attractionFactor = 100d - attractionSlider.getValue() / 100;
			}
		});

		repulsionSlider.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				algo.repulsionFactor = 100d + repulsionSlider.getValue() / 100;
			}
		});
	}
}
