package uk.me.phillsacre;


import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


/**
 * Utilities class for handling properties. This will load the properties from
 * the uploader.properties file, and also handle the saving back of properties
 * if need be. This class uses <a
 * href="http://jakarta.apache.org/commons/configuration">Commons Configuration</a>.
 * 
 * @author psacre
 * 
 */
public class PropertyUtils
{
	private static PropertiesConfiguration _configuration;
	private static File _configFile;
	private static Logger _log = Logger.getLogger(PropertyUtils.class);

	static
	{
		_configuration = new PropertiesConfiguration();
		_configuration.setDelimiterParsingDisabled(true);

		try
		{
			// First, attempt to load the properties file from the user.home
			File f = new File(System.getProperty("user.home") + File.separator
					+ ".fb-uploader" + File.separator + "uploader.properties");

			if (f.exists() && f.isFile())
			{
				_log.debug("Using properties file from user home: [" + f.getAbsolutePath() + "]");
				_configFile = f;
			}
			else
			{
				_log.debug("Using properties file from ClassPath");
				_configFile = new File(PropertyUtils.class.getClassLoader()
						.getResource("uploader.properties").toURI());
			}

			_configuration.load(_configFile);
		}
		catch (Exception e)
		{
			throw new UploaderException("Could not load properties", e);
		}
	}

	/**
	 * Gets the property with the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		return _configuration.getString(name);
	}

	/**
	 * Gets the property with the specified name. If it does not exist, the
	 * default will be returned instead.
	 * 
	 * @param name
	 * @param def
	 * @return
	 */
	public String getProperty(String name, String def)
	{
		return _configuration.getString(name, def);
	}

	/**
	 * Sets the property with the specified name to the specified value. Please
	 * note that this will save the properties back, i.e. perform a filesystem
	 * write.
	 * 
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, String value)
	{
		_configuration.setProperty(name, value);
		try
		{
			_configuration.save(_configFile);
		}
		catch (Exception e)
		{
			throw new UploaderException("Could not save properties", e);
		}
	}
}
