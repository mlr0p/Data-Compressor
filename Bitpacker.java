/**
 * The Bitpacker accepts phrase numbers as given by the encoder
 * and outputs them using the minimum number of bits
 *
 * @author Michael Tsai
 * @author David Trye
 */

import java.io.*;
import java.util.*;
import java.lang.*;

class Bitpacker{

	public static void main(String[] args){
		try{
			//Read in the phrase number from Standard In
			Scanner sc = new Scanner(System.in);
			//Store the phrase numbers in an array list
			ArrayList<Integer> uncompressedData = new ArrayList<Integer>();
			while(sc.hasNext()){
				int i = sc.nextInt();
				uncompressedData.add(i);		
			}			
			// Bitpack the uncompressed data
			bitPack(uncompressedData);
		}
		catch(Exception e){
			;
		}
	}
	/**
	 * Compute a squence of characters representing the printed version
	 * of the bit stream and output one byte at a time
	 * @param uncompressedData
	 * 		An arraylist of phrase numbers
	 */
	private static void bitPack(ArrayList<Integer> uncompressedData){		
		//Initialise the maximum phrase number and required bits
		int maxPhraseNum = 255;
		int rb = 8;
		//Initialise a variable to store  the printed version of the packed bits
		String unpackedBits = "";
		//Loop through the phrase numbers, concatenate printed version of the bits
		//to the unpackedBits variable, output one byte at a time
		for(int i: uncompressedData){
			//Calculate overall required bits
			rb = Integer.toBinaryString(maxPhraseNum).length();
			//Concatenate printed version of the bits to the unpackedBits variable
			unpackedBits += String.format("%"+rb+"s", Integer.toBinaryString(i)).replace(' ', '0');
			//Output one byte at a time
			while(unpackedBits.length()>=8){
				String bits = unpackedBits.substring(0, 8);
				unpackedBits = unpackedBits.substring(8);
				System.out.write((byte)(Integer.parseInt(bits, 2)));
			}
			//If we need to reset, reset the maxPhraseNum
			if(i > maxPhraseNum){
				maxPhraseNum = 255;
			}	
			else maxPhraseNum++;
		}
		//Add zeroes to the end of the last byte in order to maintain the correct order
		int l = unpackedBits.length();
		unpackedBits = String.format("%8s", unpackedBits).replace(' ', '0');
		unpackedBits = unpackedBits.substring(8-l)+unpackedBits.substring(0, 8-l);
		System.out.write((byte)(Integer.parseInt(unpackedBits, 2)));		
		System.out.flush();
	}
}
