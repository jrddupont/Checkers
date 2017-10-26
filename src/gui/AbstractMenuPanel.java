package gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractMenuPanel extends JPanel{
	public static final Dimension size = new Dimension(300, 500);
	public static final int padding = 20;
	public AbstractMenuPanel() {
		setPreferredSize(size);
		setBorder( BorderFactory.createEmptyBorder(padding, padding, padding, padding) );
	}
}
