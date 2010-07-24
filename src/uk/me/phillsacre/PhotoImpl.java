package uk.me.phillsacre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;



import com.facebook.api.PhotoTag;

/**
 * Implementation of Photo to use the local hard disk.
 * 
 * @author psacre
 * 
 */
public class PhotoImpl implements Photo
{
	private static final long serialVersionUID = 20070615L;

	private static FileSystemView _fileSystemView = FileSystemView
			.getFileSystemView();
	private File _photo;
	private List<PhotoTag> _tags;
	private String _caption;

	public PhotoImpl(File photo)
	{
		_photo = photo;
		_tags = new ArrayList<PhotoTag>();

		
	}

	public String getName()
	{
		return _photo.getName();
	}
	
	public String getPath()
	{
		try
		{
			return _photo.getCanonicalPath();
		}
		catch (IOException e)
		{
			throw new UploaderException("Could not get path", e);
		}
	}

	public Icon getDisplayIcon()
	{
		return _fileSystemView.getSystemIcon(_photo);
	}

	public InputStream getInputStream() throws IOException
	{
		return new FileInputStream(_photo);
	}

	public OutputStream getOutputStream() throws IOException
	{
		return new FileOutputStream(_photo);
	}

	public long getSize()
	{
		return _photo.length();
	}

	public List<PhotoTag> getTags()
	{
		return _tags;
	}
	
	public void removeTag(PhotoTag tag)
	{
		_tags.remove(tag);
	}

	public void addTag(PhotoTag tag)
	{
		_tags.add(tag);
	}

	public String toString()
	{
		return getPath();
	}

	public boolean equals(Object photo)
	{
		if (photo instanceof Photo)
		{
			return ((Photo) photo).getPath().equals(getPath());
		}

		return false;
	}
	
	public String getCaption()
	{
		return _caption;
	}
	
	public void setCaption(String caption)
	{
		_caption = caption;
	}
}
