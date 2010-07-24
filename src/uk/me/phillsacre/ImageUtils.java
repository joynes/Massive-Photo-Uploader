package uk.me.phillsacre;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

/**
 * Utilities class for images
 * 
 * @author psacre
 * 
 */
public class ImageUtils
{
	private static Logger _log = Logger.getLogger(ImageUtils.class);
	private static GraphicsConfiguration _graphicsConfiguration = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	public static final int ROTATE_RIGHT = 1;
	public static final int ROTATE_LEFT = 3;

	/**
	 * Loads an image given a File object.
	 * 
	 * @param image
	 *            The image to load
	 * @return The Buffered Image
	 */
	public static BufferedImage loadImage(File image)
	{
		_log.debug("Loading image: " + image);

		BufferedImage bufferedImage = null;

		try
		{
			ImageInputStream imageInputStream = ImageIO
					.createImageInputStream(image);
			ImageReader reader = ImageIO.getImageReaders(imageInputStream)
					.next();
			reader.setInput(imageInputStream);
			bufferedImage = reader.read(0);
		}
		catch (IOException e)
		{
			_log.error("Could not load image", e);
			throw new UploaderException("Could not load image", e);
		}

		return bufferedImage;
	}

	/**
	 * Loads a BufferedImage from a Photo.
	 * 
	 * @param photo
	 * @return
	 */
	public static BufferedImage loadImage(Photo photo)
	{
		_log.debug("Loading image: " + photo.getName());

		BufferedImage bufferedImage = null;

		try
		{
			ImageInputStream imageInputStream = ImageIO
					.createImageInputStream(photo.getInputStream());
			ImageReader reader = ImageIO.getImageReaders(imageInputStream)
					.next();
			reader.setInput(imageInputStream);
			bufferedImage = reader.read(0);
		}
		catch (IOException e)
		{
			_log.error("Could not load image", e);
			throw new UploaderException("Could not load image", e);
		}

		return bufferedImage;
	}

	/**
	 * Retrieves an icon from the classpath.
	 * 
	 * @param classPathUrl
	 *            The classpath URL (i.e., icons/someicon.png)
	 * @return The Icon
	 */
	public static Icon getIcon(String classPathUrl)
	{
		return new ImageIcon(ClassLoader.getSystemResource(classPathUrl));
	}

	/**
	 * Gets an icon scaled to the specified width / height, with a margin around
	 * it.
	 * 
	 * @param icon
	 *            The icon
	 * @param mainWidth
	 *            The width of the surrounding container
	 * @param mainHeight
	 *            The height of the surrounding container
	 * @param margin
	 *            The margin around the image.
	 * @return
	 */
	public static ImageIcon getScaledIcon(ImageIcon icon, int mainWidth,
			int mainHeight, int margin)
	{
		if (margin < 0)
		{
			margin = 0;
		}

		ImageIcon newIcon = icon;

		int width = icon.getIconWidth();
		int height = icon.getIconHeight();

		if (width > height)
		{
			if (width > (mainWidth - (margin * 2)))
			{
				newIcon = new ImageIcon(icon.getImage().getScaledInstance(
						mainWidth - (margin * 2), -1, Image.SCALE_FAST));
			}
		}
		else
		{
			if (height > (mainHeight - (margin * 2)))
			{
				newIcon = new ImageIcon(icon.getImage().getScaledInstance(-1,
						mainHeight - (margin * 2), Image.SCALE_DEFAULT));
			}
		}

		return newIcon;
	}

	/**
	 * Creates a scaled version of an image given the maximum width and height.
	 * 
	 * @param image
	 *            The image
	 * @param maximumWidth
	 *            The maximum width
	 * @param maximumHeight
	 *            The maximum height
	 * @return The scaled image
	 */
	public static BufferedImage createScaledImage(BufferedImage image,
			int maximumWidth, int maximumHeight)
	{
		final double imageWidth = image.getWidth();
		final double imageHeight = image.getHeight();
		final double scale = Math.min((double) maximumWidth / imageWidth,
				(double) maximumHeight / imageHeight);

		return createScaledImage(image, scale);
	}

