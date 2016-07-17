/**
 * Main class to match each product in products.txt on listings.txt and generate a results.txt
 * @author Emerson
 *
 */
public class MatchPro {
	
	public static void main(String args[]) {
		long startTime = System.currentTimeMillis();
		
		Products products = new Products();
		products.load("products.txt");
		System.out.println("Producs.txt loaded...");
		
		Listings listings = new Listings();
		listings.load("listings.txt");
		System.out.println("Listings.txt loaded..");
		
		listings.classify();
		System.out.println("Manufactors classified..");
		
		Results results = new Results(products, listings);
		System.out.println("Starting process..");
		results.process();
		
		long elepsedTime = System.currentTimeMillis() - startTime;
		
		System.out.println("Elepsed Time:"+elepsedTime+" (milli seconds)");
	}
	
}
