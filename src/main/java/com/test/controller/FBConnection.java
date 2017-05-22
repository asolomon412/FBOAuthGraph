package com.test.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FBConnection {

    // fb app id and fb app secret are provided to us by the FB Developers website
    public static final String FB_APP_ID = "1410235862353226";
    public static final String FB_APP_SECRET = "e1c54b7b0afa3d05fb135114a7f58ce3";
    public static final String REDIRECT_URI = "http://localhost:8080/welcome";

    static String accessToken = "";

    public String getFBAuthUrl() {
        String fbLoginUrl = "";
        // the job of the lines of code below is to take the user to the facebook page and
        // once they are authenticated they will be redirected back to our site
        // & is used in this case to provide concatination for things that are delimeters
            fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
                    + FBConnection.FB_APP_ID + "&redirect_uri="
                   + REDIRECT_URI
                    + "&scope=email";

        return fbLoginUrl;
    }

    public String getFBGraphUrl(String code) {
        String fbGraphUrl = "";

            fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
                    + "client_id=" + FBConnection.FB_APP_ID + "&redirect_uri="
                    + REDIRECT_URI
                    + "&client_secret=" + FB_APP_SECRET + "&code=" + code;

        return fbGraphUrl;
    }

    public String getAccessToken(String code) {
        if (accessToken.equals("")) {
            URL fbGraphURL;
            try {
                fbGraphURL = new URL(getFBGraphUrl(code));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid code received " + e);
            }
            URLConnection fbConnection;
            StringBuffer b = null;
            try {
                fbConnection = fbGraphURL.openConnection();
                BufferedReader in;
                in = new BufferedReader(new InputStreamReader(
                        fbConnection.getInputStream()));
                String inputLine;
                b = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                    b.append(inputLine + "\n");
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to connect with Facebook "
                        + e);
            }

            accessToken = b.toString();
            if (accessToken.startsWith("{")) {
                throw new RuntimeException("ERROR: Access Token Invalid: "
                        + accessToken);
            }
        }
        return accessToken;
    }
}