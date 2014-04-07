package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.read.ReadResponse;
import de.metalcon.imageStorageServer.protocol.read.ReadScaledRequest;
import de.metalcon.utils.formItemList.FormItemList;

public class ReadScaledRequestTest extends RequestTest {

    private ReadScaledRequest readScaledRequest;

    private void fillRequest(
            final String imageIdentifier,
            final String width,
            final String height) {
        FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }

        if (width != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Read.IMAGE_WIDTH, width);

        }

        if (height != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Read.IMAGE_HEIGHT, height);
        }

        final ReadResponse readResponse = new ReadResponse();
        readScaledRequest =
                ReadScaledRequest.checkRequest(formItemList, readResponse);
        extractJson(readResponse);
    }

    @Test
    public void testReadScaledRequest() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        assertNotNull(readScaledRequest);
        assertEquals(VALID_IDENTIFIER, readScaledRequest.getImageIdentifier());
        assertEquals(ProtocolTestConstants.VALID_SCALING_WIDTH,
                String.valueOf(readScaledRequest.getImageWidth()));
        assertEquals(ProtocolTestConstants.VALID_SCALING_HEIGHT,
                String.valueOf(readScaledRequest.getImageHeight()));
    }

    @Test
    public void testImageIdentifierMissing() {
        fillRequest(null, ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER);
        assertNull(readScaledRequest);
    }

    @Test
    public void testWidthMissing() {
        fillRequest(VALID_IDENTIFIER, null,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_WIDTH);
        assertNull(readScaledRequest);
    }

    @Test
    public void testWidthMalformed() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.MALFORMED_SCALING_VALUE,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_MALFORMED);
        assertNull(readScaledRequest);
    }

    @Test
    public void testWidthInvalid() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.INVALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_INVALID);
        assertNull(readScaledRequest);
    }

    @Test
    public void testHeightMissing() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.VALID_SCALING_WIDTH, null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_HEIGHT);
        assertNull(readScaledRequest);
    }

    @Test
    public void testHeightMalformed() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.MALFORMED_SCALING_VALUE);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_MALFORMED);
        assertNull(readScaledRequest);
    }

    @Test
    public void testHeightInvalid() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.INVALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_INVALID);
        assertNull(readScaledRequest);
    }

}