	/**
	 * Creates a scaled version of an image given the scale in %age.
	 * 
	 * @param image
	 *            The image
	 * @param scale
	 *            The scale
	 * @return The scaled image
	 */
	private static BufferedImage createScaledImage(BufferedImage image,
			double scale)
	{
		if (scale == 1.0)
			return image;

		final double imageWidth = image.getWidth();
		final double imageHeight = image.getHeight();
		final int reducedWidth = (int) (imageWidth * scale);
		final int reducedHeight = (int) (imageHeight * scale);

		BufferedImage reducedImage = _graphicsConfiguration
				.createCompatibleImage(reducedWidth, reducedHeight);
		Graphics2D g2d = reducedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.drawImage(image, AffineTransform.getScaleInstance(scale, scale),
				null);
		g2d.dispose();

		return reducedImage;
	}

	/**
	 * Resizes an image on disk, maintaining the aspect ratio.
	 * 
	 * @param photo
	 *            The photo to resize
	 * @param maxWidth
	 *            The maximum width of the new photo
	 * @param maxHeight
	 *            The maximum height of the new photo
	 */
	public static void resizeImage(Photo photo, int maxWidth, int maxHeight)
	{
		try
		{
			_log.debug(String.format("Resizing [%s] to (%d, %d)", photo
					.getPath(), maxWidth, maxHeight));
			BufferedImage image = loadImage(photo);

			if (image.getHeight() > image.getWidth())
			{
				_log.debug("Image is portrait - swapping width and height");
				
				int temp = maxWidth;
				maxWidth = maxHeight;
				maxHeight = temp;
			}
			
			if (image.getWidth() <= maxWidth && image.getHeight() <= maxHeight)
			{
				_log
						.debug("Image is already smaller than width and height -- skipping");
				return;
			}

			BufferedImage resizedImage = createScaledImage(image, maxWidth,
					maxHeight);
			ImageIO.write(resizedImage, "jpeg", photo.getOutputStream());

			_log.debug(String.format("Image written to [%s]", photo.getPath()));
		}
		catch (IOException e)
		{
			_log.error("Could not resize image", e);
			throw new UploaderException("Could not resize image", e);
		}
	}

	/**
	 * Rotates an image in a given direction. The direction can be specified by
	 * {@link #ROTATE_LEFT} and {@value #ROTATE_RIGHT} respectively.
	 * 
	 * @param file
	 * @param direction
	 */
	public static void rotateImage(Photo photo, int direction)
	{
		try
		{
			_log.debug("Rotating image [" + photo.getPath()
					+ "] with direction [" + direction + "]");

			BufferedImage sourceImage = ImageIO.read(photo.getInputStream());

			int targetWidth = sourceImage.getHeight();
			int targetHeight = sourceImage.getWidth();

			AffineTransform transform = new AffineTransform();

			if (ROTATE_RIGHT == direction)
			{
				transform.translate(sourceImage.getHeight(), 0);
				transform.rotate(Math.PI / 2);
			}
			else
			{
				transform.translate(0, sourceImage.getWidth());
				transform.rotate(-Math.PI / 2);
			}

			BufferedImage tempImage = _graphicsConfiguration
					.createCompatibleImage(targetWidth, targetHeight);
			Graphics2D g2d = tempImage.createGraphics();
			g2d.drawImage(sourceImage, transform, null);
			g2d.dispose();

			ImageIO.write(tempImage, "jpeg", photo.getOutputStream());

			_log.debug("Image written to [" + photo.getPath() + "]");
		}
		catch (Exception e)
		{
			_log.error("", e);
			throw new UploaderException("Could not rotate image", e);
		}
	}
}
