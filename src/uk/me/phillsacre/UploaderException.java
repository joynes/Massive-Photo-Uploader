package uk.me.phillsacre;

/**
 * Generic base class for excpetions caught by the Uploader application.
 * 
 * @author phill
 * 
 */
public class UploaderException extends RuntimeException
{
	private static final long serialVersionUID = 20070529L;
	
	public UploaderException(String msg)
	{
		super(msg);
	}

	public UploaderException(Throwable th)
	{
		super(th);
	}

	public UploaderException(String msg, Throwable th)
	{
		super(msg, th);
	}
}
