package de.metalcon.imageServer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetaDatabaseTest {

	private static final String VALID_READ_IDENTIFIER = "img1";

	private static final String INVALID_READ_IDENTIFIER = "noimg";

	private static final String VALID_CREATE_IDENTIFIER = "img2";

	private static final JSONObject META_DATA = new JSONObject();

	private static final JSONParser PARSER = new JSONParser();

	private static MetaDatabase DB;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DB = new MetaDatabase("localhost", 27017, "testdb");
		META_DATA.put("pos", "home");
	}

	@Before
	public void setUp() throws Exception {
		DB.clear();
		assertTrue(DB.addDatabaseEntry(VALID_READ_IDENTIFIER, META_DATA));
	}

	@Test
	public void testAddDatabaseEntry() {
		assertTrue(DB.addDatabaseEntry(VALID_CREATE_IDENTIFIER, META_DATA));
		assertFalse(DB.addDatabaseEntry(VALID_CREATE_IDENTIFIER, META_DATA));
	}

	@Test
	public void testGetAddedMetaData() {
		// tested implicitly by all tests reading!
		this.testAddDatabaseEntry();
		assertNotNull(DB.getMetadata(VALID_CREATE_IDENTIFIER));
	}

	@Test
	public void testGetMetaData() {
		final String metaData = DB.getMetadata(VALID_READ_IDENTIFIER);
		assertNotNull(metaData);

		final JSONObject metaDataJSON = parseToJSON(metaData);
		assertTrue(META_DATA.equals(metaDataJSON));

		final String noMetaData = DB.getMetadata(INVALID_READ_IDENTIFIER);
		assertNull(noMetaData);
	}

	@Test
	public void testAppendMetaData() {
		assertTrue(DB.appendMetadata(VALID_READ_IDENTIFIER, "pos", "home1"));
		assertFalse(DB.appendMetadata(INVALID_READ_IDENTIFIER, "pos", "null"));
	}

	@Test
	public void testGetAppendedMetaData() {
		this.testAppendMetaData();

		final String metaData = DB.getMetadata(VALID_READ_IDENTIFIER);
		assertNotNull(metaData);

		final JSONObject metaDataJSON = parseToJSON(metaData);
		assertFalse(META_DATA.equals(metaDataJSON));
	}

	@Test
	public void testDeleteDatabaseEntry() {
		assertTrue(DB.deleteDatabaseEntry(VALID_READ_IDENTIFIER));
		assertFalse(DB.deleteDatabaseEntry(VALID_READ_IDENTIFIER));
	}

	@Test
	public void testGetDeletedMetaData() {
		this.testDeleteDatabaseEntry();
		assertNull(DB.getMetadata(VALID_READ_IDENTIFIER));
	}

	/**
	 * parse a String to a JSON object<br>
	 * <b>fails the test</b> if the parsing failed
	 * 
	 * @param value
	 *            String to be parsed
	 * @return JSON object represented by the String passed<br>
	 *         <b>null</b> if the parsing failed
	 */
	protected static JSONObject parseToJSON(final String value) {
		try {
			return (JSONObject) PARSER.parse(value);
		} catch (final ParseException e) {
			fail("failed to parse to JSON object!");
		}

		return null;
	}

}