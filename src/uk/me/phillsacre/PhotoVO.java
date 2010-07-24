package uk.me.phillsacre;

/**
 * Interface to define a Photo value object.
 * 
 * @author psacre
 * 
 */
public interface PhotoVO
{
	/**
	 * Retrieves the ID of this album.
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Retrieves the URL to this photo.
	 * 
	 * @return
	 */
	public String getUrl();

	/**
	 * Retrieves the URL to the big version of this photo.
	 * 
	 * @return
	 */
	public String getUrlBig();

	/**
	 * Retrieves the URL to the small version of this photo.
	 * 
	 * @return
	 */
	public String getUrlSmall();
}
