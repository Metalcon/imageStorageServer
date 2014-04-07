package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.update.UpdateRequest;
import de.metalcon.imageStorageServer.protocol.update.UpdateResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class UpdateRequestTest extends RequestTest {

    private UpdateRequest updateRequest;

    private void fillRequest(
            final String imageIdentifier,
            final String imageMetaData) {
        FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Update.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }

        if (imageMetaData != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Update.META_DATA,
                    imageMetaData);
        }

        final UpdateResponse updateResponse = new UpdateResponse();
        updateRequest =
                UpdateRequest.checkRequest(formItemList, updateResponse);
        extractJson(updateResponse);
    }

    @Test
    public void testUpdateRequest() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.VALID_IMAGE_METADATA);
        assertNotNull(updateRequest);
        assertEquals(VALID_IDENTIFIER, updateRequest.getImageIdentifier());
        assertEquals(ProtocolTestConstants.VALID_IMAGE_METADATA,
                updateRequest.getMetaData());
    }

    @Test
    public void testImageIdentifierMissing() {
        fillRequest(null, ProtocolTestConstants.VALID_IMAGE_METADATA);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Update.IMAGE_IDENTIFIER);
        assertNull(updateRequest);
    }

    @Test
    public void testMetadataMissing() {
        fillRequest(VALID_IDENTIFIER, null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Update.META_DATA);
        assertNull(updateRequest);
    }

    @Test
    public void testMetadataMalformed() {
        fillRequest(VALID_IDENTIFIER,
                ProtocolTestConstants.MALFORMED_IMAGE_METADATA);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Update.META_DATA_MALFORMED);
        assertNull(updateRequest);
    }
}
