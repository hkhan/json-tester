package agile.co.uk.jsontester.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class Response {

    private HttpResponse response;
    private boolean consumed;

    Response(HttpResponse httpResponse) {
        this.response = httpResponse;
    }

    private void assertNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException("Response content has been already consumed");
        }
    }

    private void dispose() {
        if (this.consumed) {
            return;
        }

        HttpEntity entity = this.response.getEntity();
        if (entity == null) {
            return;
        }

        try {
            if (entity.isStreaming()) {
                InputStream instream = entity.getContent();
                if (instream != null) {
                    instream.close();
                }
            }
        } catch (Exception ignore) {
        } finally {
            this.consumed = true;
        }
    }

    public <T> T handleResponse(final ResponseHandler<T> handler) throws ClientProtocolException, IOException {
        assertNotConsumed();
        try {
            return handler.handleResponse(this.response);
        } finally {
            dispose();
        }
    }

}
