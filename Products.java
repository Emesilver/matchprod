import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Products {
	
	Map<Integer, String> idxProd = new HashMap<Integer, String>();
	
	public void load(String fileName) {
		
		try {
			FileReader fReader = new FileReader(System.getProperty("user.dir")+File.separator+fileName);
			BufferedReader bReader = new BufferedReader(fReader);
			Integer key = 1;
			
			for(String line = bReader.readLine(); line != null; line = bReader.readLine()) {
				idxProd.put(key++, line);
			}
			bReader.close();
		} catch (IOException e) {
			System.out.println("Fail opening file "+fileName);
			System.out.println(e.getMessage());
		}
		
	}
}
