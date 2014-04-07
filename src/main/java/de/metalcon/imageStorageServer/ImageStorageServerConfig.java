package de.metalcon.imageStorageServer;

import de.metalcon.utils.Config;

public class ImageStorageServerConfig extends Config {

    private static final long serialVersionUID = 3422972336410861945L;

    /**
     * root directory for the image storage server
     */
    public String image_directory;

    /**
     * temporary directory for image magic
     */
    public String temporary_directory;

    /**
     * host address of the server the meta database runs at
     */
    public String metaDatabase_host;

    /**
     * port to connect to the meta database
     */
    public int metaDatabase_port;

    /**
     * name of the meta database used
     */
    public String metaDatabase_name;

    /**
     * load image storage server configuration
     * 
     * @param configPath
     *            path to configuration file
     */
    public ImageStorageServerConfig(
            String configPath) {
        super(configPath);
    }

    /**
     * @return root directory for the image storage server
     */
    public String getImageDirectory() {
        return image_directory;
    }

    /**
     * @return temporary directory for image magic
     */
    public String getTemporaryDirectory() {
        return temporary_directory;
    }

    /**
     * @return host address of the server the meta database runs at
     */
    public String getMetaDatabaseHost() {
        return metaDatabase_host;
    }

    /**
     * @return port to connect to the meta database
     */
    public int getMetaDatabasePort() {
        return metaDatabase_port;
    }

    /**
     * @return name of the meta database used
     */
    public String getMetaDatabaseName() {
        return metaDatabase_name;
    }

}
