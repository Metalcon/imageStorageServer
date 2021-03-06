package de.metalcon.imageServer.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.imageStorageServer.ImageStorageServerConfig;
import de.metalcon.imageStorageServer.protocol.ProtocolConstants;
import de.metalcon.imageStorageServer.protocol.create.CreateRequest;
import de.metalcon.imageStorageServer.protocol.create.CreateResponse;
import de.metalcon.utils.formItemList.FormItemList;

public class CreateRequestTest extends RequestTest {

    private static final String VALID_AUTOROTATE_FLAG_TRUE = "1";

    private static final String VALID_AUTOROTATE_FLAG_FALSE = "0";

    private static final String MALFORMED_AUTOROTATE_FLAG = "wrong";

    private static final String CONFIG_PATH = "src/main/resources/test.config";

    private static File DISK_FILE_REPOSITORY;

    private static FileItem VALID_IMAGE_ITEM_JPEG;

    private static String VALID_CREATE_META_DATA;

    private CreateRequest createRequest;

    @BeforeClass
    public static void beforeClass() {
        final ImageStorageServerConfig config =
                new ImageStorageServerConfig(CONFIG_PATH);
        DISK_FILE_REPOSITORY = new File(config.getTemporaryDirectory());
    }

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws IOException {
        DISK_FILE_REPOSITORY.delete();
        DISK_FILE_REPOSITORY.mkdirs();

        // JPEG image item
        final File imageItemJpeg = new File("src/main/resources/test.jpeg");
        VALID_IMAGE_ITEM_JPEG = createImageItem("image/jpeg", imageItemJpeg);
        assertEquals(imageItemJpeg.length(), VALID_IMAGE_ITEM_JPEG.getSize());

        // meta data
        final JSONObject metaDataCreate = new JSONObject();
        metaDataCreate.put("title", "My Great Picture");
        metaDataCreate.put("camera", "Testy BFC9000");
        metaDataCreate.put("resolution", "yes");
        metaDataCreate.put("license", "General More AllYouCanSee License");
        metaDataCreate.put("date", "1991-11-11");
        metaDataCreate.put("description", "Pixels in a frame!");
        VALID_CREATE_META_DATA = metaDataCreate.toJSONString();
    }

    @After
    public void tearDown() {
        DISK_FILE_REPOSITORY.delete();
    }

