package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

/*
 * This program is a word spell application. It uses the Metaphone 
 * algorithm to provide possible corrections to misspelled words.
 * 
 * This application has a graphical user interface built using
 * "javax.swing.*". 
 * The UI is separated into 2 main sections:
 * 1. Typing section: In this section, the user enters their 
 *                    word(s) or sentence(s). The other section comes
 *                    to play when the cursor is on one of the 
 *                    misspelled words.
 *                    
 * 2. Suggestions section: When the cursor is on a misspelled word,
 *                         this section will display a list of
 *                         suggestions that can be clicked and used
 *                         to replace the incorrect word.
 *                       
 * NOTE: WORDS MUST BE SURROUNDED BY A SPACE
 * Created on: March 20, 2015 
 * Last Edited on: March 26, 2015
 *
 * @authors Omar Almootassem and Talha Zia
 *
 */
public class WordSpell {
	
	private JFrame frame;
	private JPanel mainPanel;
	private static JPanel suggestPanel;
	private JPanel titlePanel;
	private JPanel credentialsPanel;	
	private JLabel title, credentials;
	
	//declared public to allow the Metaphone class to read and edit
	//the objects
	public static JTextArea textEntry = new JTextArea("  ");
	
	//number of suggestions displayed is SUGGESTION_NUMS
	private static final int SUGGESTION_NUMS = 5;
	//array of JButtons that is limited to SUGGESTION_NUMS objects
	public static JButton suggestButton[] = new JButton[SUGGESTION_NUMS];
	
	/**
	 * This class builds the UI for the Word Spell program
	 */
	public WordSpell(){
		//create the Frame and give title
		frame = new JFrame("Word Spell");
		
		//create the JPanels
		mainPanel = new JPanel();
		suggestPanel = new JPanel();
		titlePanel = new JPanel();
		credentialsPanel = new JPanel();
		
		//initialize JLabels for title and credentials
		title = new JLabel ("Word Spell");
		title.setFont(new Font ("Serif", Font.BOLD, 24));
		textEntry.setToolTipText("Sentence MUST start and end with a space");
		textEntry.setCaretPosition(1);
		credentials = new JLabel("Created by Omar Almootassem and Talha Zia");
		
		//initialize suggestButton[]
		suggestButton[0] = new JButton("_");
		suggestButton[1] = new JButton("_");
		suggestButton[2] = new JButton("_");
		suggestButton[3] = new JButton("_");
		suggestButton[4] = new JButton("_");
		
		//add Action Listener to all buttons to replace the word in the JTextArea
		suggestButton[0].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WordCapture.replaceWord(suggestButton[0].getText());
			}
		});
		suggestButton[1].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WordCapture.replaceWord(suggestButton[1].getText());
			}
		});
		suggestButton[2].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WordCapture.replaceWord(suggestButton[2].getText());
			}
		});
		suggestButton[3].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WordCapture.replaceWord(suggestButton[3].getText());
			}
		});
		suggestButton[4].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WordCapture.replaceWord(suggestButton[4].getText());
			}
		});
		
		//add CaretListener which detects changes to the caret's location
		//and calls the WordCapture class
		textEntry.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent arg0) {
				try{
					new WordCapture();
				}catch(BadLocationException e){
					e.printStackTrace();
				}
			}	
		});
		
		//add objects to the panels
		titlePanel.add(title);
		credentialsPanel.add(credentials);
		
		//set GridLayout to suggestPanel
		suggestPanel.setLayout(new GridLayout(5,1));
		//add suggestButton(s) to the suggestPanel
		for (int i = 0; i < suggestButton.length; i++){
				suggestPanel.add(suggestButton[i]);
		}
		
		//set the layout of the mainPanel to BorderLayout
		mainPanel.setLayout(new BorderLayout());
		//add objects to the border layout
		mainPanel.add(titlePanel, BorderLayout.PAGE_START);
		mainPanel.add(textEntry, BorderLayout.CENTER);
		mainPanel.add(suggestPanel, BorderLayout.LINE_END);
		mainPanel.add(credentialsPanel, BorderLayout.PAGE_END);
		
		
		frame.add(mainPanel);
		//set the size of the window to be 1/4 of the screen
		frame.setSize(new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2, 
									java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2));
		//set window location to center
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void updateDisplay(){
		//add suggestButton(s) to the suggestPanel
				for (int i = 0; i < suggestButton.length; i++){
					//if the JButton's text is not empty, display it
					if (!suggestButton[i].getText().equals("_")){
						suggestPanel.add(suggestButton[i]);
					}
				}
	}
		
	/**
	 * main method to launch the program
	 * @param args
	 */
	public static void main (String[] args){
		new WordSpell();	//launch program
	}

}
