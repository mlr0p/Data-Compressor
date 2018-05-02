/**
 * A self-implemented multiway trie to be used as a encoding dictionary
 * for the LZW compression and decompression assignment
 *
 * @author Michael Tsai
 * @author David Trye
 */

import java.io.*;
import java.util.*;

public class MultiwayTrie{

	//Initialize the trie size to 256 to store phrase number from
	//0 to 255 for every possible byte value
	int trieSize = 256;	
	//Negative one because we would increase it before we output the 
	//first phrase number
	int currentPhraseNumber = -1;
	//Create root trie node
	TrieNode root = new TrieNode();

	//Class for a node in the trie implemented
	//as an array, position in array as value
	private class TrieNode {
		int phraseNumber;
		TrieNode[] next = new TrieNode[trieSize];
	}

	//Constructor for the trie
	public MultiwayTrie(){
		root.phraseNumber = 0;
	}
	/**
	 *	Add a phrase (mismatch character) to the trie
	 *	@param path 
	 *		the matched prefix
	 *	@param newByte 
	 *		the first mismatch byte
	*/
	public void addPhrase(byte[] path, byte newByte){
		//Declare a temporary node to traverse down the trie
		TrieNode tn = root;
		//Go down the path
		for (byte b : path){
			//128 because java byte value is signed
			tn = tn.next[b+128];
		}		
		//Add a Trie Node at the end
		tn.next[newByte+128] = new TrieNode();
			currentPhraseNumber++;
			tn.next[newByte+128].phraseNumber = currentPhraseNumber;
	}
	
	/**
	 *	Return the phrase number for a matching prefix
	 *	@param path
	 *		a sequence of matched phrases
	 */
	public int getPhraseNumber(byte[] path){
		TrieNode tn = root;
		for (byte b : path){
			tn = tn.next[b+128];
		}
		return tn.phraseNumber;
	}

	/**
	 *	Returns a matching prefix for a specified array of bytes
	 *	@param path
	 *		a sequence of byte values
	*/
	public byte[] searchPrefix(byte[] bytes){
		//Default is zerofor matching length
		int matchingLength = 0;
		TrieNode tn = root;
		//Search until we hit a mismatch character
		for(matchingLength=0; matchingLength<bytes.length; matchingLength++){
			if(tn.next[bytes[matchingLength]+128]==null){
				return Arrays.copyOfRange(bytes, 0, matchingLength);
			}
			else{
				tn = tn.next[bytes[matchingLength]+128];
			}
		}
		//Return the matched phrases
		return bytes;
	}
}

