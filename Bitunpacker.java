/**
 * The Bitunpacker accepts as input a stream of bytes and outputs the 
 * corresponding phrase number using the minimum number of bits
 *
 * @author Michael Tsai
 * @author David Trye
 */

import java.io.*;
import java.util.*;
import java.lang.*;

class Bitunpacker{

	public static void main(String[] args){
		try{
			//Reads in a stream of bytes from Standard In
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
  	  byte[] buffer = new byte[1024];
  	  int bytesRead;
  	  while ((bytesRead = System.in.read(buffer)) > 0) {
  	      bos.write(buffer, 0, bytesRead);
  	  }
			//Stores the bytes read to a variable
  	  byte[] compressedData = bos.toByteArray();	
			//Invoke the bitUnpack function	
			bitUnpack(compressedData);
		}
		catch(Exception e){
			;
		}
	}
	/**
	 * Retrieve the original packed bits, and output using the minimum  
	 * required bits
   * @param compressedData
   *    An arraylist of phrase number
   */
	private static void bitUnpack(byte[] compressedData){
		//Initialise the maximum phrase numbers, required bits
		int maxPhraseNum = 255;
		int rb = 8;
		String packedBits = "";
		//Loop through each byte and retrieve the packed bits
		//Output using minimum required bits
		for (byte b : compressedData){
			//Retrieve the packed bits
			String bt = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
			packedBits += bt;
			//Output using minimum required bits
			while(packedBits.length() >= rb){
				//Parse the integer in a binary format
				int n = Integer.parseInt(packedBits.substring(0, rb), 2);
				//Remove the unpacked bits
				packedBits = packedBits.substring(rb);
				//If we need to reset, reset the maximum phrase number
				//254 because it would be increased to 255
				if(n>maxPhraseNum) maxPhraseNum = 254;
				//Output the phrase number
				System.out.println(n);
				maxPhraseNum++;
				//Update the minimum required bits
				rb = Integer.toBinaryString(maxPhraseNum).length();
			}
		}
		//If the last item is not zero, print it to standard output
		if(Integer.parseInt(packedBits,2)!=0){
			System.out.println(Integer.parseInt(packedBits, 2));
		}
	}
}

