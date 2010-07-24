package uk.me.phillsacre;

/**
 * Application Constants.
 * 
 * @author psacre
 * 
 */
public abstract class Constants {

    /**
     * General constants.
     * 
     * @author psacre
     * 
     */
    public interface General {

        /**
         * The uploader download URL.
         */
        public static final String DOWNLOAD_PAGE = "http://code.google.com/p/fb-photo-uploader/downloads/list";
        /**
         * The URL the application needs to connect to in order to check if
         * there is an updated version available.
         */
        public static final String VERSION_CHECK_URL = "http://fb-photo-uploader.googlecode.com/svn/trunk/latest-release.xml";
        /**
         * The minimum number of milliseconds to leave between checking whether
         * an update is available.
         */
        public static final long MILLIS_BETWEEN_CHECKS = 1000 * 60 * 60 * 24;
    }

    /**
     * Constants for Caching
     * 
     * @author psacre
     * 
     */
    public interface Cache {

        /**
         * The name of the Thumbnail Cache
         */
        public static final String THUMBNAIL_CACHE_NAME = "thumbnailCache";
        /**
         * The name of the Photo Tag cache.
         */
        public static final String PHOTO_TAG_CACHE_NAME = "photoTagCache";
        /**
         * The name of the caption cache.
         */
        public static final String CAPTION_CACHE_NAME = "captionCache";
    }

    /**
     * Constants for the photo browser
     * 
     * @author psacre
     * 
     */
    public interface PhotoBrowser {

        /**
         * The maximum width of a photo in the browser.
         */
        public static final int MAX_PHOTO_WIDTH = 120;
        /**
         * The maximum height of a photo in the browser.
         */
        public static final int MAX_PHOTO_HEIGHT = 120;
    }

    /**
     * Constants for the image editor.
     * 
     * @author psacre
     * 
     */
    public interface ImageEditor {

        /**
         * The width of the tag box, as a percentage of the image width (0.0 =
         * 0%, 1.0 = 100%)
         */
        public static final double TAG_WIDTH = 0.2;
        /**
         * The height of the tag box, as a percentage of the image height (0.0 =
         * 0%, 1.0 = 100%)
         */
        public static final double TAG_HEIGHT = 0.2;
    }

    /**
     * Constants for errors returned by Facebook.
     * 
     * @author psacre
     * 
     */
    public interface FacebookErrors {

        /**
         * Cannot upload because the album is full.
         */
        public static final int ALBUM_FULL = 321;
        /**
         * Cannot upload because there are too many pending photos awaiting
         * approval.
         */
        public static final int TOO_MANY_PENDING_PHOTOS = 325;
        /**
         * Photo Tag has an invalid subject.
         */
        public static final int INVALID_PHOTO_TAG_SUBJECT = 322;
        /**
         * There are too many pending photo tags.
         */
        public static final int TOO_MANY_PENDING_TAGS = 326;
    }

    /**
     * Property file keys
     * 
     * @author psacre
     * 
     */
    public interface Properties {

        public static final String FACEBOOK_API_KEY = "facebook.api-key";
        public static final String FACEBOOK_SECRET = "facebook.secret";
        public static final String SESSION_PERSISTENT_KEY = "session.persistent-key";
        public static final String SESSION_SECRET_KEY = "session.secret-key";
        public static final String SESSION_USER_ID = "session.user-id";
        public static final String LAST_DIRECTORY = "uploader.last-directory";
        public static final String MAX_DIMENSION = "uploader.maxdimension";
        public static final String VISIBILITY = "uploader.visibility";
        
        public static final String IMAGE_MINIMUM_SIZE = "image.minimumsize";
        public static final String ALBUM_NAME_DELIMITER = "album.namedelimiter";
        public static final String ALBUM_MAX_IMAGES = "album.maximages";
        public static final String INIT_FIRSTIME = "uploader.firsttime";
    }
}
