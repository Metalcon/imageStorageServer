package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.read.ReadRequest;
import de.metalcon.imageStorageServer.protocol.read.ReadResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class ReadOriginalRequestTest extends RequestTest {

    private ReadRequest readRequest;

    private void fillRequest(final String imageIdentifier) {
        FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }

        final ReadResponse readResponse = new ReadResponse();
        readRequest = ReadRequest.checkRequest(formItemList, readResponse);
        extractJson(readResponse);
    }

    @Test
    public void testReadOriginalRequest() {
        fillRequest(VALID_IDENTIFIER);
        assertNotNull(readRequest);
        assertEquals(VALID_IDENTIFIER, readRequest.getImageIdentifier());
    }

    @Test
    public void testImageIdentifierMissing() {
        fillRequest(null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Read.IMAGE_IDENTIFIER);
        assertNull(readRequest);
    }
}
