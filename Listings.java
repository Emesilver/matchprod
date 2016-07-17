import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Emerson
 * Load listings.txt to memory and provide functions to do search on it
 * If listings is more big, consider to change this class to operate reading direct from the disk to prevent memory overload.
 * If listings is really more big, consider to change this class to load to a noSQL database like MongoDB
 * If listings is really really more big, consider to change this class to load to a distributed noSQL database like Cassandra 
 */
public class Listings {

	Map<Integer, String> idxList = new HashMap<Integer, String>();
	Map<String, ArrayList<Integer>> idxManuf;
	
	public void load(String fileName) {
		
		try {
			FileReader fReader = new FileReader(System.getProperty("user.dir")+File.separator+fileName);
			BufferedReader bReader = new BufferedReader(fReader);
			Integer key = 1;
			
			for(String line = bReader.readLine(); line != null; line = bReader.readLine()) {
				idxList.put(key++, line);
			}
			bReader.close();
		} catch (IOException e) {
			System.out.println("Fail opening file "+fileName);
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Create a list with classified words for manufacturer to speed up and limit the search
	 * (Artificial Intelligence technique)
	 */
	public void classify() {
		
        // Store records of the same manufacturer words
        HashMap<String, ArrayList<Integer>> idxBuffer = new HashMap<String, ArrayList<Integer>>();
        
		for(Map.Entry<Integer, String> entry : idxList.entrySet() ) {
			String manufacturer;
			manufacturer = getManufacturer(entry.getValue());
	        manufacturer = manufacturer.toUpperCase();
	        String[] manufWords = manufacturer.split(" ");
	        for(int ind = 0; ind < manufWords.length; ind++){
	        	// Will be considered just words with more than one letter. Just one letter will not help.
	        	if(manufWords[ind].length()>1) {
		        	ArrayList<Integer> records = idxBuffer.get(manufWords[ind]);
		        	if(records == null){
		        		records = new ArrayList<Integer>();
		        	}
	        		records.add(entry.getKey());
		        	idxBuffer.put(manufWords[ind], records);
	        	}
	        }
		}
		// Indexing to faster access
		idxManuf = new TreeMap<String, ArrayList<Integer>>(idxBuffer);
	}

	private String getManufacturer(String jSonRec) {
		return JSONutil.getFieldValue("manufacturer", jSonRec);
	}
	
	// Returns a list of records for a manufacturer considering all words of manufacturer name
	public ArrayList<Integer> records(String manuf) {
		String manufUpper = manuf.toUpperCase();
		String manufWords[] = manufUpper.split(" ");
		ArrayList<Integer> ret = new ArrayList<Integer>();
		if(manufWords.length > 0) {
			ret = idxManuf.get(manufWords[0]);
			// Retain the records with all words
			for(int ind=1; ind < manufWords.length; ind++){
				ret.retainAll(idxManuf.get(manufWords[ind]));
			}
		}
		return ret;
	}
	
	// Returns a list of records matching the product
	public ArrayList<Integer> match(String prodName, ArrayList<Integer> records){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		String prodNameUpper = prodName.toUpperCase();
//		System.out.println("************************ "+ prodNameUpper+":");
		String[] lstWords1 = prodNameUpper.split("_");
		ArrayList<String> prodNameWords = new ArrayList<String>();
		for(int ind=0; ind<lstWords1.length; ind++){
			String[] lstWords2 = lstWords1[ind].split("-");
			for(int ind2=0; ind2<lstWords2.length; ind2++){
				prodNameWords.add(lstWords2[ind2]);
			}
		}
		// Search in all records of this manufacturer
		for(Integer rec : records){
			if(match(prodNameWords, JSONutil.getFieldValue("title", idxList.get(rec)))){
				ret.add(rec);
			}
		}
		
		return ret;
	}
	
	// Returns true if a list of words match the string stMatch
	private boolean match(ArrayList<String> prodNameWords, String stMatch){
		boolean ret = false;
		if(prodNameWords.size() > 0){
			String stMatchUpper = stMatch.toUpperCase();
			int ind=0;
			ret = true;
			while(ret && (ind < prodNameWords.size())){
				ret = stMatchUpper.contains(prodNameWords.get(ind));
				ind++;
			}
		}
		/*
		if(ret){
			System.out.println(stMatch);
		}
		*/
		return ret;
	}
}
