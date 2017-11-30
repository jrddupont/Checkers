package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.GameState;
import util.Netwrk;
import util.PlayerDisconnectException;
import client.ClientPlayer;
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
					serverTextField.setText(serversList.getSelectedValue().toString().split(" ")[1]);
				}
			}
		});
		
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new MenuPanel());
			}
		});
		
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    JPanel panel = new JPanel(new BorderLayout(5, 5));

			    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
			    label.add(new JLabel("Username", SwingConstants.RIGHT));
			    label.add(new JLabel("Password", SwingConstants.RIGHT));
			    panel.add(label, BorderLayout.WEST);

			    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
			    JTextField username = new JTextField();
			    controls.add(username);
			    JPasswordField password = new JPasswordField();
			    controls.add(password);
			    panel.add(controls, BorderLayout.CENTER);

			    int choice = JOptionPane.showConfirmDialog(null, panel, "login", JOptionPane.OK_CANCEL_OPTION);
			    if(choice == JOptionPane.CANCEL_OPTION){
			    	return;
			    }
			    
			    String usernameText = username.getText().trim();
			    String passwordText = new String(password.getPassword()).trim();
			    int gameID = Integer.parseInt(serverTextField.getText().trim());
			    
			    
			    System.out.println(usernameText + " " + passwordText);
			    
			    ClientPlayer player = new ClientPlayer(usernameText, passwordText, gameID);
			    
			    
			    try
				{
					player.processPacket(player.getMail()); // get put into game
					System.out.printf("got my packet 1\n");
				} catch (PlayerDisconnectException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			    player.name = usernameText;
				
			    GamePanel gamePanel = new GamePanel(player.gameState, player);
			    
			    System.out.printf("got my gamePanel\n");
			    
			    player.gameBoardUI = gamePanel.gameBoard;
			    
			    Driver.switchMenu(gamePanel);
			    
			    System.out.printf("switched to my gamePanel... ");
			    
			    System.out.printf("going\n");
			    
			    Thread thread = new Thread(player);
			    thread.start();
			}
		});
		
	}
	
	private void initServerList(){
//		listModel.addElement("Example");
//		listModel.addElement("servers");
//		listModel.addElement("to");
//		listModel.addElement("connect");
//		listModel.addElement("to.");
		
		updateServerList(getGamesListFromServer(Netwrk.IP_ADDRESS));
	}
	
	public void updateServerList(String[] servers){
		listModel.clear();
		for(String server : servers){
			listModel.addElement(server);
		}
	}
	
	// XXX will be moved to another helper class at a later date
	// this turned out much uglier than intended
	
	// XXX feel free to reformat the returned string.
	// if more/different information is wanted it can be changed
	
	// XXX client player will be refactored to be easy to call and join a game.
	
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
			
			data.put(Netwrk.OPCODE, Netwrk.SERVER_LIST_REQUEST);
			
			
			PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
			printer.println(data.toJSONString());
			
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String done = "false";
			
			while( !done.equals("null") ) {
				data = (JSONObject) ((Object) parser.parse(buffReader.readLine()));
				
				// stupid check
				if( ((Long)data.get(Netwrk.OPCODE)).byteValue() == Netwrk.SERVER_LIST_REQUEST) {
					
					if( !(done = data.get(Netwrk.PLAYER_ONE_UNAME).toString()).equals("null")) {
						servers.add( 
							String.format("GameID: %d Opponent: %s", 
								((Long)data.get(Netwrk.GAME_ID)).intValue(), data.get(Netwrk.PLAYER_ONE_UNAME).toString()) );
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
