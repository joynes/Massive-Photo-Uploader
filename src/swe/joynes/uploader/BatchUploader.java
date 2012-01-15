/*********************************************************************
 * Massive Photo Uploader: Upload a batch of albums to facebook
 * Copyright (C) 2010  Johannes Kählare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/
package swe.joynes.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import massivephotouploader.MassivePhotoUploaderView;
import org.apache.log4j.Logger;

import swe.joynes.preparator.Prepare;
import swe.joynes.preparator.PreparedAlbum;
import swe.joynes.preparator.PreparedPhoto;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.me.phillsacre.Constants;
import uk.me.phillsacre.ImageUtils;
import uk.me.phillsacre.PhotoImpl;
import uk.me.phillsacre.XMLUtils;
import com.google.code.facebookapi.*;

/**
 * Scans a directory for albums and uploads them on facebook
 * @author joynes
 *
 */
public class BatchUploader {

    private FacebookXmlRestClient facebookClient;
    private Prepare albums;
    private Logger log = Logger.getLogger(getClass());
    private JTextArea jTextArea;
    private JFrame mainFrame;
    private JProgressBar jProgressBar;
    private int picturesUploaded = 0;
    private int photoSize;

    /**
     * Inits the batch uploader 
     * @param facebookClient client initated and loggedin elsewhere
     * @param albums prepared structure with all album and photo information
     * @param jTextArea a GUI area to put information of what is happening
     * @param mainFrame a place to create POPUPS to display important messages
     * @throws java.lang.Exception
     */
    public BatchUploader(FacebookXmlRestClient facebookClient, Prepare albums, JTextArea jTextArea, JFrame mainFrame, JProgressBar jProgressBar) throws Exception {
        this.albums = albums;
        this.facebookClient = facebookClient;
        this.jTextArea = jTextArea;
        this.mainFrame = mainFrame;
        this.jProgressBar = jProgressBar;
        upload();
    }

    private void upload() {
        photoSize = albums.getPhotoSize();
        try {
            printInfo("Uploading");
            AlbumsHandler();

            jProgressBar.setValue(100);
            printInfo("Uploading finnished successfully!");
            alert("Uploading finnished successfully!");
            jProgressBar.setValue(0);
        } catch (Exception ex) {
            printInfo("Error: " + ex.getMessage());
            log.error("Here: " + ex, ex);

            String errorText = "";
            if (ex.getMessage().contains("Session key invalid")) {
                MassivePhotoUploaderView.properties.setProperty(Constants.Properties.SESSION_PERSISTENT_KEY, "");
                MassivePhotoUploaderView.properties.setProperty(Constants.Properties.SESSION_SECRET_KEY, "");
                alert("Session has expired, Please click Upload again to obtain a new one!");
            } else {
                alert("The albums was not successfully uploaded. \nTo recover from this login to facebook and remove the last created album because all photos might not be included. \nWhen done rerun this program to continue the synchronization!");
                alert("Error: " + errorText + ex.getMessage());
            }
        }
    }

    /**
     * Gets all the albums lying in a directory. If an album with that name does
     * not exist on facebook then it uploads the album. If the pictures are more
     * then 604x604 then they will resized to fit in that range 
     * (recommended by facebook)
     * 
     * If something goes wrong during any picture for an album then further 
     * processeing will be discarded.
     *
     */
    public void AlbumsHandler() throws Exception {
        log.info("BatchUploading");

        // get all existing albums to check if the album already exists
        List<String> existingAlbumNames = getUserAlbums();

        List<PreparedAlbum> nonExistingAlbums = new LinkedList<PreparedAlbum>();
        // iterate all albums and find out which to process
        for (PreparedAlbum preparedAlbum : albums.getAlbums()) {

            String albumName = preparedAlbum.getName();

            boolean isExisting = false;
            // check if the album already exists
            for (String existingAlbumName : existingAlbumNames) {
                if (existingAlbumName.equals((albumName))) {
                    isExisting = true;
                    log.info("Album " + albumName + " already exists so sync it!");
                    printInfo("Synchronize: Album " + albumName + " already exists so sync it!");
                    photoSize -= preparedAlbum.getSize();
                    preparedAlbum.setAlreadyExist(true);
                    nonExistingAlbums.add(preparedAlbum);
                }
            }

            if (isExisting == false) {
                preparedAlbum.setAlreadyExist(false);
                nonExistingAlbums.add(preparedAlbum);
            }

        }
        // handle all albums that didnt exist
        for (PreparedAlbum preparedAlbum : nonExistingAlbums) {
            albumHandler(preparedAlbum);
        }

    }

    /**
     * Resize all the photos into a temp path and add them to the album. 
     * Then upload the album
     * @param preparedAlbum
     * @throws IOException 
     * @throws InterruptedException 
     */
    private void albumHandler(PreparedAlbum preparedAlbum) throws IOException, InterruptedException, FacebookException, Exception {

        String tmpPath = System.getProperty("java.io.tmpdir");

        // Copy all the pictures to a temp folder and resize them
        log.info("Image tmp path is " + tmpPath);
        printInfo("Image tmp path is " + tmpPath);
        // Create the album if it was not already created

        Long albumId = null;
        if (preparedAlbum.getAlreadyExists()) {
            printInfo("The album " + preparedAlbum.getName() + " does already exists. Syncing files instead.");
        } else {
            // sleep a little before and after uploading.. seems that it was doing things to fast..
            printInfo("Creating Facebook album: " + preparedAlbum.getName());
            
            
            Document doc = facebookClient.photos_createAlbum(preparedAlbum.getName(), preparedAlbum.getDescription(), null);//, null,
                //    MassivePhotoUploaderView.properties.getProperty(Constants.Properties.VISIBILITY));

            NodeList nl = doc.getElementsByTagName("photos_createAlbum_response");
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                Node node = nl.item(i);
                Map<String, String> childData = XMLUtils.getChildValueMap(node);
                albumId = Long.valueOf(childData.get("aid"));
                printInfo("Created album with id " + albumId + " and visibility " + childData.get("visible") + ", link " + childData.get("link"));
            }
        }
        printInfo("Starting uploads!");

