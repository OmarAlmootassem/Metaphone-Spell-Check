package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import levenshteinDistance.LevenshteinDistance;
import metaphone.Metaphone;

/*
 * This class receives a word from the WordCapture class and compares it to
 * the words in Dictionary.txt. If the word is in the file, that means it is
 * spelled correctly and there is no need to call the Metaphone class to try 
 * and find a possible correct word. If the word is not in the Dictionary.txt
 * file, then it is most likely spelled incorrectly and the Metaphone class
 * will be called to find corrections.
 *                         
 * Created on: March 22, 2015
 * Last Edited on: March 26, 2015
 *
 * @authors Omar Almootassem and Talha Zia
 *
 */
public class CheckSpelling {
	
	//String where the words from Dictionary.txt will be saved to compare them
	//with the input word
	private static String line = "NULL";
	
	/**
	 * Checks if the word is in the dictionary. If it is not, then it calls the 
	 * metaphone class to look for possible corrections from the dictionary.
	 * @param word - input word
	 */
	public CheckSpelling(String word){
		//if the word does not have a space " " somewhere in the middle then check if
		//it is spelled correctly, otherwise do not check to save time
		if (!word.contains(" ")){
			//Open the file
			try{
				BufferedReader reader = new BufferedReader(new FileReader("src/Dictionary.txt"));
				line = reader.readLine();
				/*
				 * The do while loop reads through the entire text file line by line. Each line has
				 * one word on it. It saves the word in "line" variable and compares it with "word".
				 * If they are the same word or the file ends, the loop stops.
				 */
				do{
					//System.out.println(line + " " + word);	//DEBUG (prints out content of the Dictionary file)
				}while (((line = reader.readLine()) != null) && !word.equals(line));
				
				//close the file
				reader.close();
				
			//catch the IOException if file is not found
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
		//If word is found, it is spelled correctly
		if (word.equals(line)){
			WordSpell.suggestButton[0].setText("_");
			WordSpell.suggestButton[1].setText("_");
			WordSpell.suggestButton[2].setText("_");
			WordSpell.suggestButton[3].setText("_");
			WordSpell.suggestButton[4].setText("_");
			
			System.out.println ("found");	//DEBUG
		}
		//If word has a space in it, it can not be compared to words in the txt file
		else if(word.contains(" ")){
			System.out.println("Can't be compared");	//DEBUG
		}
		//If word is not found, call metaphone to find possible spelling corrections
		else{
			new LevenshteinDistance(word);
			new Metaphone(word);
			
			System.out.println("Not found");	//DEBUG
		}
	}
}
