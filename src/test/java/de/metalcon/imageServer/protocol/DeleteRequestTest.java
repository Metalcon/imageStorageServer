package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.delete.DeleteRequest;
import de.metalcon.imageStorageServer.protocol.delete.DeleteResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class DeleteRequestTest extends RequestTest {

    private DeleteRequest deleteRequest;

    private void fillRequest(final String imageIdentifier) {
        FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Delete.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }

        final DeleteResponse deleteResponse = new DeleteResponse();
        deleteRequest =
                DeleteRequest.checkRequest(formItemList, deleteResponse);
        extractJson(deleteResponse);
    }

    @Test
    public void testDeleteRequest() {
        fillRequest(VALID_IDENTIFIER);
        assertNotNull(deleteRequest);
        assertEquals(VALID_IDENTIFIER, deleteRequest.getImageIdentifier());
    }

    @Test
    public void testImageIdentifierMissing() {
        fillRequest(null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Delete.IMAGE_IDENTIFIER);
        assertNull(deleteRequest);
    }
}