        if (albumId == null) {
            // get albumId
            albumId = getUserAlbumId(preparedAlbum.getName());
        }

        Set<String> existingImages = new HashSet<String>();

        // if album already existed.. then get all list of all the images in that album
        if (preparedAlbum.getAlreadyExists()) {
            printInfo("Get a list of existing photos...");
            existingImages = getAlbumPhotos(albumId);
        }


        // go through all photos and upload them
        for (PreparedPhoto photo : preparedAlbum.getPhotos()) {
            if (existingImages.contains(photo.getDescription())) {
                printInfo("Photo " + photo.getDescription() + " already exists so skipping it..");
            } else {

                String photoPath = photo.getPath();

                String resolutionString = MassivePhotoUploaderView.properties.getProperty(Constants.Properties.MAX_DIMENSION);
                if (!resolutionString.equals("None")) {

                    String tmpPhotoPath = tmpPath + "/" + photo.getDescription() + ".jpg";
                    copyfile(photoPath, tmpPhotoPath);

                    printInfo("Resizing: " + photo.getDescription());
                    int maxResolution = Integer.parseInt(MassivePhotoUploaderView.properties.getProperty(Constants.Properties.MAX_DIMENSION));
                    ImageUtils.resizeImage(new PhotoImpl(new File(tmpPhotoPath)), maxResolution, maxResolution);
                    photoPath = tmpPhotoPath;
                }

                printInfo("Upload: " + photo.getDescription());
                jProgressBar.setValue((int) (((float) picturesUploaded++ / (float) photoSize) * 100F));
                facebookClient.photos_upload(new File(photoPath), photo.getDescription(), String.valueOf(albumId));
            }
        }
    }

    /**
     * Copy one file to another location
     * @param srFile
     * @param dtFile
     * @throws IOException
     */
    private void copyfile(String srFile, String dtFile) throws IOException {
        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        InputStream in = new FileInputStream(f1);

        //For Overwrite the file.
        OutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Get all album names from current user in facebook. Pretty ugly!
     * @return
     */
    public List<String> getUserAlbums() throws FacebookException, IOException {
        
        List<String> albums = new LinkedList<String>();
        long userId = facebookClient.users_getLoggedInUser();
        
        Document doc = facebookClient.photos_getAlbums(userId);

        NodeList nl = doc.getElementsByTagName("album");
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nl.item(i);
            Map<String, String> childData = XMLUtils.getChildValueMap(node);
            albums.add(childData.get("name"));
        }

        return albums;
    }

    /**
     * Get all photo captions for the specific album
     * @param albumId
     * @return
     * @throws FacebookException
     * @throws IOException
     */
    private Set<String> getAlbumPhotos(Long albumId) throws FacebookException, IOException {

        Set<String> set = new HashSet<String>();
        Document doc = facebookClient.photos_get(albumId);
        NodeList nl = doc.getElementsByTagName("photo");
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nl.item(i);
            Map<String, String> childData = XMLUtils.getChildValueMap(node);
            set.add(childData.get("caption"));
        }
        return set;
    }

    /**
     * Get Id of the album with that name. This is an ugly way to do it but Im
     * short of time
     * @return
     */
    public long getUserAlbumId(String albumname) throws FacebookException, IOException {
        List<String> albums = new LinkedList<String>();
        
        long userId = facebookClient.users_getLoggedInUser();
        Document doc = facebookClient.photos_getAlbums(userId);

        NodeList nl = doc.getElementsByTagName("album");
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nl.item(i);
            Map<String, String> childData = XMLUtils.getChildValueMap(node);

            if (childData.get("name").equals(albumname)) {
                return Long.parseLong(childData.get("aid"));
            }
        }
        throw new RuntimeException("Could not find album " + albumname
                    + "though it should been created");
        

    }

    /**
     * Starts a thread that uploads all albums and can at the same time write
     * information to the GUI. 
     */
    public void run() {
        try {
            printInfo("Uploading");
            AlbumsHandler();

            printInfo("Uploading finnished successfully!");
            alert("Uploading finnished successfully!");
        } catch (Exception ex) {
            printInfo("Error: " + ex.getMessage());
            log.error("Here: " + ex);

            String errorText = "";
            if (ex.getMessage().contains("Session key invalid")) {
                MassivePhotoUploaderView.properties.setProperty(Constants.Properties.SESSION_PERSISTENT_KEY, "");
                MassivePhotoUploaderView.properties.setProperty(Constants.Properties.SESSION_SECRET_KEY, "");
                alert("Resetting sessionkey, Please click upload again!");
            } else {
                alert("Error: " + errorText + ex.getMessage());
            }
        }
    }

    /**
     * POPUP a message alert
     * @param str
     */
    public void alert(String str) {
        JOptionPane.showMessageDialog(mainFrame, str);
    }

    /**
     * Print info to the gui, scroll down the textarea and do normal log.
     * @param str
     */
    public void printInfo(String str) {
        jTextArea.setText(jTextArea.getText() + "\n" + str);
        jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
        log.info(str);
    }
}
