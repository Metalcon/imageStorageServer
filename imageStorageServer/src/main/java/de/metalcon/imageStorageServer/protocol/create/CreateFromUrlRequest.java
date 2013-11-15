package de.metalcon.imageStorageServer.protocol.create;

import java.net.MalformedURLException;
import java.net.URL;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.utils.FormItemList;

// TODO: JavaDoc according to specification

/**
 * create requests using an URL
 * 
 * @author sebschlicht
 * 
 */
public class CreateFromUrlRequest {

	protected final String imageIdentifier;

	protected final URL imageUrl;

	protected final String metaData;

	public CreateFromUrlRequest(final String imageIdentifier,
			final URL imageUrl, final String metaData) {
		this.imageIdentifier = imageIdentifier;
		this.imageUrl = imageUrl;
		this.metaData = metaData;
	}

	public String getImageIdentifier() {
		return this.imageIdentifier;
	}

	public URL getImageUrl() {
		return this.imageUrl;
	}

	public String getMetaData() {
		return this.metaData;
	}

	public static CreateFromUrlRequest checkRequest(
			final FormItemList formItemList, final CreateResponse response) {
		final String imageIdentifier = checkIdentifier(formItemList, response);
		if (imageIdentifier != null) {
			final URL imageUrl = checkUrl(formItemList, response);
			if (imageUrl != null) {
				// TODO: workarround: meta data null != error
				final String metaData = checkMetaData(formItemList, response);

				return new CreateFromUrlRequest(imageIdentifier, imageUrl,
						metaData);
			}
		}

		return null;
	}

	protected static String checkIdentifier(final FormItemList formItemList,
			final CreateResponse response) {
		try {
			return formItemList
					.getField(ProtocolConstants.Parameters.Create.IMAGE_IDENTIFIER);
		} catch (final IllegalArgumentException e) {
			response.imageIdentifierMissing();
		}

		return null;
	}

	protected static URL checkUrl(final FormItemList formItemList,
			final CreateResponse response) {
		try {
			final String imageUrlString = formItemList
					.getField(ProtocolConstants.Parameters.Create.URL);

			try {
				URL imageUrl = new URL(imageUrlString);
				return imageUrl;
			} catch (final MalformedURLException e) {
				response.imageUrlMalformed(imageUrlString);
			}
		} catch (final IllegalArgumentException e) {
			response.imageUrlMissing();
		}

		return null;
	}

	protected static String checkMetaData(final FormItemList formItemList,
			final CreateResponse response) {
		try {
			return formItemList
					.getField(ProtocolConstants.Parameters.Create.META_DATA);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}