package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.Settings;
import client.Driver;

/**
 * Panel that contains all the options regarding theme selection and editing
 *
 */
@SuppressWarnings("serial")
public class SettingsPanel extends AbstractMenuPanel{
	
	private static final String themePath = "config/themes/themes.json";
	
	HashMap<String, Theme> themes;
	JComboBox<String> themeChoiceSelector;
	
	JTextField themeNameField = new JTextField("");
	
	JButton saveThemeButton = new JButton("Save theme");
	JButton removeThemeButton = new JButton("Remove theme");
	
	SmallColorChooser scc = new SmallColorChooser();
	
	JButton changeLightColorButton =	new JButton("Change light square color");
	JButton changeDarkColorButton =		new JButton("Change dark square color");
	JButton changeP1ColorButton =		new JButton("Change player 1 color");
	JButton changeP2ColorButton =		new JButton("Change player 2 color");
	JButton changeKingColorButton =		new JButton("Change king color");
	
	JButton mainMenuButton =			new JButton("Main menu");
	
	ThemePreviewPanel themePreviewPanel;
	
	
	/**
	 * Creates a new settings panel and adds lots and lots of buttons and options to it. I will comment the action listeners because they actually
	 * contain code.
	 */
	public SettingsPanel(){
		super();
		padding /= 2;
		
		
		themeChoiceSelector = new JComboBox<String>();
		themes = new HashMap<String, Theme>();
		loadThemes();
		refreshComboBox();
		
		themePreviewPanel = new ThemePreviewPanel(Settings.currentTheme);
		
		JButton[] buttons = {saveThemeButton, removeThemeButton, changeLightColorButton, changeDarkColorButton, changeP1ColorButton, changeP2ColorButton, changeKingColorButton, mainMenuButton};
		
		for(JButton button : buttons){
			button.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
			button.setMaximumSize(new Dimension(size.width, saveThemeButton.getPreferredSize().height));
		}
		
		themeChoiceSelector.setAlignmentX(Component.CENTER_ALIGNMENT);	// Center align the labels
		themeChoiceSelector.setMaximumSize(new Dimension(size.width, themeChoiceSelector.getPreferredSize().height));
		
		themeNameField.setColumns(100);
		themeNameField.setMaximumSize( new Dimension(themeNameField.getPreferredSize().width, removeThemeButton.getPreferredSize().height)  ); 
		
	    add(themeChoiceSelector);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(changeLightColorButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(changeDarkColorButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(changeP1ColorButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(changeP2ColorButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(changeKingColorButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(themePreviewPanel);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    
	    add( Box.createVerticalGlue() );	// Add the labels to the panel and align them horizontally (The glue objects take up space)
	    
	    add(themeNameField);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(saveThemeButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(removeThemeButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(mainMenuButton);
	    
	    // Dropdown menu to select from known themes. It will set the current theme to whatever is chosen.
	    themeChoiceSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> comboBox = (JComboBox<String>)e.getSource();
				if(comboBox.getItemCount() < 1){
					return;
				}
				Settings.currentTheme = themes.get(comboBox.getSelectedItem());
				themePreviewPanel.currentTheme = Settings.currentTheme;
				themeNameField.setText(Settings.currentTheme.name);
			}
		});
	    
	    // Puts all the theme data into a JSON object and puts that in a map of all themes
	    // This overrides themes with the same name, so be careful
	    saveThemeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Theme newTheme = new Theme(Settings.currentTheme);
				newTheme.name = themeNameField.getText();
				themes.put(themeNameField.getText(), newTheme);
				saveThemes();
				refreshComboBox();
			}
		});
	    
	    // Removes the currently selected theme from the map of themes
	    removeThemeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				themes.remove(themeNameField.getText());
				saveThemes();
				refreshComboBox();
			}
		});
	    
	    // Enables and disables the save button based on if there is any text in the theme name text input
	    themeNameField.getDocument().addDocumentListener(new DocumentListener() {
	    	@Override
	    	public void changedUpdate(DocumentEvent e) {}
	    	@Override
	    	public void removeUpdate(DocumentEvent e) {
	    		saveThemeButton.setEnabled(themeNameField.getText().length() > 0);
	    	}
	    	@Override
	    	public void insertUpdate(DocumentEvent e) {
	    		saveThemeButton.setEnabled(themeNameField.getText().length() > 0);
	    	}
	    });
	    
	    // The next 5 listeners all do the same thing, they just open a color selector for the relevant color option
	    changeLightColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.currentTheme.lightBoardColor = scc.getColor();
						repaint();
					}
				};
				scc.setColor(Settings.currentTheme.lightBoardColor);
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, scc, returnListener,null);
				jdi.setVisible(true);
			}
		});
	    
	    changeDarkColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.currentTheme.darkBoardColor = scc.getColor();
						repaint();
					}
				};
				scc.setColor(Settings.currentTheme.darkBoardColor);
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, scc, returnListener,null);
				jdi.setVisible(true);
			}
		});
	    
	    changeP1ColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.currentTheme.player1Color = scc.getColor();
						repaint();
					}
				};
				scc.setColor(Settings.currentTheme.player1Color);
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, scc, returnListener,null);
				jdi.setVisible(true);
			}
		});
	    
	    changeP2ColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.currentTheme.player2Color = scc.getColor();
						repaint();
					}
				};
				scc.setColor(Settings.currentTheme.player2Color);
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, scc, returnListener,null);
				jdi.setVisible(true);
			}
		});
	    
	    changeKingColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Settings.currentTheme.kingColor = scc.getColor();
						repaint();
					}
				};
				scc.setColor(Settings.currentTheme.kingColor);
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, scc, returnListener,null);
				jdi.setVisible(true);
			}
		});
	    
	    // Switch the panel to the menu panel
	    mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Driver.switchMenu(new MenuPanel());
			}
		});
	}
	
	
	/**
	 * Removes all items from the dropdown menu and replaces them with all the keys in the theme map
	 */
	private void refreshComboBox(){
		
		if(themes.size() == 0){
			themes.put("Default", new Theme());
		}
		
		String[] choices = new String[themes.size()];
		int i = 0;
		for(String key : themes.keySet()){
			choices[i] = key;
			i++;
		}
		
		themeChoiceSelector.removeAllItems();
	    for(String s : choices){
	    	themeChoiceSelector.addItem(s);
	    }
	    
	    themeChoiceSelector.setSelectedIndex(0);
	    themeNameField.setText(choices[0]);
	    Settings.currentTheme = themes.get(choices[0]);
	}
	
	/**
	 * Loads the JSON file containing theme data and replaces whatever is in themes with the new data
	 */
	private void loadThemes(){
		themes.clear();
		JSONParser parser = new JSONParser();
		try {
			JSONArray jsonArray = (JSONArray) ((JSONObject) parser.parse(new FileReader(themePath))).get("themes");
			if(jsonArray == null){
				return;
			}
			for(Object object : jsonArray){
				Theme currentTheme = new Theme((JSONObject) object);
				themes.put(currentTheme.name, currentTheme);
			}
		} catch (IOException | ParseException e) { e.printStackTrace(); }
	}
	
	/**
	 * Saves the current theme map to a text file in JSON
	 */
	@SuppressWarnings({ "unchecked" })
	private void saveThemes() {
		JSONObject output = new JSONObject();
		JSONArray themeArray = new JSONArray();
	   
		for(String key : themes.keySet()){
			if(themes.get(key).name == null){
				continue;
			}
			themeArray.add( themes.get(key).getJSONObject() );
		}
	    
	    output.put("themes", themeArray);
	    
	    PrintWriter out = null;
		try {
			out = new PrintWriter(themePath);
		} catch (FileNotFoundException e) {
			return;
		}
	    out.print(output.toJSONString());
	    out.flush();
	    out.close();
	}
	
	
	/**
	 * @author yb5243yv
	 *
	 */
	private class SmallColorChooser extends JColorChooser{
		public SmallColorChooser(){
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			this.setAlignmentX(LEFT_ALIGNMENT);
			setAlignmentX(LEFT_ALIGNMENT);
			
			setPreviewPanel(new JPanel());
			AbstractColorChooserPanel[] panels = getChooserPanels();
			for (AbstractColorChooserPanel accp : panels) {
				if(!accp.getDisplayName().equals("HSV")) {
					removeChooserPanel(accp);
				} 
			}
			JComponent current = (JComponent) getComponents()[0];
			while( !current.getClass().toString().equals( "class javax.swing.colorchooser.ColorPanel" ) ){
				
				current = (JComponent) current.getComponents()[0]; 
			}
			current.removeAll();
		}
	}
	
	private class ThemePreviewPanel extends JComponent{
		public Theme currentTheme;
		public ThemePreviewPanel(Theme defaultTheme){
			currentTheme = defaultTheme;
			setPreferredSize(new Dimension(size.width, size.width));
		}
		@Override
		public void paint(Graphics g){
			int height = Math.min(getHeight(), getWidth() / 2);
			
			g.setColor(currentTheme.lightBoardColor);
			g.fillRect(0, 0, height * 2, height);
			
			g.setColor(currentTheme.darkBoardColor);
			g.fillRect(0, 0, height/2, height/2);
			g.fillRect(height/2, height/2, height/2, height/2);
			g.fillRect(height, 0, height/2, height/2);
			g.fillRect(height + height/2, height/2, height/2, height/2);
			
			g.setColor(currentTheme.player1Color);
			g.fillOval(0, 0, height/2, height/2);
			
			g.setColor(currentTheme.player2Color);
			g.fillOval(height + height/2, height/2, height/2, height/2);
			
			g.setColor(currentTheme.kingColor);
			g.fillOval( height/8, height/8, height/4, height/4);
		}
	}
}
