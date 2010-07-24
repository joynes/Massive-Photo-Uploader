package uk.me.phillsacre;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.swing.Icon;

import com.facebook.api.PhotoTag;

/**
 * Interface to define a Photo in the file system.
 * 
 * @author psacre
 * 
 */
public interface Photo extends Serializable
{
	/**
	 * Retrieve the name of this photo.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Retrieve the path of this photo.
	 * 
	 * @return
	 */
	public String getPath();

	/**
	 * Gets the Icon for this photo
	 * 
	 * @return
	 */
	public Icon getDisplayIcon();

	/**
	 * Retrieve the size of this photo, in bytes.
	 * 
	 * @return
	 */
	public long getSize();

	/**
	 * Retrieve the contents of this photo as an InputStream.
	 * 
	 * @return
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * Retrieve the output stream for this photo (i.e., to overwrite the
	 * contents).
	 * 
	 * @return
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;

	/**
	 * Adds a tag to this photo.
	 * 
	 * @param tag
	 */
	public void addTag(PhotoTag tag);

	/**
	 * Removes a tag from this photo.
	 * 
	 * @param tag
	 */
	public void removeTag(PhotoTag tag);

	/**
	 * Retrieves a list of Tags associated with this photo.
	 * 
	 * @return A List of Tags. If no tags are associated, this should return an
	 *         empty list (never null)
	 */
	public List<PhotoTag> getTags();

	/**
	 * Retrieve the caption for this photo.
	 * 
	 * @return
	 */
	public String getCaption();

	/**
	 * Sets the caption for this photo.
	 * 
	 * @param caption
	 *            The caption
	 */
	public void setCaption(String caption);
}
