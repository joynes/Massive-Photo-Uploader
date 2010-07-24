package swe.joynes.preparator;
import java.util.List;

import org.apache.log4j.Logger;


public class PreparedAlbum {

	private String albumName;
	private String description;
	private Boolean alreadyExists;
        private List<PreparedPhoto> photos;
	
	private Logger _log = Logger.getLogger(getClass());
	
	public PreparedAlbum(String replace, String description2, List<PreparedPhoto> files) {
		albumName = replace;
		photos = files;
		description = description2;
	}

	/**
	 * Print info about this album
	 */
	public String print()
	{
		StringBuffer str = new StringBuffer();
		str.append("\n------------------------------------");
		for (PreparedPhoto photo : photos)
		{
			str.append(photo.getDescription() + ": " + photo.getPath() + "\n");
		}
		str.append("------------------------------------\n");
		return str.toString();
		
	}

    public String getName() {
        return albumName;
    }

    public List<PreparedPhoto> getPhotos() {
        return photos;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSize() {
        return photos.size();
    }

    public void setAlreadyExist(Boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    public boolean getAlreadyExists() {
        return alreadyExists;
    }

}
