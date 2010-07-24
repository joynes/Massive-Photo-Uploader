package uk.me.phillsacre;


/**
 * Interface to define an Album.
 * 
 * @author psacre
 * 
 */
public interface AlbumVO extends Comparable<AlbumVO>
{
	/**
	 * Retrieves the cover photo for this album.
	 * 
	 * @return
	 */
	public PhotoVO getCoverPhoto();

	/**
	 * Retrieves the description of this album.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Retrieves the ID of this album.
	 * 
	 * @return
	 */
	public Long getId();

	/**
	 * Retrieves the location of this album.
	 * 
	 * @return
	 */
	public String getLocation();

	/**
	 * Retrieves the name of this album.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Retrieves the web URL of this album.
	 * 
	 * @return
	 */
	public String getLink();
}