    @Test
    public void testCreateRequest() throws IOException {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG,
                VALID_CREATE_META_DATA, VALID_AUTOROTATE_FLAG_FALSE);
        assertNotNull(createRequest);
        assertEquals(VALID_IDENTIFIER, createRequest.getImageIdentifier());
        assertTrue(compareInputStreams(VALID_IMAGE_ITEM_JPEG.getInputStream(),
                createRequest.getImageStream()));
        assertEquals(VALID_CREATE_META_DATA, createRequest.getMetaData());
        assertEquals(false, createRequest.isAutoRotateFlag());
    }

    @Test
    public void testCreateRequestAutoRotate() throws IOException {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG,
                VALID_CREATE_META_DATA, VALID_AUTOROTATE_FLAG_TRUE);
        assertNotNull(createRequest);
        assertEquals(VALID_IDENTIFIER, createRequest.getImageIdentifier());
        assertTrue(compareInputStreams(VALID_IMAGE_ITEM_JPEG.getInputStream(),
                createRequest.getImageStream()));
        assertEquals(VALID_CREATE_META_DATA, createRequest.getMetaData());
        assertEquals(true, createRequest.isAutoRotateFlag());
    }

    @Test
    public void testImageIdentifierMissing() {
        fillRequest(null, VALID_IMAGE_ITEM_JPEG,
                ProtocolTestConstants.VALID_IMAGE_METADATA,
                VALID_AUTOROTATE_FLAG_TRUE);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Create.IMAGE_IDENTIFIER);
        assertNull(createRequest);
    }

    @Test
    public void testImageStreamMissing() {
        fillRequest(VALID_IDENTIFIER, null,
                ProtocolTestConstants.VALID_IMAGE_METADATA,
                VALID_AUTOROTATE_FLAG_TRUE);
        checkForMissingParameterMessage(ProtocolConstants.Parameters.Create.IMAGE_STREAM);
        assertNull(createRequest);
    }

    @Test
    public void testAutorotateFlagMissing() throws IOException {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG,
                VALID_CREATE_META_DATA, null);
        assertNotNull(createRequest);
        assertEquals(false, createRequest.isAutoRotateFlag());
    }

    @Test
    public void testAutorotateFlagMalformed() {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG,
                VALID_CREATE_META_DATA, MALFORMED_AUTOROTATE_FLAG);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Create.AUTOROTATE_FLAG_MALFORMED);
        assertNull(createRequest);
    }

    @Test
    public void testImageMetadataMissing() {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG, null,
                VALID_AUTOROTATE_FLAG_TRUE);
        assertNotNull(createRequest);
        assertNull(createRequest.getMetaData());
    }

    @Test
    public void testImageMetadataMalformed() {
        fillRequest(VALID_IDENTIFIER, VALID_IMAGE_ITEM_JPEG,
                ProtocolTestConstants.MALFORMED_IMAGE_METADATA,
                VALID_AUTOROTATE_FLAG_TRUE);
        checkForStatusMessage(ProtocolConstants.StatusMessage.Create.META_DATA_MALFORMED);
        assertNull(createRequest);
    }

    private void fillRequest(
            final String imageIdentifier,
            final FileItem imageItem,
            final String metaData,
            final String autoRotateFlag) {
        // create and fill form item list
        final FormItemList formItemList = new FormItemList();

        if (imageIdentifier != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Create.IMAGE_IDENTIFIER,
                    imageIdentifier);
        }
        if (imageItem != null) {
            formItemList.addFile(
                    ProtocolConstants.Parameters.Create.IMAGE_ITEM, imageItem);
        }
        if (metaData != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Create.META_DATA, metaData);
        }
        if (autoRotateFlag != null) {
            formItemList.addField(
                    ProtocolConstants.Parameters.Create.AUTOROTATE_FLAG,
                    autoRotateFlag);
        }

        // check request and extract the response
        final CreateResponse createResponse = new CreateResponse();
        createRequest =
                CreateRequest.checkRequest(formItemList, createResponse);
        extractJson(createResponse);
    }

    /**
     * create an image item
     * 
     * @param contentType
     *            content type of the music file
     * @param musicFile
     *            file handle to the music file
     * @return music item representing the music file passed
     */
    private static FileItem createImageItem(
            final String contentType,
            final File imageFile) {
        final FileItem imageItem =
                new DiskFileItem(
                        ProtocolConstants.Parameters.Create.IMAGE_ITEM,
                        contentType, false, imageFile.getName(),
                        (int) imageFile.length(), DISK_FILE_REPOSITORY);

        // reason for call of getOutputStream: bug in commons.IO
        // called anyway to create file
        try {
            final OutputStream outputStream = imageItem.getOutputStream();
            final InputStream inputStream = new FileInputStream(imageFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (final IOException e) {
            e.printStackTrace();
            fail("image item creation failed!");
        }

        return imageItem;
    }

    /**
     * compare two input streams
     * 
     * @param stream1
     *            first input stream
     * @param stream2
     *            second input stream
     * @return true - if the two streams does contain the same content<br>
     *         false - otherwise
     * @throws IOException
     *             if IO errors occurred
     */
    private static boolean compareInputStreams(
            final InputStream stream1,
            final InputStream stream2) throws IOException {
        final ReadableByteChannel channel1 = Channels.newChannel(stream1);
        final ReadableByteChannel channel2 = Channels.newChannel(stream2);
        final ByteBuffer buffer1 = ByteBuffer.allocateDirect(4096);
        final ByteBuffer buffer2 = ByteBuffer.allocateDirect(4096);

        try {
            while (true) {

                int n1 = channel1.read(buffer1);
                int n2 = channel2.read(buffer2);

                if ((n1 == -1) || (n2 == -1)) {
                    return n1 == n2;
                }

                buffer1.flip();
                buffer2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++) {
                    if (buffer1.get() != buffer2.get()) {
                        return false;
                    }
                }

                buffer1.compact();
                buffer2.compact();
            }

        } finally {
            if (stream1 != null) {
                stream1.close();
            }
            if (stream2 != null) {
                stream2.close();
            }
        }
    }
}
