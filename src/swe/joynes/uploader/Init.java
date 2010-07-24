package swe.joynes.uploader;

import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import edu.stanford.ejalbert.BrowserLauncher;
import javax.swing.JOptionPane;
import uk.me.phillsacre.Constants;
import uk.me.phillsacre.PropertyUtils;
import uk.me.phillsacre.WorkingFacebookRestClient;

public class Init {

    private Logger _log = Logger.getLogger(getClass());
    private PropertyUtils _props = new PropertyUtils();
    public WorkingFacebookRestClient _facebookClient;
    private BrowserLauncher _browserLauncher;
    private String _authToken;

    //private FacebookDAO _facebookDAO;
    private boolean _requireLogin;
    JTextArea _jTextArea;
    JFrame _mainFrame;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Init applic");
            //Init test = new Init(null);
            //test.connect();
            Thread.sleep(10000);
        //test.authenticate();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param jTextArea Area to put information about status
     */
    public Init(JTextArea jTextArea, JFrame mainFrame) {
        _jTextArea = jTextArea;
        _mainFrame = mainFrame;
    }

    public void authenticate() throws Exception {
        getAuthenticatedSession();

    }

    /**
     * Initialise the connection to Facebook.
     * @return true if session already exists
     */
    public boolean connect() throws Exception {

        String apiKey = _props.getProperty(Constants.Properties.FACEBOOK_API_KEY);
        String secret = _props.getProperty(Constants.Properties.FACEBOOK_SECRET);

        String persistentKey = _props.getProperty(Constants.Properties.SESSION_PERSISTENT_KEY);
        String sessionSecret = _props.getProperty(Constants.Properties.SESSION_SECRET_KEY);

        // if firsttime then redirect to page where to accept the peding stuff

        printInfo("Initialising facebook client");
        String firsttime = _props.getProperty(Constants.Properties.INIT_FIRSTIME);
        if (firsttime != null && firsttime.equals("1")) {
            alert("Program is run for first time! I will guide you through the setup now :). Press ok to continue!");
            alert("First you need to authorize this application. \nIt is recommended that you mark <Keep me logged in> to not need to login in everytime you use the applic. \nPress ok to open a browser and authorize this application");
            openBrowser("http://www.facebook.com/authorize.php?" + "&api_key=" + apiKey + "&ext_perm=photo_upload");
            alert("Press ok when the application has been authorized!");
            alert("It is very much recommended that you allow:\n<Allow massivePhotoUpload to upload or modify photos without my approval each time> \nOtherwise this might be a lot of unnecessary manual work. \nPress ok to browse to this setup!");
            openBrowser("http://www.facebook.com/authorize.php?" + "&api_key=" + apiKey + "&ext_perm=photo_upload");
            alert("Press ok when done with the setup. Files will now be uploaded!");
            
            _props.setProperty(Constants.Properties.INIT_FIRSTIME, "0");
        }


        if (StringUtils.isNotBlank(persistentKey)) {
            printInfo("Using existing session");

            _facebookClient = new WorkingFacebookRestClient(apiKey, secret,
                    persistentKey);
            _facebookClient.setSessionSecret(sessionSecret);
            _requireLogin = false;
        } else {
            _facebookClient = new WorkingFacebookRestClient(apiKey, secret);
            _requireLogin = true;
        }

        _facebookClient.setIsDesktop(true);

        String auth = getAuthorisationToken();

        if (auth != null) {
            String loginUrl = _props.getProperty("facebook.login-url");
            openBrowser(loginUrl + "&api_key=" + apiKey + "&auth_token=" + auth);
            return false;
        }
        return true;
    }

    private String getAuthorisationToken() throws Exception {
        try {
            if (_requireLogin) {
                _authToken = _facebookClient.auth_createToken();

                _log.debug("Got auth key: " + _authToken);
            }

            return _authToken;
        } catch (Exception e) {
            printInfo("Error when getting auth token");
            throw new Exception("Error when getting auth token", e);
        }
    }

    /**
     * Implementing MainWindowUI
     */
    private void openBrowser(String url) throws Exception {
        try {
            printInfo(String.format("Browsing to: [%s]", url));
            _browserLauncher = new BrowserLauncher(null);
            _browserLauncher.openURLinBrowser(url);


        } catch (Exception e) {
            printInfo("Could not launch browser");
            _log.error("Could not launch browser", e);
            throw new Exception("Could not launch browser", e);
        }
    }

    private void getAuthenticatedSession() throws Exception {
        try {
            if (_requireLogin) {
                _facebookClient.auth_getSession(_authToken);

                if ("0".equals(_facebookClient.getSessionExpires())) {
                    _log.debug("Session is set not to expire - saving info");

                    _props.setProperty(
                            Constants.Properties.SESSION_PERSISTENT_KEY,
                            _facebookClient.getSessionKey());
                    _props.setProperty(Constants.Properties.SESSION_SECRET_KEY,
                            _facebookClient.getSessionSecret());
                }
            }
        } catch (Exception e) {
            printInfo("Could not authenticate session");
            _log.error("Could not authenticate session", e);
            throw new Exception("Could not authenticate session", e);
        }
    }

    /**
     * Print info to the gui, scroll down the textarea and do normal log.
     * @param str
     */
    public void printInfo(String str) {
        _jTextArea.setText(_jTextArea.getText() + "\n" + str);
        _jTextArea.setCaretPosition(_jTextArea.getDocument().getLength());
        _log.info(str);
    }

    /**
     * POPUP a message alert
     * @param str
     */
    public void alert(String str) {
        JOptionPane.showMessageDialog(_mainFrame, str);
    }
}
