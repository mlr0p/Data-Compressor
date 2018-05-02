/**
 * The encoder uses the LZW algorithm and multiway trie to output phrase numbers
 *
 * @author Michael Tsai
 * @author David Trye
 */

import java.io.*;
import java.util.*;

class Encoder{

	public static void main(String[] args){
		//If user does not provide exactly one argument, output a prompt
		if(args.length != 1) {
			System.err.println("Usage:  java Encoder <int>");
			return;
		}
		try {
			//Store maximum number of bits that can be used to encode a phrase number
			int dictionaryCapacity = Integer.parseInt(args[0]);
			//Check 2^dictionaryCapacity will be greater than the initial number of phrases in the trie
			if(dictionaryCapacity <= 8) {
				System.err.println("Please provide an integer greater than 8!");
				return;
			}
			//Calculate the maximum size of the trie
			dictionaryCapacity = (int)Math.pow(2, dictionaryCapacity);
	
			//Input is a stream of bytes from standard input
			//Declare a ByteArrayOutputStream to enable bytes to be read from Standard In
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[32];
			int bytesRead;

			//Read from standard in and write to output stream
			while ((bytesRead = System.in.read(buffer)) > 0) {
					bos.write(buffer, 0, bytesRead);
			}
			//Convert the bytes read to byte array
			byte[] input_bytes = bos.toByteArray();

			//Create multi-way trie for encoding dictionary
			MultiwayTrie mwt = new MultiwayTrie();
			//Populate trie with a symbol set that includes every possible byte
			String initial_path = "";
			for(int i=-128; i<=127; i++){
				mwt.addPhrase(initial_path.getBytes(), (byte)i);
			}

			//Counter variable to store the maximum phrase number currently in trie, initialised to 255
			int maxPhraseNum = 255;
			/*
			Repeatedly find a phrase number for a matching prefix, output the phrase number and add
			the first mismatch character with the next available phrase number to the trie
			*/
			while(input_bytes.length!=0){
				//Find the matching prefix
				byte[] matchingPrefix = mwt.searchPrefix(input_bytes);
        //Remove the encoded phrases from the input
        input_bytes = Arrays.copyOfRange(input_bytes, matchingPrefix.length, input_bytes.length);
				//If the input is not empty
        if(input_bytes.length!=0){
					//Add mismatch character to trie
					mwt.addPhrase(matchingPrefix, input_bytes[0]);
					//Print out the phrase number
					byte[] addedPhrase = {input_bytes[0]};
					System.out.println(mwt.getPhraseNumber(matchingPrefix));
					//Increment number of phrases in the trie
					maxPhraseNum++;
					//If dictionary reaches full capacity
					if(maxPhraseNum >= dictionaryCapacity){
							//Throw away all acquired phrases and continue encoding as if starting from scratch
							//Reinitialise and repopulate the trie
    					mwt = new MultiwayTrie();
    					for(int i=-128; i<=127; i++){
    					    mwt.addPhrase(initial_path.getBytes(), (byte)i);
    					}       
							//Emit phrase number for reset symbol, which is one more than the maximum size
    					System.out.println(maxPhraseNum+1);
    					//Reset maximum phrase number in trie
    					maxPhraseNum = 255;
					}
        }
				//If the input is empty, output the last phrase number
				else{
					if(matchingPrefix.length!=0){
						System.out.println(mwt.getPhraseNumber(matchingPrefix));
					}
				}
      }			
		} 
		catch(Exception e) {
			//Inform user that they need to input an integer
			System.err.println("Please enter an integer!");
		}
	}
}
