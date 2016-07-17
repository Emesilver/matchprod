import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Process and write results to a JSON file records results.txt 
 * @author Emerson
 *
 */
public class Results {
	
	private Products products;
	private Listings listings;
	private BufferedWriter resultWriter;
	private String lineSep; // platform line separator
	
	// Result counters
	private Integer productsMatched;
	private Integer listingsMatched;
	private Integer productsNotMatched;
	
	public Results(Products products, Listings listings) {
		this.products = products;
		this.listings = listings;
	}
	
	public void process() {
		productsMatched=0;
		listingsMatched=0;
		productsNotMatched=0;

		try {
			File resultFile = new File(System.getProperty("user.dir")+File.separator+"result.txt");
			resultFile.createNewFile();
			FileWriter fw = new FileWriter(resultFile.getAbsolutePath());
			lineSep = System.getProperty("line.separator");
			resultWriter = new BufferedWriter(fw);
			// Process each product matching to the listings
			for(Map.Entry<Integer, String> entry : products.idxProd.entrySet()) {
				matchProduct(JSONutil.getFieldValue("product_name", entry.getValue()),
							 JSONutil.getFieldValue("manufacturer", entry.getValue()));
			}
			resultWriter.close();
			System.out.println("Result generated at "+resultFile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printReport();
	}
	
	// Try to match products to listings limiting to the same manufacturer
	private void matchProduct(String prodName, String manufacturer) {
		ArrayList<Integer> records = listings.records(manufacturer);
		productsNotMatched++;
		if(records != null){
			ArrayList<Integer> listMatch = listings.match(prodName, records);
			
			listingsMatched+=listMatch.size();
			if(listMatch.size()>0){
				writeResult(prodName, listMatch);				
				productsMatched++;
				productsNotMatched--;
			}
		}
		
	}
	
	// write a product result match to file
	private void writeResult(String prodName, ArrayList<Integer> listMatch){
		try {
			resultWriter.write("{");
			resultWriter.write(JSONutil.getFmtField("product_name", prodName));
			resultWriter.write(",\"listings\":[");
			String commaSep = "";
			for(Integer recMatch : listMatch){
				resultWriter.write(commaSep+listings.idxList.get(recMatch));
				commaSep = ",";
			}
			resultWriter.write("]");
			resultWriter.write("}"+lineSep);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Print a simple result report to screen
	private void printReport() {
		System.out.println("Products processed:"+products.idxProd.size());
		System.out.println("Listings processed:"+listings.idxList.size());
		System.out.println("Products Matched:"+productsMatched);
		System.out.println("Products not matched:"+productsNotMatched);
		System.out.println("Listings Matched:"+listingsMatched);
	}
}
