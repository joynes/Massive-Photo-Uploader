package uk.me.phillsacre;


/**
 * Interface to define an exception handler.
 * 
 * @author psacre
 * 
 */
public interface ExceptionHandler
{
	/**
	 * Handles an uncaught exception. This should response appropriately by
	 * displaying a message to the user, etc.
	 * 
	 * @param e
	 */
	public void handleUncaughtException(UploaderException e);
}
