package de.metalcon.imageServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.metalcon.imageStorageServer.ImageStorageServer;
import de.metalcon.imageStorageServer.protocol.create.CreateResponse;

public class CreateGalleryPicturesBenchmark {

	private static final String SQL_DUMP = "/home/sebschlicht/galleryImage.sql";

	private static final String CONFIG_PATH = "test.iss.config";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(SQL_DUMP));
			ImageStorageServer server = new ImageStorageServer(CONFIG_PATH);
			server.clear();

			long timeCreate = 0, timePerCreate;
			long numCreate = 0, failed = 0;

			System.out.println("Benchmark running...");

			String line;
			while ((line = reader.readLine()) != null) {
				if (numCreate >= 1000) {
					break;
				}

				String[] items = extractInsertionItems(line);

				if (items != null) {
					System.out.println(items.length + " items");
					for (String item : items) {
						if (numCreate >= 1000) {
							break;
						}

						String[] values = extractItemValues(item);

						long time = System.nanoTime();
						CreateResponse response = new CreateResponse();

						try {
							if (!server.createImage(values[0],
									new FileInputStream(
											"/home/sebschlicht/Pictures/metalcon/pic/"
													+ values[1] + ".jpg"),
									null, false, response)) {
								System.err.println("failed to create image:");
								System.err.println(response);
								break;
							}

							timeCreate += System.nanoTime() - time;
							numCreate += 1;

						} catch (final FileNotFoundException e) {
							failed += 1;
						}
					}
				}
			}

			System.out.println("Benchmark finished.");
			System.out.println("Results ---------");
			System.out.println("Total time: " + (timeCreate / 1000 / 1000)
					+ "ms");
			System.out.println("Successful requests: " + numCreate);
			if (numCreate > 0) {
				timePerCreate = timeCreate / numCreate;
				System.out.println("per request: "
						+ (timePerCreate / 1000 / 1000) + "ms");
			}
			System.out.println("Failed requests: " + failed);
			System.out.println("-----------------");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private static String[] extractInsertionItems(String line) {
		// handle value insertion line only
		if (!line.startsWith("INSERT")) {
			return null;
		}

		// extract single insertion items
		line = line.substring(line.indexOf("(") + 1, line.length() - 1);
		return line.split("\\),\\(");
	}

	private static String[] extractItemValues(String item) {
		String[] values = item.split(",");
		values[1] = values[1].substring(1, values[1].length() - 1);
		values[5] = values[5].substring(1, values[5].length() - 1);

		String[] exportedValues = new String[6];
		exportedValues[0] = values[0]; // image ID
		exportedValues[1] = values[1]; // image key
		exportedValues[2] = values[2]; // user ID
		exportedValues[3] = values[3]; // gallery ID
		exportedValues[4] = values[5]; // date
		exportedValues[5] = values[12];// views

		return exportedValues;
	}
}