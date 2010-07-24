package swe.joynes.preparator;

public class PreparedPhoto {

	private String description;
	private String path;
	
	public PreparedPhoto(String replaceFirst, String fileName) {
		description = replaceFirst;
		path = fileName; 
	}

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

}
