package de.metalcon.imageStorageServer.protocol.create;

import java.io.IOException;
import java.io.InputStream;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.utils.FormFile;
import de.metalcon.utils.FormItemList;

/**
 * 
 * @author Christian Schowalter
 * 
 */
public class CreateRequest {

	// TODO: JavaDoc

	protected final String imageIdentifier;

	protected final InputStream imageStream;

	protected final String metaData;

	protected final boolean autoRotateFlag;

	public CreateRequest(final String imageIdentifier,
			final InputStream imageStream, final String metaData,
			final boolean autoRotateFlag) {
		this.imageIdentifier = imageIdentifier;
		this.imageStream = imageStream;
		this.metaData = metaData;
		this.autoRotateFlag = autoRotateFlag;
	}

	public String getImageIdentifier() {
		return this.imageIdentifier;
	}

	public InputStream getImageStream() {
		return this.imageStream;
	}

	public String getMetaData() {
		return this.metaData;
	}

	public boolean isAutoRotateFlag() {
		return this.autoRotateFlag;
	}

	public static CreateRequest checkRequest(final FormItemList formItemList,
			final CreateResponse response) {

		final String imageIdentifier = checkImageIdentifier(formItemList,
				response);
		if (imageIdentifier != null) {
			final InputStream imageStream = checkImageStream(formItemList,
					response);
			if (imageStream != null) {
				final String metaData = checkMetaData(formItemList, response);
				if ((metaData != null) || !response.getRequestErrorFlag()) {
					final Boolean autoRotateFlag = checkAutoRotateFlag(
							formItemList, response);
					if (autoRotateFlag != null) {
						return new CreateRequest(imageIdentifier, imageStream,
								metaData, autoRotateFlag);
					}
				}
			}
		}

		return null;
	}

	protected static String checkImageIdentifier(
			final FormItemList formItemList, final CreateResponse response) {
		try {
			return formItemList
					.getField(ProtocolConstants.Parameters.Create.IMAGE_IDENTIFIER);
		} catch (final IllegalArgumentException e) {
			response.imageIdentifierMissing();
		}

		return null;
	}

	protected static InputStream checkImageStream(
			final FormItemList formItemList, final CreateResponse response) {
		try {
			final FormFile imageItem = formItemList
					.getFile(ProtocolConstants.Parameters.Create.IMAGE_ITEM);
			if (imageItem != null) {
				return imageItem.getFormItem().getInputStream();
			}

		} catch (final IllegalArgumentException e) {
			response.imageStreamMissing();
		} catch (final IOException e) {
			response.internalServerError();
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

	protected static Boolean checkAutoRotateFlag(
			final FormItemList formItemList, final CreateResponse response) {
		try {
			final String autoRotateFlag = formItemList
					.getField(ProtocolConstants.Parameters.Create.AUTOROTATE_FLAG);

			if ("1".equals(autoRotateFlag)) {
				return true;
			} else if ("0".equals(autoRotateFlag)) {
				return false;
			} else {
				response.autoRotateFlagMalformed(autoRotateFlag);
			}
		} catch (final IllegalArgumentException e) {
			return false;
		}

		return null;
	}

}