package uk.me.phillsacre;

import com.sun.xml.internal.xsom.impl.FacetImpl;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import com.google.code.facebookapi.*;


/**
 * Wrapper class for the Facebook XML Rest Client to try and fix some of the
 * problems with it. This class is designed to work with the Facebook client in
 * order that the Facebook Client does not need any modifications. Obviously if
 * they make any big changes, this class will probably need to be modified.
 * However, using this rather than the base Facebook Rest Client will hopefully
 * provide some degree of abstraction to changes in the main Facebook Client and
 * mean less effort in maintaining it.
 * 
 * @author psacre
 * 
 */
public class WorkingFacebookRestClient extends FacebookXmlRestClient
{
	private static Logger _log = Logger
			.getLogger(WorkingFacebookRestClient.class);

	//private Photo _uploadPhoto;
	//private UploadListener _uploadListener;

	public WorkingFacebookRestClient(String apiKey, String secret)
	{
		super(apiKey, secret);
	}

	public WorkingFacebookRestClient(String apiKey, String secret,
			String persistentKey)
	{
		super(apiKey, secret, persistentKey);
	}

	/**
	 * Retrieve the session key.
	 * 
	 * @return
	 */
	public String getSessionKey()
	{
            return "null";
	}

	/**
	 * Retrieve the session secret (necessary in order not to receive 'invalid
	 * signature' errors)
	 * 
	 * @return
	 */
	public String getSessionSecret()
	{
		return "null";
	}

	/**
	 * Set the session secret
	 * 
	 * @param sessionSecret
	 */
	public void setSessionSecret(String sessionSecret)
	{
		//_sessionSecret = sessionSecret;
	}

	/**
	 * Sets the photo to upload
	 * 
	 * @param photo
	 */
	/*
	public void setUploadPhoto(Photo photo)
	{
		_uploadPhoto = photo;
	}
	*/

	/**
	 * Get the expiry date of the session as a UNIX time. A value of 0 means the
	 * session will never expire.
	 * 
	 * @return
	 */
	public String getSessionExpires()
	{
		// joynes
		//return _sessionExpires;
		return "0";
	}

	/**
	 * Uploads a photo to Facebook
	 * 
	 * @param photo
	 * @param albumId
	 * @return
	 */
	/*
	public Document photos_upload(Photo photo, Long albumId,
			UploadListener uploadListener) throws IOException,
			FacebookException
	{
		_uploadPhoto = photo;
		_uploadListener = uploadListener;

		if (null != photo.getCaption())
		{
			_log.debug(String.format("Photo caption is [%s]", photo
					.getCaption()));
		}

		return super.photos_upload(null, photo.getCaption(), albumId);
	}
	*/

	/*
	@Override
	protected InputStream postFileRequest(String methodName,
			Map<String, CharSequence> params) throws IOException
	{
		InputStream input = postFileRequest(methodName, params, _uploadListener);

		_uploadListener = null;

		return input;
	}
*/
	/**
	 * Implementation of postFileRequest which uses an upload listener.
	 * 
	 * @param methodName
	 * @param params
	 * @param uploadListener
	 * @return
	 * @throws IOException
	 */
	/*
	protected InputStream postFileRequest(String methodName,
			Map<String, CharSequence> params, UploadListener uploadListener)
			throws IOException
	{
		assert (null != _uploadPhoto);

		HttpURLConnection con;

		try
		{
			BufferedInputStream bufin = new BufferedInputStream(_uploadPhoto
					.getInputStream());

			String boundary = Long.toString(System.currentTimeMillis(), 16);
			con = (HttpURLConnection) SERVER_URL.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("MIME-version", "1.0");

			// Buffer the content internally so we know what the content length
			// will be
			ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(outBuffer);

			for (Map.Entry<String, CharSequence> entry : params.entrySet())
			{
				out.writeBytes(PREF + boundary + CRLF);
				out.writeBytes("Content-disposition: form-data; name=\""
						+ entry.getKey() + "\"");
				out.writeBytes(CRLF + CRLF);
				out.writeBytes(entry.getValue().toString());
				out.writeBytes(CRLF);
			}

			out.writeBytes(PREF + boundary + CRLF);
			out.writeBytes("Content-disposition: form-data; filename=\""
					+ _uploadPhoto.getName() + "\"" + CRLF);
			out.writeBytes("Content-Type: image/jpeg" + CRLF);

			out.writeBytes(CRLF);

			byte[] buffer = outBuffer.toByteArray();
			int bytesTotal = buffer.length + (int) _uploadPhoto.getSize()
					+ (CRLF + PREF + boundary + PREF + CRLF).getBytes().length;
			_log.debug("Total bytes to send: " + bytesTotal);

			con.setFixedLengthStreamingMode(bytesTotal);
			con.connect();

			out = new DataOutputStream(con.getOutputStream());
			outBuffer.writeTo(out);

			byte b[] = new byte[UPLOAD_BUFFER_SIZE];
			int byteCounter = 0;
			int i;
			while (-1 != (i = bufin.read(b)))
			{
				byteCounter += i;
				out.write(b, 0, i);
				out.flush();

				if (null != uploadListener)
				{
					try
					{
						uploadListener.handleBytesUpload(
								_uploadPhoto.getName(), _uploadPhoto.getSize(),
								i);
					}
					catch (UploadCancelledException e)
					{
						_log
								.debug("Upload cancel request received -- cancelling");
						bufin.close();
						out.close();
						return null;
					}
				}
			}
			out.writeBytes(CRLF + PREF + boundary + PREF + CRLF);

			out.close();
			bufin.close();

			InputStream is = con.getInputStream();
			return is;
		}
		catch (Exception e)
		{
			_log.error("Error while posting file request", e);
			return null;
		}
		finally
		{
			_uploadPhoto = null;
		}
	}
	*/
}
