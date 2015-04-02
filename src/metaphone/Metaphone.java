package metaphone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import levenshteinDistance.LevenshteinDistance;
import application.WordSpell;

/*
 * This class contains the metaphone algorithm that will be used
 * to get the possible spelling corrections for the incorrect words.
 * The algorithm rules are:
 * 
 * 1. Drop duplicate adjacent letters, except for C. [done]
 * 2. If the word begins with 'KN', 'GN', 'PN', 'AE', 'WR', drop the first letter. [done]
 * 3. Drop 'B' if after 'M' at the end of the word. [done]
 * 4. 'C' transforms to 'X' if followed by 'IA' or 'H' (unless in latter case, it 
 *    is part of '-SCH-', in which case it transforms to 'K'). 'C' transforms to 'S' 
 *    if followed by 'I', 'E', or 'Y'. Otherwise, 'C' transforms to 'K'. [done]
 * 5. 'D' transforms to 'J' if followed by 'GE', 'GY', or 'GI'. Otherwise, 'D' transforms
 *    to 'T'. [done]
 * 6. Drop 'G' if followed by 'H' and 'H' is not at the end or before a vowel. [done]
 * 7. 'G' transforms to 'J' if before 'I', 'E', or 'Y', and it is not in 'GG'. Otherwise,
 *    'G' transforms to 'K'. [done]
 * 8. Drop 'H' if after vowel and not before a vowel. [done]
 * 9. 'CK' transforms to 'K'. [done]
 * 10. 'PH' transforms to 'F'. [done]
 * 11. 'Q' transforms to 'K'. [done]
 * 12. 'S' transforms to 'X' if followed by 'H', 'IO', or 'IA'. [done]
 * 13. 'T' transforms to 'X' if followed by 'IA' or 'IO'. Drop 'T' if followed by 'CH'. [done]
 * 14. 'V' transforms to 'F'. [done]
 * 15. 'WH' transforms to 'W' if at the beginning. Drop 'W' if not followed by a vowel. [done]
 * 16. 'X' transforms to 'S' if at the beginning. Otherwise, 'X' transforms to 'KS'. [done]
 * 17. Drop 'Y' if not followed by a vowel. [done]
 * 18. 'Z' transforms to 'S'. [done]
 * 19. Drop all vowels unless it is the beginning. [done]
 *                         
 * Created on: March 21, 2015
 * Last Edited on: March 26, 2015
 *
 * @authors Omar Almootassem and Talha Zia
 *
 */
public class Metaphone {
	
	//ArrayList to save the letters of the word into
	ArrayList<Character> letters = new ArrayList<Character>();
	
	//Array used to sort the remaining possible outcomes
	int [][] sorting = new int [LevenshteinDistance.levenWords.size()][LevenshteinDistance.levenWords.size()];
	
	//ArrayList that contains all the vowels
	ArrayList<Character> vowels = new ArrayList<Character>();
	
	//A counter that is used to set the text on the suggestion buttons in the UI
	private int suggestCounter = 0;
	
	//variable to save the input word
	private String input;
	
