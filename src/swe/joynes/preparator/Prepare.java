package swe.joynes.preparator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import uk.me.phillsacre.Constants;
import uk.me.phillsacre.PropertyUtils;

/**
 * Scan harddrive for information about possible albums and setup all data for the albums
 * @author joynes
 *
 */
public class Prepare {

    List<String> folders = new LinkedList<String>();
    public List<PreparedAlbum> albums = new LinkedList<PreparedAlbum>();
    private Logger _log = Logger.getLogger(getClass());
    
    private PropertyUtils _props = new PropertyUtils();
    
    int IMAGE_MINIMUM_SIZE;
    String delim;
    
    public int photoSize;
          

    /**
     * Prepare all albums in the subdirectories. If a subdirectory includes pictures (JPG or jpg) then consider the
     * subdirectory an album and set the name bases on the directory names.. like <Uppsala>_<2006>_<02-03> where each tag
     * is a directoryname down to the subdirectory. If it exists a data.txt file in the album-directory then the contents will
     * be used as description for the album. Description of the pictures will be the name of the pictures.
     * 
     * If a folder consists of more then 60 pictures it will be devided into serveral albums with a number appended to the name like:
     * Uppsala_2006_02-03__1
     * Uppsala_2006_02-03__2
     * 
     * and so on...
     * Folders and the data.txt can include non ASCII characters but filenames cannot be handled by the Facebook api right now.
     * 
     * @param path
     */
    public Prepare(String path) throws Exception {

        Integer albumMaxImages = Integer.valueOf(_props.getProperty(Constants.Properties.ALBUM_MAX_IMAGES));
        // get config data
        IMAGE_MINIMUM_SIZE = Integer.parseInt(_props.getProperty(Constants.Properties.IMAGE_MINIMUM_SIZE));
        delim = _props.getProperty(Constants.Properties.ALBUM_NAME_DELIMITER);
        
        visitAllDirs(new File(path));
        // look in each dir and find files
        for (String folder : folders) {
            List<PreparedPhoto> files = getFiles(folder);

            if (files.size() > 0) {
                // check for description in data-file
                String description = null;
                String albumName = folder.replace(path, "").replace("\\", delim).replaceFirst("^"+ delim, "");
                try {
                    description = readTextFile(folder + "/data.txt");
                } catch (IOException e) {
                    _log.info("No description for " + albumName);
                }

                int i = 0;
                List<PreparedPhoto> limitesPhotos = new LinkedList<PreparedPhoto>();
                // create albums only for a certain amount of files
                for (PreparedPhoto preparedPhoto : files) {
                    limitesPhotos.add(preparedPhoto);
                    i++;
                if (i % albumMaxImages == 0) {
                        albums.add(new PreparedAlbum(getAlbumName(albumName,i), description, new LinkedList<PreparedPhoto>(limitesPhotos)));
                        photoSize += albumMaxImages;
                        limitesPhotos.clear();
                    }

                }
                // add the last album which will be less then the max nr of photos or 0
                if (limitesPhotos.size() > 0) {
                    // if there is only one album in total then dont use an index number
                    int index = (i / albumMaxImages + 1);
                    String indexStr = "";
                    if (index != 1) {
                        indexStr = delim + delim + index;
                    }
                    if (albumName == null || albumName.equals(""))
                        throw new Exception("Directory name invalid, make sure the dir you choosed has subdir with albums!");
                    albums.add(new PreparedAlbum(albumName + indexStr, description, new LinkedList<PreparedPhoto>(limitesPhotos)));
                    photoSize += limitesPhotos.size();
                }
            }
        }
    //print(albums);

    }

    private List<PreparedPhoto> getFiles(String folder) {
        List<PreparedPhoto> files = new LinkedList<PreparedPhoto>();
        File dir = new File(folder);
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            String fileName = dir + "/" + children[i];
            File file = new File(fileName);
            if (file.isFile() && (fileName.endsWith(".JPG") || fileName.endsWith(".jpg")
                    || fileName.endsWith(".gif") || fileName.endsWith(".GIF")) && (file.length() / 1000 > IMAGE_MINIMUM_SIZE)) {
                files.add(new PreparedPhoto(children[i].replaceFirst("\\....$", ""), fileName));
            }
        }
        return files;
    }

    /**
     * Tester of the application
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws Exception {
        //new Prepare("G:\\johannes\\source\\programming\\workspace\\javaFacebookPhotoUploader\\scripts\\pics");
        Prepare test = new Prepare("G:\\johannes\\release\\pics\\uppsala");

        test.print();
    }

    /**
     * Process only directories under dir. Used to get all subfolders, which are possible albums
     */
    public void visitAllDirs(File dir) {
        if (dir.isDirectory()) {
            folders.add(dir.getAbsolutePath());

            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                visitAllDirs(new File(dir, children[i]));
            }
        }
    }

    /**
     * Put contents of a file into a string
     * @param fullPathFilename
     * @return
     * @throws IOException
     */
    public String readTextFile(String fullPathFilename) throws IOException {
        StringBuffer result = new StringBuffer();
        // Open the file that is the first 
        // command line parameter
        FileInputStream fstream = new FileInputStream(fullPathFilename);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            result.append(strLine + "\n");
        }
        //Close the input stream
        in.close();

        return result.toString();
    }

    /**
     * Print information about the albums 
     */
    public String print() {
        StringBuffer str = new StringBuffer();
        for (PreparedAlbum album : albums) {
            str.append("##########################\n");
            str.append("Album name: " + album.getName());
            str.append("\n--------------------------\n");
            str.append("Album description: " + ((album.getDescription() != null)? album.getDescription() : "None"));
            str.append("\n--------------------------\n");
            str.append("Album images: \n");
            List<PreparedPhoto> photos = album.getPhotos();

            int imageCounter = 0;
            for (PreparedPhoto preparedPhoto : photos) {
                str.append(preparedPhoto.getDescription());
                imageCounter++;
                if (imageCounter % 1 == 0) {
                    str.append("\n");
                }
                else {
                    str.append(", ");
                }
            }
            str.append("##########################\n\n");
        }
        return str.toString();
    }
    
    private String getAlbumName(String albumName, int i) throws Exception {
        String name = albumName + delim + delim + (i / Integer.valueOf(_props.getProperty(Constants.Properties.ALBUM_MAX_IMAGES)));
        if (name == null || name.equals(""))
            throw new Exception("Directory name invalid, make sure the dir you choosed has subdir with albums!");
        return name;
    }
}
