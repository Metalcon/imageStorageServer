package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.create.CreateFromUrlRequest;
import de.metalcon.imageStorageServer.protocol.create.CreateResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class CreateFromUrlTest extends RequestTest {

    private static final String MALFORMED_URL = "/:::malformedurl:::/";

    private static final String VALID_URL =
            "http://www.cyclonefanatic.com/forum/attachments/big-xii-conference/20805d1374005349-whats-awkward-awkward-club-photos-5.jpg";

    private CreateFromUrlRequest createFromUrlRequest;

    @Test
    public void testCreateFromUrl() {
        fillRequest(VALID_IDENTIFIER, VALID_URL);
        assertNotNull(createFromUrlRequest);
    }

    @Test
    public void testUrlMissing() {
        fillRequest(VALID_IDENTIFIER, null);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Create.URL);
        assertNull(createFromUrlRequest);
    }

    @Test
    public void testUrlMalformed() {
        fillRequest(VALID_IDENTIFIER, MALFORMED_URL);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Create.IMAGE_URL_MALFORMED);
        assertNull(createFromUrlRequest);
    }

    private void
        fillRequest(final String imageIdentifier, final String imageUrl) {
        // create and fill form item list
        final FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Create.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }
        if (imageUrl != null) {
            formItemList.addField(ProtocolConstants.Parameters.Create.URL,
                    imageUrl);
        }

        // check request and extract the response
        final CreateResponse createResponse = new CreateResponse();
        createFromUrlRequest =
                CreateFromUrlRequest.checkRequest(formItemList, createResponse);
        extractJson(createResponse);
    }
}
