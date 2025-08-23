package com.status.configurations;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class UnsafeRestTemplate {

    public static RestTemplate create() throws Exception {
        // Trust manager that does nothing (trusts all certificates)
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        // Install the all-trusting manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        // Disable hostname verification
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Create RestTemplate using default factory
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }
}
