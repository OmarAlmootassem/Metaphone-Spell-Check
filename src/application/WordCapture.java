package application;

import javax.swing.text.BadLocationException;

/*
 * This class captures the selected word from the JTextArea in the window 
 * and checks if it is correctly spelled before sending it to the Metaphone
 * class to look for possible correct spellings.
 * 
 * To capture the word, it first gets the location of the cursor, then checks
 * to the left of the cursor till it hits a space " " and then to the right
 * till it hits another space.
 *                         
 * Created on: March 21, 2015 
 * Last Edited on: March 26, 2015
 *
 * @authors Omar Almootassem and Talha Zia
 *
 */
public class WordCapture {
	
	//integer to save the cursor(caret) position on the JTextArea
	private int caretPosition;
	
	//integers for the location of the start and the end of the word
	private static int wordStart, wordEnd;
	
	//the final word
	private String finalWord;
	
	/**
	 * The class that captures the word
	 * @throws BadLocationException
	 */
	public WordCapture() throws BadLocationException{
		//get and save the caret position
		caretPosition = WordSpell.textEntry.getCaretPosition();
		
		int i = 0;	//counter integer for the 2 do-while loops
		
		//Check to the left side of the caret until there is a space and save the location of 
		//the space in wordStart
		do {
			wordStart = caretPosition - i - 1;
			//System.out.println("checking left" + wordStart);	//DEBUG
			i++;
		} while (!WordSpell.textEntry.getText(caretPosition - i, 1).equals(" ") && wordStart >= 0);
		
		i = 0; //reset value of i
		
		//Check to the right side of the caret until there is a space and save the location of 
		//the space in wordEnd
		do {
			wordEnd = caretPosition + i;
			//System.out.println("checking right" + wordEnd);	//DEBUG
			i++;
		} while (!WordSpell.textEntry.getText(caretPosition + i, 1).equals(" ") && wordEnd >= 0);
		
		//remove extra spaces and save the word in finalWord
		finalWord = WordSpell.textEntry.getText(wordStart + 1, Math.abs(wordEnd - wordStart)).trim().toUpperCase();
		
		//Check if the entered word is a word in the dictionary
		new CheckSpelling(finalWord);
		
		//System.out.println(finalWord);	//DEBUG
	}
	
	/**
	 * A method called by the action listeners on the suggestion buttons that
	 * replaces the word
	 * @param word
	 */
	public static void replaceWord(String word){
		//Replaces the word in the textEntry 
		WordSpell.textEntry.replaceRange(word.toLowerCase(), wordStart + 1, wordEnd + 1);
		//Set the position of the Caret in the middle of the word to re-check spelling
		WordSpell.textEntry.setCaretPosition(wordStart + 2);
	}

}