	public Metaphone(String word){
		input = word;
		//put the vowels into the vowels ArrayList
		vowels.add('A');
		vowels.add('E');
		vowels.add('U');
		vowels.add('I');
		vowels.add('O');
		//move the letters of the word to the Character ArrayList
		for (char c : word.toCharArray()){
			letters.add(c);
		}
		
		//System.out.println("char: "+ letters + letters.get(0));	//DEBUG (Check that word got saved into arraylist)
		
		/*------------------------------------------------------
		 * Step 1: Drop duplicate adjacent letters, except for C
		 *------------------------------------------------------*/
		if (letters.get(0).equals('C') && letters.get(1).equals('C')){
			letters.remove(0);
			
			//Compare the suggested word with the Levenshtein array
			compareSuggestion();
		}
		
		//Search through the array for consecutive duplicate letters other than 'C' and remove the first one
		for (int i = 0, x = 1; x < letters.size(); i++, x++){
			if (letters.get(i).equals(letters.get(x))){
				if (!letters.get(i).equals('C') && !letters.get(x).equals('C')){
					letters.remove(i);
				}
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*--------------------------------------------------------------------------------------
		 * Step 2: If the word begins with 'KN', 'GN', 'PN', 'AE', or 'WR', drop the first letter
		 *--------------------------------------------------------------------------------------*/
		if (letters.get(0).equals('K') && letters.get(1).equals('N')){
			letters.remove(0);
		}
		else if (letters.get(0).equals('G') && letters.get(1).equals('N')){
			letters.remove(0);
		}
		else if (letters.get(0).equals('P') && letters.get(1).equals('N')){
			letters.remove(0);	
		}
		else if (letters.get(0).equals('A') && letters.get(1).equals('E')){
			letters.remove(0);
		}
		else if (letters.get(0).equals('W') && letters.get(1).equals('R')){
			letters.remove(0);
		}
		//Compare the suggested word with the Levenshtein array
		compareSuggestion();
		
		/*-----------------------------------------------------
		 * Step 3: Drop 'B' if after 'M' at the end of the word
		 *-----------------------------------------------------*/
		if (letters.get(letters.size() - 1).equals('B') && letters.get(letters.size() - 2).equals('M')){
			letters.remove(letters.size() - 1);
		}
		//Compare suggestions
		compareSuggestion();
		
		/*------------------------------------------------------------------------------------
		 * Step 4: 'C' transforms to 'X' if followed by 'IA' or 'H' (unless in latter case, it 
		 * is part of '-SCH-', in which case it transforms to 'K'). 'C' transforms to 'S' 
		 * if followed by 'I', 'E', or 'Y'. Otherwise, 'C' transforms to 'K'
		 *------------------------------------------------------------------------------------*/
		for (int x = 0, y = 1, z = 2; z < letters.size(); x++, y++, z++){
			//Check for 'CIA'
			if ((letters.get(x).equals('C') && letters.get(y).equals('I') && letters.get(z).equals('A'))){
				letters.set(x, 'X');
			}
			//check for 'CH' and 'SCH'
			else if (letters.get(x).equals('C') && letters.get(y).equals('H')){
				if (x > 0 && letters.get(x - 1).equals('S')){
					letters.set(x, 'K');
				}else{
					letters.set(x, 'X');
				}
			}
			//check for 'CI', 'CE', and 'CY'
			else if (letters.get(x).equals('C') && (letters.get(y).equals('I') || letters.get(y).equals('E') || letters.get(y).equals('Y'))){
				letters.set(x, 'S');
			}
			//otherwise replace every C with a K
			else if (letters.get(x).equals('C')){
				letters.set(x, 'K');
			}
			
			//Compare suggestions
			compareSuggestion();
		}
		
		/*--------------------------------------------------------------------------------
		 * Step 5: 'D' tranforms to 'J' if followed by 'GE', 'GY', or 'GI'. Otherwise, 'D'
		 * transforms to 'T'
		 *--------------------------------------------------------------------------------*/
		for (int x = 0, y = 1, z = 2; z < letters.size(); x++, y++, z++){
			//check for 'DG' and then 'E', 'Y', or 'I'
			if (letters.get(x).equals('D') && letters.get(y).equals('G')){
				if (letters.get(z).equals('E') || letters.get(z).equals('Y') || letters.get(z).equals('I')){
					letters.set(x, 'J');
				}
			}
			//if there is only a 'D', replace with 'T'
			else if (letters.get(x).equals('D')){
				letters.set(x, 'T');
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*--------------------------------------------------------------------------------
		 * Step 6: Drop 'G' if followed by 'H' and 'H' is not at the end or before a vowel
		 *--------------------------------------------------------------------------------*/
		for (int x = 0, y = 1; y < letters.size(); x++, y++){
			//check that the 'H' is not at the end and not 'G' is not followed by a vowel
			if ((y != letters.size() - 1) && !vowels.contains(letters.get(y + 1))){
				if (letters.get(x).equals('G') && letters.get(y).equals('H')){
					letters.remove(x);
				}
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*------------------------------------------------------------------------------
		 * Step 7: 'G' transforms to 'J' if before 'I', 'E', or 'Y', and is not in 'GG'.
		 * Otherwise, 'G' transforms to 'K'
		 *------------------------------------------------------------------------------*/
		for (int x = 0, y = 1; y < letters.size(); x++, y++){
			//Check for 'GI', 'GE', or 'GY'
			if (letters.get(x).equals('G') && (letters.get(y).equals('I') || letters.get(y).equals('E') || letters.get(y).equals('Y'))){
				letters.set(x, 'J');
			}
			//check for any 'G' that is not followed by another 'G'
			else if (letters.get(x).equals('G') && !letters.get(y).equals('G')){
				letters.set(x, 'K');
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*--------------------------------------------------
		 * Step 8: Drop 'H' after vowel and not before vowel
		 *--------------------------------------------------*/
		for (int i = 1; i < letters.size() - 1; i++){
			//check for 'H' and vowels around it
			if (letters.get(i).equals('H') && vowels.contains(letters.get(i - 1)) && !vowels.contains(letters.get(i + 1))){
				letters.remove(i);
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*-------------------------------
		 * Step 9: 'CK' transforms to 'K'
		 *-------------------------------*/
		for (int x = 0, y = 1; y < letters.size(); x++, y++){
			if (letters.get(x).equals('C') && letters.get(y).equals('K')){
				letters.remove(x);
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*--------------------------------
		 * Step 10: 'PH' transforms to 'F'
		 *--------------------------------*/
		for (int x = 0, y = 1; y < letters.size(); x++, y++){
			if (letters.get(x).equals('P') && letters.get(y).equals('H')){
				letters.remove(x);
				letters.set(y, 'F');
			}
			//Compare suggestions
			compareSuggestion();
		}
		
		/*-------------------------------
		 * Step 11: 'Q' transforms to 'K'
		 *-------------------------------*/
		for (int i = 0; i < letters.size(); i++){
			if (letters.get(i).equals('Q')){
				letters.set(i, 'K');
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*----------------------------------------------------------------
		 * Step 12: 'S' transforms to 'X' if followed by 'H', 'IO' or 'IA'
		 *----------------------------------------------------------------*/
		for (int x = 0, y = 1, z = 2; z < letters.size(); x++, y++, z++){
			//check for 'S'
			if (letters.get(x).equals('S')){
				//Check for an 'H' or an 'I' after the 'S'
				if (letters.get(y).equals('H') || letters.get(y).equals('I')){
					//check for 'O' or 'A' after the 'I'
					if (letters.get(z).equals('O') || letters.get(z).equals('A')){
						letters.set(x, 'X');
					}
					letters.set(x, 'X');
				}
				letters.set(x, 'X');
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*---------------------------------------------------------------------
		 * Step 13: 'T' transforms to 'X' if followed by 'IA' or 'IO'. Drop 'T' 
		 * if followed by 'CH'.
		 *---------------------------------------------------------------------*/
		for (int x = 0, y = 1, z = 2; z < letters.size(); x++, y++, z++){
			//Check for 'T'
			if (letters.get(x).equals('T')){
				if (letters.get(y).equals('I') && (letters.get(z).equals('A') || letters.get(z).equals('O'))){
					letters.set(x, 'X');
				}
				else if(letters.get(y).equals('C') && letters.get(z).equals('H')){
					letters.remove(x);
				}
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*-------------------------------
		 * Step 14: 'V' transforms to 'F'
		 *-------------------------------*/
		for (int i = 0; i < letters.size(); i++){
			//Check for 'V' and replace with 'F'
			if (letters.get(i).equals('V')){
				letters.set(i, 'F');
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*-----------------------------------------------------------------
		 * Step 15: 'WH' transforms to 'W' if at the beginning. Drop 'W' if
		 * not followed by a vowel
		 *-----------------------------------------------------------------*/
		if (letters.get(0).equals('W') && letters.get(1).equals('H')){
			letters.remove(1);
			//compare suggestions
			compareSuggestion();
		}
		else{
			for (int x = 0, y = 1; y < letters.size(); x++, y++){
				if (letters.get(x).equals('W') && !vowels.contains(letters.get(y))){
					letters.remove(x);
				}
				//compare suggestions
				compareSuggestion();
			}
		}
		
		/*------------------------------------------------------------------------------
		 * Step 16: 'X' transforms to 'S' if at the beginning. Otherwise, 'X' transforms
		 * to 'KS'
		 *------------------------------------------------------------------------------*/
		if (letters.get(0).equals('X')){
			letters.set(0, 'S');
			//compare suggestions
			compareSuggestion();
		}
		else{
			for (int i = 1; i < letters.size(); i++){
				if (letters.get(i).equals('X')){
					letters.set(i, 'K');
					letters.add(i + 1, 'S');
				}
				//compare suggestions
				compareSuggestion();
			}
		}
		
		/*---------------------------------------------
		 * Step 17: Drop 'Y' if not followed by a vowel
		 *---------------------------------------------*/
		for (int x = 0, y = 1; y < letters.size(); x++, y++){
			if (letters.get(x).equals('Y') && vowels.contains(letters.get(y))){
				letters.remove(x);
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*-------------------------------
		 * Step 18: 'Z' transforms to 'S'
		 *-------------------------------*/
		for (int i = 0; i < letters.size(); i++){
			if (letters.get(i).equals('Z')){
				letters.set(i, 'S');
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*----------------------------------------------------
		 * Step 19: Drop all vowels unless it is the beginning
		 *----------------------------------------------------*/
		for (int i = 1; i < letters.size(); i++){
			if (vowels.contains(letters.get(i))){
				letters.remove(i);
			}
			//compare suggestions
			compareSuggestion();
		}
		
		/*--------------------
		 * ALGORITHM FINISHED
		 *--------------------*/
		
		//System.out.println("fixed: " + letters);	//DEBUG
		//check for any more possible corrections
		checkRest();
	}
	
	/**
	 * A method that converts the Character ArrayList to a String for easier comparisons
	 * @param word
	 * @return the built string
	 */
	public String toString (ArrayList<Character> word){
		//Rebuild the string using StringBuilder and insert each character into the string
		//one by one
		StringBuilder builder = new StringBuilder(word.size());
		for (Character ch : word){
			builder.append(ch);
		}
		return builder.toString();
	}
	
	/**
	 * A method that compares the suggested word by the Metaphone algorithm to a suggested
	 * list of words from the Levenshtein distance algorithm. If the word is found, then it
	 * is displayed on the first available suggestion button
	 */
	public void compareSuggestion(){
		//Since there are only 5 buttons (suggestions), the algorithm will only need to run till
		//it finds 5 suggestions
		if (suggestCounter < 5){
			//if the suggestion is found in the levenWords arraylist add it to the buttons IF
			//there are no duplicates
			if (LevenshteinDistance.levenWords.contains(toString(letters))){
				if (suggestCounter == 0){
					WordSpell.suggestButton[suggestCounter].setText(toString(letters));
					suggestCounter++;
				}
				else if (!WordSpell.suggestButton[suggestCounter - 1].getText().equals(toString(letters))){
					WordSpell.suggestButton[suggestCounter].setText(toString(letters));
					suggestCounter++;
				}
			}
			//reset the letters ArrayList back to the original word
			resetWord();
		}
	}
	
	/**
	 * Resets the Character ArrayList after each edit
	 */
	public void resetWord(){
		//clear the arrayList
		letters.clear();
		//move the letters of the word to the Character ArrayList
		for (char c : input.toCharArray()){
			letters.add(c);
		}
	}
	
	/**
	 * This method checks if there are any more possible word suggestions not suggested by the 
	 * Metaphone algorithm above. If there are more, it sorts them according to whichever word has
	 * more in common with the misspelled word.
	 */
	public void checkRest(){
		int buttonText = sorting.length - 1;
		
		/*
		 * In this chunk of code, we prepare to sort the levenWords by the highest Levenshtein Distance. 
		 * This way the program provides a more accurate list of suggestions
		 */
		for (int i = 0; i < LevenshteinDistance.levenWords.size(); i++){
			int count = 0;
			for (int m = 0; m < letters.size(); m++){
				if (LevenshteinDistance.levenWords.get(i).contains(Character.toString(letters.get(m)))){
					System.out.println("YES");
					count++;
					sorting[i][0] = count;
				}
				else{
					System.out.println("NO");
				}
			}
		}
		
		//fill the second dimension of the array to be able to sort the suggested words
		for (int i = sorting.length - 1; i >= 0; i--){
			sorting[i][1] = i;
		}
		
		//sort the array by highest Levenshtein Distance
		Arrays.sort(sorting, new Comparator<int[]>(){
			public int compare(int[] a, int[] b) {
				return Integer.compare(a[0], b[0]);
			}
		});
		
		//add the best suggested words to the suggestion buttons
		while (suggestCounter < 5){
			WordSpell.suggestButton[suggestCounter].setText(LevenshteinDistance.levenWords.get(sorting[buttonText][1]));
			buttonText--;
			suggestCounter++;
		}
	}
}
