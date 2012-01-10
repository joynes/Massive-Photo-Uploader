/*********************************************************************
 * Massive Photo Uploader: Upload a batch of albums to facebook
 * Copyright (C) 2010  Johannes Kählare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/
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
import com.google.code.facebookapi.*;

public class Init {

    private Logger log = Logger.getLogger(getClass());
    private PropertyUtils props = new PropertyUtils();
    private FacebookXmlRestClient facebookClient;
    private BrowserLauncher browserLauncher;
    private String authToken;
    private boolean requireLogin;
    private JTextArea jTextArea;
    private JFrame mainFrame;

    /**
     * @param jTextArea Area to put information about status
     */
    public Init(JTextArea jTextArea, JFrame mainFrame) {
        this.jTextArea = jTextArea;
        this.mainFrame = mainFrame;
    }

    public void authenticate() throws Exception {
        getAuthenticatedSession();

    }

    /**
     * Initialise the connection to Facebook.
     * @return true if session already exists
     */
    public boolean connect() throws Exception {

        String apiKey = props.getProperty(Constants.Properties.FACEBOOK_API_KEY);
        String secret = props.getProperty(Constants.Properties.FACEBOOK_SECRET);

        String persistentKey = props.getProperty(Constants.Properties.SESSION_PERSISTENT_KEY);
        String sessionSecret = props.getProperty(Constants.Properties.SESSION_SECRET_KEY);

        // if firsttime then redirect to page where to accept the peding stuff

        printInfo("Initialising facebook client");
        String firsttime = props.getProperty(Constants.Properties.INIT_FIRSTIME);
        if (firsttime != null && firsttime.equals("1")) {
            alert("Program is run for first time! I will guide you through the setup now :). Press ok to continue!");
            alert("First you need to authorize this application. \nIt is recommended that you mark <Keep me logged in> to not need to login in everytime you use the applic. \nPress ok to open a browser and authorize this application");
            openBrowser("http://www.facebook.com/authorize.php?" + "&api_key=" + apiKey + "&ext_perm=photo_upload");
            alert("Press ok when the application has been authorized!");
            alert("It is very much recommended that you allow:\n<Allow massivePhotoUpload to upload or modify photos without my approval each time> \nOtherwise this might be a lot of unnecessary manual work. \nPress ok to browse to this setup!");
            openBrowser("http://www.facebook.com/authorize.php?" + "&api_key=" + apiKey + "&ext_perm=photo_upload");
            alert("Press ok when done with the setup. Files will now be uploaded!");

            props.setProperty(Constants.Properties.INIT_FIRSTIME, "0");
        }


        if (StringUtils.isNotBlank(persistentKey)) {
            printInfo("Using existing session");

            facebookClient = new FacebookXmlRestClient(apiKey, secret,
                    persistentKey);
            
            //facebookClient.
            
           // new FacebookXmlRestClient
                    
                    
                    
            //getFacebookClient().setSessionSecret(sessionSecret);
            requireLogin = false;
        } else {
            facebookClient = new FacebookXmlRestClient(apiKey, secret);
            requireLogin = true;
        }

        //getFacebookClient().setIsDesktop(true);
        
        String auth = getAuthorisationToken();

        if (auth != null) {
            String loginUrl = props.getProperty("facebook.login-url");
            openBrowser(loginUrl + "&api_key=" + apiKey + "&auth_token=" + auth);
            return false;
        }
        return true;
    }

    private String getAuthorisationToken() throws Exception {
        try {
            if (requireLogin) {
                authToken = getFacebookClient().auth_createToken();

                log.debug("Got auth key: " + authToken);
            }

            return authToken;
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
            browserLauncher = new BrowserLauncher(null);
            browserLauncher.openURLinBrowser(url);


        } catch (Exception e) {
            printInfo("Could not launch browser");
            log.error("Could not launch browser", e);
            throw new Exception("Could not launch browser", e);
        }
    }

    private void getAuthenticatedSession() throws Exception {
        try {
            if (requireLogin) {
                getFacebookClient().auth_getSession(authToken);

                if ("0".equals(getFacebookClient().getCacheSessionExpires())) {
                    log.debug("Session is set not to expire - saving info");

                    props.setProperty(
                            Constants.Properties.SESSION_PERSISTENT_KEY,
                            getFacebookClient().getCacheSessionKey());
                    props.setProperty(Constants.Properties.SESSION_SECRET_KEY,
                            getFacebookClient().getCacheSessionSecret());
                }
            }
        } catch (Exception e) {
            printInfo("Could not authenticate session");
            log.error("Could not authenticate session", e);
            throw new Exception("Could not authenticate session", e);
        }
    }

    /**
     * Print info to the gui, scroll down the textarea and do normal log.
     * @param str
     */
    public void printInfo(String str) {
        jTextArea.setText(jTextArea.getText() + "\n" + str);
        jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
        log.info(str);
    }

    /**
     * POPUP a message alert
     * @param str
     */
    public void alert(String str) {
        JOptionPane.showMessageDialog(mainFrame, str);
    }

    /**
     * @return the facebookClient
     */
    public FacebookXmlRestClient getFacebookClient() {
        return facebookClient;
    }
}
