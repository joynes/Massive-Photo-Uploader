package uk.me.phillsacre;


/**
 * Interface to specify an upload listener.
 * 
 * @author psacre
 * 
 */
public interface UploadListener
{
	/**
	 * Processes the number of bytes uploaded.
	 * 
	 * @param byteCount
	 *            The number of bytes uploaded since the last time this was
	 *            called
	 * @param total
	 *            The total number of bytes
	 * @param currentFile
	 *            The name of the file currently being uploaded
	 * @throws UploadCancelledException -
	 *             classes implementing this should throw this exception if the
	 *             upload has been cancelled. Classes using UploadListeners
	 *             should handle this exception and cancel the upload
	 *             accordingly.
	 */
	public void handleBytesUpload(String currentFile, long total, long byteCount)
			throws UploadCancelledException;
	
	/**
	 * This is called when the upload is complete.
	 *
	 */
	public void handleUploadComplete();
}
