/**
 * The decoder reads phrase numbers from standard input and outputs the 
 * corresponding sequence of bytes, assuming an LZW encoding
 *
 * @author Michael Tsai
 * @author David Trye
 */

import java.io.*;
import java.util.*;

class Decoder{

	public static void main(String[] args){

		try{
			//Wrap a scanner around standard in
			Scanner sc = new Scanner(System.in);	
			//Assumes the dictionary has 256 phrases at the outset
			ArrayList<byte[]> dictionary = new ArrayList<byte[]>();
			//Populate the dictionary with known symbols
			for(int i=-128; i<=127; i++){
				dictionary.add(new byte[] {(byte)i});
			}
			
			//Store the current and next phrase number
			int phraseNum = sc.nextInt();
			int	nextPhraseNum = sc.nextInt();

			//Loop through the phrase numbers from Standard In
			while(sc.hasNext()){
				//Check if the phrase number exists in the dictionary
				//to determine whether it needs to be reset
				if(phraseNum < dictionary.size()){
					//Write the corresponding phrase to standard out
					System.out.write(dictionary.get(phraseNum));

					//A variable to store the encountered phrases
					ByteArrayOutputStream phrases = new ByteArrayOutputStream();

					//If the next phrase number signals the dictionary
					//has to be reset
					if(nextPhraseNum>dictionary.size()){
						//Do nothing
						//Move on to the next phrase number
						;
					}
					//If the next phrase number is not in our dictionary
					//and no reset is needed
					else if (nextPhraseNum == dictionary.size()){
						//Concatenate the current phrase with its first character
						phrases.write(dictionary.get(phraseNum));
						phrases.write(phrases.toByteArray()[0]);
						//Add the newly created phrase to the dictionary
						dictionary.add(phrases.toByteArray());
					}
					//If the next phrase number is in our dictionary
					else{
						//Concatenate the current phrase with the first character
						//from the next phrase
						phrases.write(dictionary.get(phraseNum));
						phrases.write(dictionary.get(nextPhraseNum)[0]);
						//Add the newly created phrase to the dictionary
						dictionary.add(phrases.toByteArray());
					}
					//Proceed to the next phrase
					phraseNum = nextPhraseNum;
					nextPhraseNum = sc.nextInt();
				}
				//If reset is needed
				else{	
					//Reinitialise and repopulate the dictionary
					dictionary = new ArrayList<byte[]>();
					for(int i=-128; i<=127; i++){
						dictionary.add(new byte[] {(byte)i});
					}
					//Proceed to the next phrase number
					phraseNum = nextPhraseNum;
					nextPhraseNum = sc.nextInt();
				}
			}	
			//Write out the last phrase for both the current phrase number
			//and the next phrase number
			System.out.write(dictionary.get(phraseNum));
			System.out.write(dictionary.get(nextPhraseNum));
		}
		catch(Exception e){
			;
		}
	}
}
