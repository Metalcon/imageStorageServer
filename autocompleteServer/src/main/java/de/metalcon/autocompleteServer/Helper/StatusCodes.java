/**
 * 
 */
package de.metalcon.autocompleteServer.Helper;

/**
 * Status codes that are used in the Auto Suggest Transfer Protocol (ASTP)
 * @author Rene Pickhardt
 *
 */
public class StatusCodes {
	public static final String NUMITEMS_NOT_AN_INTEGER = "The numItems Parameter needs to be an Integer. I set it to: " + ProtocolConstants.MAX_NUMBER_OF_SUGGESTIONS;
	public static final String NUMITEMS_NOT_GIVEN = "You did not give the number of items I set it to: "+ ProtocolConstants.MAX_NUMBER_OF_SUGGESTIONS;
	public static final String NUMITEMS_OUT_OF_RANGE = "The rquested number of items is out of range. It has to be between 1 and " + ProtocolConstants.MAX_NUMBER_OF_SUGGESTIONS + " I set it to: "+ ProtocolConstants.MAX_NUMBER_OF_SUGGESTIONS;
	public static final String QUERYNAME_NOT_GIVEN = "No name given. A suggest item always needs a name. Please enter a name for your item.";
	public static final String INDEXNAME_NOT_GIVEN = "Missing index name. You need to specify which index the entry should be inserted into.";

}
