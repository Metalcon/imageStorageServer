package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.read.ReadBulkRequest;
import de.metalcon.imageStorageServer.protocol.read.ReadResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class ReadBulkRequestTest extends RequestTest {

    private static final String MALFORMED_IDENTIFIER_LIST = "test,,test";

    private static final String VALID_IMAGE_IDENTIFIER_LIST = "ii1, ii2, ii3";

    private ReadBulkRequest readBulkRequest;

    private void fillRequest(
            final String imageIdentifierList,
            final String width,
            final String height) {
        FormItemList formItemList = new FormItemList();

        if (imageIdentifierList != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER_LIST,
                    imageIdentifierList);
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
        readBulkRequest =
                ReadBulkRequest.checkRequest(formItemList, readResponse);
        extractJson(readResponse);

    }

    @Test
    public void testReadBulkRequest() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        assertNotNull(readBulkRequest);

        final String[] identifiers = VALID_IMAGE_IDENTIFIER_LIST.split(",");
        final String[] readIdentifiers =
                readBulkRequest.getImageIdentifierList();
        assertNotNull(readIdentifiers);
        assertEquals(identifiers.length, readIdentifiers.length);

        int i = 0;
        for (String identifier : identifiers) {
            assertEquals(identifier, readIdentifiers[i++]);
        }

        assertEquals(ProtocolTestConstants.VALID_SCALING_WIDTH,
                String.valueOf(readBulkRequest.getImageWidth()));
        assertEquals(ProtocolTestConstants.VALID_SCALING_HEIGHT,
                String.valueOf(readBulkRequest.getImageHeight()));
    }

    @Test
    public void testImageIdentifierListMissing() {
        fillRequest(null, ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER);
        assertNull(readBulkRequest);
    }

    @Test
    public void testImageIdentifierListMalformed() {
        fillRequest(MALFORMED_IDENTIFIER_LIST,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.IDENTIFIER_LIST_CONTAINS_EMPTY_PARTS);
        assertNull(readBulkRequest);
    }

    @Test
    public void testWidthMissing() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST, null,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_WIDTH);
        assertNull(readBulkRequest);
    }

    @Test
    public void testWidthMalformed() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.MALFORMED_SCALING_VALUE,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_MALFORMED);
        assertNull(readBulkRequest);
    }

    @Test
    public void testWidthInvalid() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.INVALID_SCALING_WIDTH,
                ProtocolTestConstants.VALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_WIDTH_INVALID);
        assertNull(readBulkRequest);
    }

    @Test
    public void testHeightMissing() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.VALID_SCALING_WIDTH, null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_HEIGHT);
        assertNull(readBulkRequest);
    }

    @Test
    public void testHeightMalformed() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.MALFORMED_SCALING_VALUE);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_MALFORMED);
        assertNull(readBulkRequest);
    }

    @Test
    public void testHeightInvalid() {
        fillRequest(VALID_IMAGE_IDENTIFIER_LIST,
                ProtocolTestConstants.VALID_SCALING_WIDTH,
                ProtocolTestConstants.INVALID_SCALING_HEIGHT);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Read.SCALING_HEIGHT_INVALID);
        assertNull(readBulkRequest);
    }

}
