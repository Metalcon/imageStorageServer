package de.metalcon.imageStorageServer.protocol.read;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.Response;

public class ReadResponse extends Response {

	// TODO: write helpful solution messages for missing parameters!
	// TODO: JavaDoc
	// TODO: refactor code using constants (in malformed-error methods)
	// TODO: refactor to match the other protocol request/response patterns

	/**
	 * Adds an error message for a missing image identifier parameter to the
	 * response.
	 */
	public void addNoImageIdentifierError() {
		this.parameterMissing(
				ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER,
				"The image identifier is missing. Please deliver one");

	}

	/**
	 * Adds an error message for a malformed image identifier String to the
	 * response.
	 */
	public void addOriginalImageFlagMalformedError() {
		this.addStatusMessage(
				"request corrupt: parameter \"originalFlag\" is malformed",
				"The originalImage flag String is malformed");

	}

	/**
	 * Adds an error message for missing image scaling width parameter to the
	 * response.
	 */
	public void addNoImageWidthError() {
		this.parameterMissing(
				ProtocolConstants.Parameters.Read.IMAGE_WIDTH,
				"The image width is not given. Either deliver width AND height or leave them out and ask for the unscaled image");
	}

	/**
	 * Adds an error message for a malformed image scaling width parameter
	 * string to the response.
	 */
	public void addImageWidthMalformedError(final String width) {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_MALFORMED,
				"The imageWidth String is malformed");
	}

	public void addImageWidthInvalidError(final int width) {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_INVALID,
				"The requested width is zero or less. Please specify a value greater than zero instead.");
	}

	/**
	 * Adds an error message for a missing image scaling height parameter to the
	 * response.
	 */
	public void addNoImageHeightError() {
		this.parameterMissing(
				ProtocolConstants.Parameters.Read.IMAGE_HEIGHT,
				"The image height is not given. Either deliver width AND height or leave them out and ask for the unscaled image");
	}

	/**
	 * Adds an error message for a malformed image scaling height parameter
	 * string to the response.
	 */
	public void addImageHeightMalformedError(final String height) {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_MALFORMED,
				"The imageHeight String is malformed");
	}

	public void addImageHeightInvalidError(final int height) {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_INVALID,
				"The requested height is zero or less. Please specify a value greater than zero instead.");
	}

	/**
	 * Adds an image not found error message to the response.
	 */
	public void addImageNotFoundError() {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.NO_IMAGE_FOUND, "");
	}

	/**
	 * Adds a warning concerning the requested image size being bigger than the
	 * available source image.
	 */
	public void addGeometryBiggerThanOriginalWarning() {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.GEOMETRY_BIGGER_THAN_ORIGINAL,
				"");
	}

	public void addImageIdentifierListContainsEmptyFieldsError() {
		this.addStatusMessage(
				ProtocolConstants.StatusMessage.Read.IDENTIFIER_LIST_CONTAINS_EMPTY_PARTS,
				"");
	}

}