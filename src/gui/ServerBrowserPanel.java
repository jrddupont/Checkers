package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ServerBrowserPanel extends AbstractMenuPanel{
	
	JTextField serverTextField = new JTextField("");
	JButton connectButton = new JButton("Connect");
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	public ServerBrowserPanel(){
		super();
		serverTextField.setColumns(100);
		serverTextField.setMaximumSize( new Dimension(serverTextField.getPreferredSize().width, connectButton.getPreferredSize().height)  );
		
		JList<String> serversList = new JList<String>(listModel);
		serversList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		serversList.setLayoutOrientation(JList.VERTICAL);
		serversList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(serversList);
		listScroller.setPreferredSize(new Dimension(size.width, size.width));
		
		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.X_AXIS));	// Set the panel to a vertical layout
		inputContainer.add(serverTextField);
		inputContainer.add(connectButton);

		JLabel infoLabel = new JLabel("Please select a server or type in the name: ");
		infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		add(infoLabel);
		add(Box.createRigidArea(new Dimension(0, padding)));
		add(inputContainer);
		add(Box.createRigidArea(new Dimension(0, padding)));
		add(listScroller);
		
		initServerList();

		serversList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					serverTextField.setText(serversList.getSelectedValue().toString());
				}
			}
		});

		
	}
	
	private void initServerList(){
		listModel.addElement("Example");
		listModel.addElement("servers");
		listModel.addElement("to");
		listModel.addElement("connect");
		listModel.addElement("to.");
	}
	public void updateServerList(String[] servers){
		
	}
}
