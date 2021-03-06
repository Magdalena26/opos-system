package edu.project.to;

/**
 * Created by magda on 29.11.16.
 */
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class Main {
    public static final String APPLICATION_NAME = "NamesCrawler";
    public static final String APPLICATION_VERSION = "";
    public static final String APPLICATION_URL = "https://github.com/";
    public static final String APPLICATION_UA = Main.APPLICATION_NAME + "/" + Main.APPLICATION_VERSION + " (" + Main.APPLICATION_URL + ")";

    public static int crawlUrl(String urlAddress) {
        init();
        sayHello();

        if (urlAddress == null) {
            System.out.println("Please pass the URL to crawl as an argument to this application: \"javac Main.java http://my-website.com\"");
            return -1;
        } else {
            new WebCrawler(urlAddress.trim()).start();
            return 0;
        }

    }

/*
    public static void main(String args[]){
        int i = crawlUrl("http://www.breakingnewsenglish.com/1701/170110-zombies.html");
        DbConnector dbConnector = new DbConnector();
        List<String> urls = dbConnector.getPeopleByName("Roy");
        urls.forEach(x -> System.out.println(x));

        List<String> names = dbConnector.getPeopleByUrl("http://www.breakingnewsenglish.com/1701/170110-zombies.html");
        names.forEach(x -> System.out.println(x));
    }
*/

    private static void init() {

        // Create a all-trusting TrustManager
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        } };

        // Install the all-trusting TrustManager
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.out.println("Unable to install the all-trusting TrustManager");
        }

        // Create & Install a all-trusting HostnameVerifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };

        // Install the all-trusting HostnameVerifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    private static void sayHello() {
        String lineVersion = APPLICATION_NAME + " v" + APPLICATION_VERSION;
        for (int i = lineVersion.length(); i < 37; i++) lineVersion += " ";

        System.out.println("#############################################");
        System.out.println("### " + lineVersion + " ###");
        System.out.println("###                                       ###");
        System.out.println("###            TO Project                 ###");
     //   System.out.println("### " + APPLICATION_URL.substring(8) + "  ###");
        System.out.println("#############################################\n");
    }
}