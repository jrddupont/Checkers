package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.Driver;

@SuppressWarnings("serial")
public class ServerBrowserPanel extends AbstractMenuPanel{
	
	JTextField serverTextField = new JTextField("");
	JButton connectButton = new JButton("Connect");
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	JButton mainMenuButton =			new JButton("Main menu");
	
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
		add(Box.createRigidArea(new Dimension(0, padding)));
		add(mainMenuButton);
		
		initServerList();

		serversList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					serverTextField.setText(serversList.getSelectedValue().toString());
				}
			}
		});
		
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new MenuPanel());
			}
		});
		
	}
	
	private void initServerList(){
//		listModel.addElement("Example");
//		listModel.addElement("servers");
//		listModel.addElement("to");
//		listModel.addElement("connect");
//		listModel.addElement("to.");
		
		updateServerList(getGamesListFromServer("127.0.0.1"));
	}
	
	public void updateServerList(String[] servers){
		listModel.clear();
		for(String server : servers){
			listModel.addElement(server);
		}
	}
	
	// XXX will be moved to another helper class at a later date
	// this turned out much uglier than intended
	final protected byte SERVER_LIST_REQUEST = 7;
	
	/*
	 * Talks to server, receives list of games that are waiting
	 * for a second player.
	 * Returns these in an a array of displayable strings.
	 */
	@SuppressWarnings("unchecked")
	private String[] getGamesListFromServer(String serverIP) {
		
		ArrayList<String> servers = new ArrayList();
		
		try{
			Socket socket = new Socket(serverIP, 12321);
			
			JSONObject data = new JSONObject();
			JSONParser parser = new JSONParser();
			
			data.put("Opcode", SERVER_LIST_REQUEST);
			
			
			PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
			printer.println(data.toJSONString());
			
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String done = "false";
			
			while( done.equals("false") ) {
				data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
				
				System.out.printf("in while\n");
				
				// stupid check
				if( ((Long)data.get("Opcode")).byteValue() == SERVER_LIST_REQUEST) {
					
					if( (done = data.get("Done").toString()).equals("false")) {
						servers.add( 
							String.format("GameID: %d Opponent: %s", 
								((Long)data.get("GameID")).intValue(), data.get("Player1").toString()) );
						
						System.out.printf("here 2\n");
					}	
				}
			}			
			
		} catch (UnknownHostException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return servers.toArray(new String[servers.size()]);
	}
}
