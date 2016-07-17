/**
 * JSON utilities to speed the JSon manipulating without big footprint frameworks
 * @author Emerson
 *
 */
public class JSONutil {
	
	/**
	 * Returns a content field located at a JSon string record
	 * @param field Field name to get the content
	 * @param jSonRec JSon record to search the field and the content
	 * @return Content of the field
	 */
	static String getFieldValue(String field, String jSonRec) {
		String ret = "";
		String stSearch = "\""+field+"\":";
		Integer posSearch = jSonRec.indexOf(stSearch);
		if(posSearch > -1) {
			Integer pos = posSearch + stSearch.length() + 1;
			Integer endPos = jSonRec.indexOf('"', pos);
			ret = jSonRec.substring(pos, endPos);
		}
		return ret;
	}
	
	/**
	 * Returns a field JSon formatted
	 * @param field Field name to be formatted
	 * @param content Content of the field
	 * @return String JSon formatted
	 */
	static String getFmtField(String field, String content){
		return "\""+field+"\":\""+content+"\"";
	}
}
