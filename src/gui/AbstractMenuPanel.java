package gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractMenuPanel extends JPanel{
	public static final Dimension size = new Dimension(400, 600);
	public int padding = 20;
	public AbstractMenuPanel() {
		setPreferredSize(size);
		setBorder( BorderFactory.createEmptyBorder(padding, padding, padding, padding) );
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	// Set the panel to a vertical layout
	}
}
