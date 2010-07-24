package uk.me.phillsacre;

/**
 * Exception which gets thrown when an upload is cancelled.
 * 
 * @author psacre
 *
 */
public class UploadCancelledException extends UploaderException
{
	private static final long serialVersionUID = 20070604L;
	
	public UploadCancelledException()
	{
		super("Upload cancelled");
	}
}
