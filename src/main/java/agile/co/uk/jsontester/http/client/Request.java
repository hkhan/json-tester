package agile.co.uk.jsontester.http.client;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class Request {

    private HttpRequestBase request;

    Request(HttpRequestBase request) {
        this.request = request;
    }

    public static Request Get(String uri) {
        return new Request(new HttpGet(uri));
    }

    public Request addHeader(String name, String value) {
        this.request.addHeader(name, value);
        return this;
    }

    public Request config(String name, Object value) {
        this.request.getParams().setParameter(name, value);
        return this;
    }

    public Request socketTimeout(int timeout) {
        return config(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    public Request connectTimeout(int timeout) {
        return config(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }

    public Request viaProxy(HttpHost proxy) {
        return config(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    HttpRequestBase getHttpRequest() {
        return this.request;
    }

    public Response execute() throws ClientProtocolException, IOException {
        return new Response(new DefaultHttpClient().execute(this.request));
    }
}
