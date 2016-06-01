package edu.iastate.cs311.hw3;
/**
 * @author Neh Batwara
 * ComS 311 - HW#3
 */

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocumentSimilarity {

	private static String documentToString(String fileName) {
		try {
			FileInputStream file = new FileInputStream(fileName);
			int check;
			StringBuffer singleString = new StringBuffer("");
			while ((check = file.read()) != -1) {
				char nextChar = (char) check;
				if (Character.isWhitespace(nextChar) || nextChar == 44 | nextChar == 46 | nextChar == 58
						|| nextChar == 59) {
					continue;
				} else {
					singleString.append(nextChar);
				}
			}
			return singleString.toString();
		} catch (Exception exc) {
			exc.printStackTrace();
			return "";
		}
	}

	private static int hashValue(String s) {
		int hash = 0;
		int power = 0;
		int strInt = s.length() - 1;
		for (int i = strInt; i >= 0; i--) {
			hash += s.charAt(i) * (int)Math.pow(31, power++);
		}
		return (hash % Integer.MAX_VALUE);
	}

	private static int rollOverHash(int hashVal, char firstChar, char nextChar) {
		return (( hashVal - firstChar * (int)Math.pow(31, 7)) * 31  + nextChar) % Integer.MAX_VALUE;
	}
	
	public static double hashBasedSimilarity(String s1, String s2){
		// |s1| = n ; |s2| = m
		Set<String> shingleSet = getShingles(s2, 8);
		int counter = 0;
		int s1Len = s1.length();
		int[] arrHashCodes = new int[s1Len - 7];
		// O(n) time collection of hash codes
		for(int index = 0; index <= s1Len - 8; index++){
			String subStr = s1.substring(index, index + 8);
			arrHashCodes[index] = subStr.hashCode();
		}
		// O(nlogn) sorting
		Arrays.sort(arrHashCodes);
		 // Following loop runs : O(m * log n ) 
		for(String shingle : shingleSet){
			int hashVal = shingle.hashCode();
			// log n search 
			if(binarySearch(arrHashCodes, hashVal)){
				counter++;
			}
		}
		
		//Total Running Time : O ( n + nlogn + mlogn) = O( (n+m)logn)
		System.out.println("Counter :"  +counter) ;
		return (1.0 * counter) / ( s1.length() + s2.length() -16 ) ;
	}
	
	private static boolean binarySearch(int[] arr, int val){
		int beginIndex = 0;
		int endIndex = arr.length - 1;
		while(beginIndex <= endIndex){
			int mid = (beginIndex + endIndex) / 2;
			if(arr[mid] == val){
				return true;
			} else if ( arr[mid] < val){
				beginIndex = mid + 1;
			} else{
				endIndex = mid - 1;
			}
		}
		return false;
	}

	public static double bruteForceSimilarity(String s1, String s2) {
		Set<String> shingleSet = getShingles(s2, 8);
		int str1Len = s1.length();
		int counter =  0;
		for(String shingle : shingleSet){
			for (int i = 0; i <= str1Len - 8; i++) {
				int j = 0;
				while (j < 8 && s1.charAt(i + j) == shingle.charAt(j)) {
					j++;
				}
				if (j == 8) {
					counter++;
					break;
				}
			}
		}
		// similarity = counter / (s1.length + s2.length - 2m); where m is size of shingle
		System.out.println("Counter :"  +counter) ;
		return (1.0 * counter) / ( s1.length() + s2.length() -16 ) ; 
	}
	
	public static double krSimilarity(String s1, String s2){
		// Assuming : |s1| = n and |s2| = m
		Set<String> shingleSet = getShingles(s2, 8);
		int counter = 0;
		int s1Len = s1.length();
		int[] arrHashCodes = new int[s1Len - 7];
		arrHashCodes[0] = hashValue(s1.substring(0,8));
		// O(n) time cllection of hash codes
		for(int index = 1; index <= s1Len - 8; index++){
			arrHashCodes[index] = rollOverHash(arrHashCodes[index-1], s1.charAt(index - 1), s1.charAt(index + 7));
		}
		// O(nlogn) sorting
		Arrays.sort(arrHashCodes);
		
		// Runtime of Loop : O(mlogn)
		for(String shingle : shingleSet){
			int shingleHash =  hashValue(shingle); 
			if(binarySearch(arrHashCodes, shingleHash)){
				counter++;
			}
		}
		//Total Run time : O(n + nlogn + mlogn ) = O( (n+m)logn)
	
		return (1.0 * counter) / ( s1.length() + s2.length() -16 ) ;
	}
	
	private static Set<String> getShingles(String inputStr, int shingleLength){
		if(shingleLength <= 0){
			throw new IllegalArgumentException("Shingle Length must be greater than 0");
		}else if (inputStr == null || inputStr.length() <= 0){
			throw new IllegalArgumentException("Input String must be a not null string of length greater than 0");
		} else{
			//do nothing
		}
		Set<String> shingleSet = new HashSet<String>();
		for(int index = 0; index <= inputStr.length() - shingleLength ; index++){
			shingleSet.add(inputStr.substring(index, index+shingleLength ));
		}
		return shingleSet;
	}


	public static void main(String[] args) {
		

		String a = "abcde";
		String b = "bcdef";
		
		System.out.println("Hash Value a: "  + hashValue(a));
		System.out.println("Hash Value b: "  + hashValue(b));
		System.out.println("Java Hash Code" + a.hashCode());
		System.out.println("Java Hash Code" + b.hashCode());
		
		String c = "abcd";
		String d = "bcde";
		
		System.out.println("Hash Value c: " + rollOverHash(hashValue(c), 'a', 'e'));
		
//		final String file1 = "shakespeare1.txt";
//		final String file2 = "shakespeare2.txt";
//		
//		String doc1Str = documentToString(file1);
//		String doc2Str = documentToString(file2);
//		
//		long startTime = System.currentTimeMillis();
//		double similarityScore = bruteForceSimilarity(doc1Str, doc2Str);
//		long endTime = System.currentTimeMillis();
//		System.out.println("Brute Force Similarity Result : " +similarityScore +" ( Run Time : " +(endTime - startTime) +" )");
//		
//		doc1Str = documentToString(file1);
//		doc2Str = documentToString(file2);
//		
//		startTime = System.currentTimeMillis();
//		similarityScore = hashBasedSimilarity(doc1Str, doc2Str);
//		endTime = System.currentTimeMillis();
//		System.out.println("Hash Based Similarity Result : " +similarityScore +" ( Run Time : " +(endTime - startTime) +" )");
//		
//		
//		doc1Str = documentToString(file1);
//		doc2Str = documentToString(file2);
//		
//		startTime = System.currentTimeMillis();
//		similarityScore = krSimilarity(doc1Str, doc2Str);
//		endTime = System.currentTimeMillis();
//		System.out.println("Karp Rabin Similarity Result : " +similarityScore +" ( Run Time : " +(endTime - startTime) +" )");
		
		
		
	}
}
