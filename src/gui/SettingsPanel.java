package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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

@SuppressWarnings("serial")
public class SettingsPanel extends AbstractMenuPanel{
	
	private static final String themePath = "config/themes/themes.json";
	
	HashMap<String, Theme> themes;
	JComboBox<String> themeChoiceSelector;
	
	JTextField themeNameField = new JTextField("");
	
	JButton saveThemeButton = new JButton("Save theme");
	JButton removeThemeButton = new JButton("Remove theme");
	
	JButton changeLightColorButton =	new JButton("Change light square color");
	JButton changeDarkColorButton =		new JButton("Change dark square color");
	JButton changeP1ColorButton =		new JButton("Change player 1 color");
	JButton changeP2ColorButton =		new JButton("Change player 2 color");
	JButton changeKingColorButton =		new JButton("Change king color");
	
	Settings settings;
	
	public SettingsPanel(Settings settings){
		super();
		padding /= 2;
		
		this.settings = settings;
		
		refreshComboBox();
		
		JButton[] buttons = {saveThemeButton, removeThemeButton, changeLightColorButton, changeDarkColorButton, changeP1ColorButton, changeP2ColorButton, changeKingColorButton};
		
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
	    
	    add( Box.createVerticalGlue() );	// Add the labels to the panel and align them horizontally (The glue objects take up space)
	    
	    add(themeNameField);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(saveThemeButton);
	    add(Box.createRigidArea(new Dimension(0, padding)));
	    add(removeThemeButton);
	    
	    themeChoiceSelector.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				settings.currentTheme = themes.get(cb.getSelectedItem());
				for(String s : themes.keySet()){
					System.out.println(s);
				}
				System.out.println(settings.currentTheme);
				themeNameField.setText(settings.currentTheme.name);
			}
		});
	    
	    saveThemeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	    
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

	    
	    changeLightColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener returnListener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				};
				JDialog jdi = JColorChooser.createDialog(null, "Choose a color", true, new SmallColorChooser(settings.currentTheme.lightBoardColor), returnListener,null);
				jdi.setVisible(true);
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	private void refreshComboBox(){
		themes = new HashMap<String, Theme>();
		loadThemes();
		if(themes.size() == 0){
			themes.put("Default", new Theme());
		}
		
		String[] choices = new String[themes.size()];
		int i = 0;
		for(String key : themes.keySet()){
			choices[i] = key;
			i++;
		}
	    
	    themeChoiceSelector = new JComboBox<String>(choices);
	    themeChoiceSelector.setSelectedIndex(0);
	    
	    themeNameField.setText("Default");
	}
	
	private void loadThemes(){
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveThemes() throws FileNotFoundException{
		JSONObject output = new JSONObject();
		JSONArray themeArray = new JSONArray();
	   
		for(String key : themes.keySet()){
			themeArray.add( themes.get(key) );
		}
	    
	    output.put("themes", themeArray);
	    
	    PrintWriter out = new PrintWriter(themePath);
	    out.print(output.toJSONString());
	    out.close();
	}
	
	
	
	public class SmallColorChooser extends JColorChooser{
		Color defaultColor;
		public SmallColorChooser(Color defaultColor){
			setColor(defaultColor);
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
}
