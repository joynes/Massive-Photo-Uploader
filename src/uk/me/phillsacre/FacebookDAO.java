package uk.me.phillsacre;

import java.util.List;


/**
 * Interface to define the Facebook DAO
 * 
 * @author phill
 * 
 */
public interface FacebookDAO
{
	/**
	 * Create an authorisation token for Facebook.
	 */
	public String getAuthorisationToken();

	/**
	 * Creates the authenticated session (this will require the user to have
	 * logged in on the Facebook website).
	 */
	public void getAuthenticatedSession();

	/**
	 * Creates an album.
	 * 
	 * @param name
	 * @param description
	 * @param location
	 */
	public void createAlbum(String name, String description, String location);

	/**
	 * Retrieve a list of the current user's albums.
	 * 
	 * @return
	 */
	public List<AlbumVO> getUserAlbums();

	/**
	 * Retrieve a photo by ID.
	 * 
	 * @param id
	 * @return
	 */
	public PhotoVO getPhoto(Long id);

	/**
	 * Uploads a list of files to the specified album.
	 * 
	 * @param album
	 *            The album to upload to
	 * @param fileList
	 *            The list of files
	 * @param uploadListener
	 *            The upload listener
	 */
	public void uploadPhotos(AlbumVO album, List<Photo> photoList,
			UploadListener uploadListener, ExceptionHandler exceptionHandler);

	/**
	 * Uploads a single file to a specified album.
	 * 
	 * @param album
	 * @param photo
	 * @param uploadListener
	 */
	public void uploadPhoto(AlbumVO album, Photo photo,
			UploadListener uploadListener);

	/**
	 * If there is an upload currently in progress, this should cleanly stop the
	 * upload.
	 * 
	 */
	public void cancelUpload();

	/**
	 * Retrieves a list of friends for the currently logged-on user.
	 * 
	 * @return A List of Friends.
	 */
	public List<UserVO> getFriends();
	
	/**
	 * Gets the info for the currently logged in user.
	 * 
	 * @return
	 */
	public UserVO getCurrentUserInfo();
}
