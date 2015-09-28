package com.zch.safelottery.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public abstract class AbstractHttpApi {
	
	public static final String VERSION = "1.0.0";
    public static final int DEFAULT_MAX_CONNECTIONS = 10;
    public static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_MAX_RETRIES = 5;
    public static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 1500;
    public static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ENCODING_GZIP = "gzip";
    public static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    public static int timeout = DEFAULT_SOCKET_TIMEOUT;
	
	/**
     * Create a thread-safe client. This client does not do redirecting, to allow us to capture
     * correct "error" codes.
     * @return HttpClient
     */
	public static DefaultHttpClient createHttpClient() {
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        
        HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }
	 /**
     * Create the default HTTP protocol parameters.
     */
    private static HttpParams createHttpParams() {
    	BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, timeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, "android-zch-http/1.0.0");
        return httpParams;
    }

}
