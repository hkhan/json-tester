/*
 * Copyright (C) 2010 Thomas Dudek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package agile.co.uk.jsontester.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

/**
 * Fluent builder for the {@linkplain HttpClient} >= 4.2 to simplify its usage.
 * <p/>
 * <p>
 * To execute a request, use {@linkplain #get(String) Http.get("http://w...")} or {@linkplain #post(String)
 * Http.post("http://w...")}, specify at least the {@linkplain HttpClient client} to use for this request and execute it
 * with one of the execution methods.
 * <p/>
 * <h2>Request execution methods overview:</h2>
 * <dl>
 * <dt>{@link HttpRequestBuilder#asResponse() .asResponse()}</dt>
 * <dd>execute and return the {@linkplain HttpResponse}</dd>
 * <p/>
 * <dt>{@link HttpRequestBuilder#asString() .asString()}</dt>
 * <dd>execute and return the content body as a String</dd>
 * <p/>
 * <dt>{@link HttpRequestBuilder#asFile(File) .asFile(File)}</dt>
 * <dd>execute and save the stream to a file</dd>
 * <p/>
 * <dt>{@link HttpRequestBuilder#as(ResponseHandler) .as(ResponseHandler)}</dt>
 * <dd>execute and process the response with the {@link ResponseHandler}</dd>
 * <p/>
 * <dt>{@link HttpRequestBuilder#consumeResponse() .consumeResponse()}</dt>
 * <dd>execute and consume any available content on the stream</dd>
 * <p/>
 * <dt>{@link HttpRequestBuilder#throwAwayResponse() .throwAwayResponse()}</dt>
 * <dd>execute and abort immediately</dd>
 * </dl>
 * <p/>
 * <h2>Usage examples:</h2>
 * <p>
 * <dl>
 * <dt>Simple GET-request</dt>
 * <dd>
 * 
 * <pre>
 * final String site = Http.get(&quot;http://somesite.com&quot;).use(client).asString();
 * </pre>
 * 
 * </dd>
 * <p/>
 * <dt>Extended GET-request using a {@linkplain RequestCustomizer request customizer} and a custom
 * {@linkplain ResponseHandler response handler}</dt>
 * <dd>
 * 
 * <pre>
 * final String ok = Http.get(&quot;http://somesite.com&quot;).use(client).header(&quot;User-Agent&quot;, &quot;HttpClient Wrapper&quot;)
 *         .charset(&quot;UTF-8&quot;).followRedirects(true).customize(new RequestCustomizer() {
 *             &#064;Override
 *             public void customize(final HttpUriRequest request) {
 *                 HttpProtocolParams.useExpectContinue(request.getParams());
 *             }
 *         }).as(new ResponseHandler&lt;String&gt;() {
 *             &#064;Override
 *             public String handleResponse(final HttpResponse response) throws IOException {
 *                 final int statusCode = response.getStatusLine().getStatusCode();
 *                 return statusCode == HttpStatus.SC_OK ? &quot;YES&quot; : &quot;NO&quot;;
 *             }
 *         });
 * </pre>
 * 
 * </dd>
 * <p/>
 * <dt>Simple POST-request</dt>
 * <dd>
 * 
 * <pre>
 * final HttpResponse serachResult = Http.post(&quot;http://somesite.com&quot;).data(&quot;search_name&quot;, &quot;cat&quot;)
 *         .data(&quot;search_gender&quot;, &quot;m&quot;).asResponse();
 * </pre>
 * 
 * </dd>
 * </dl>
 * </p>
 * 
 * @author Thomas Dudek (mastacode@gmail.com)
 * @version 0.1a
 * @copyright Copyright (c) Thomas Dudek
 */
public final class Http {

    /**
     * May be used to modify the {@linkplain HttpUriRequest request} just before it is being executed.
     * 
     * @see {@link HttpRequestBuilder#customize(RequestCustomizer)}
     */
    public static interface RequestCustomizer {

        /**
         * Customizes the request before the execution is done.
         * 
         * @param request
         *            the request to customize
         */
        void customize(final HttpUriRequest request);
    }

    Http() {
    }

    /**
     * Creates a builder object for a GET-request. Supports no data nor entity modifications.
     * 
     * @param url
     *            the URL to use for this request.
     * @return the builder object for the this URL.
     */
    public static HttpRequestBuilder get(final String url) {
        return new HttpGetRequestBuilder(url);
    }

    /**
     * Converts a {@linkplain HttpResponse} to a String by calling {@link EntityUtils#toString(HttpEntity)} on its
     * {@linkplain HttpEntity entity}.
     * 
     * @param response
     *            the {@linkplain HttpResponse response} to convert.
     * @param defaultCharset
     *            character set to be applied if none found in the entity.
     * @return the response body as a String or {@code null}, if no response body exists or an error occurred while
     *         converting.
     * @throws NullPointerException
     *             if the given response was null
     */
    public static String asString(final HttpResponse response, final String defaultCharset) {
        if (response == null) {
            throw new NullPointerException();
        }

        final HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }

        try {
            return EntityUtils.toString(entity, defaultCharset);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Contains a superset of builder methods for all request-type dependent builders.
     */
    public abstract static class HttpRequestBuilder {

        protected final String url;
        protected HttpClient client;
        protected List<Header> headers;
        protected List<RequestCustomizer> customizers;
        protected String charset = null;
        protected Boolean followRedirects;
        protected HttpUriRequest request;

        /**
         * Creates a new builder object for the given URL.
         * 
         * @param url
         *            the URL for the current request.
         * @throws NullPointerException
         *             if the given URL was null
         */
        protected HttpRequestBuilder(final String url) {
            if (url == null) {
                throw new NullPointerException("URL must not be null.");
            }
            this.url = url;
        }

        /**
         * Sets the entity to send with this request.
         * 
         * @param entity
         *            the entity to set for this request
         * @return this builder
         * @throws UnsupportedOperationException
         *             if this request not supports entity modifications
         */
        public HttpRequestBuilder entity(final HttpEntity entity) {
            throw new UnsupportedOperationException("This HTTP-method doesn't support to add an entity.");
        }

        /**
         * Specifies the {@linkplain HttpClient client} to use for this request.
         * 
         * @param client
         *            the client
         * @return this builder
         * @throws NullPointerException
         *             if the given {@link HttpClient} was null
         */
        public HttpRequestBuilder use(final HttpClient client) {
            if (client == null) {
                throw new NullPointerException("HttpClient must not be null.");
            }
            this.client = client;
            return this;
        }

        /**
         * Adds the given {@linkplain RequestCustomizer request customizer} to this request. All customizers are being
         * applied sequentially just before the request is being executed.
         * 
         * @param customizer
         *            the customizer to add to this request
         * @return this builder
         */
        public HttpRequestBuilder customize(final RequestCustomizer customizer) {
            getCustomizers().add(customizer);
            return this;
        }

        /**
         * Adds a header with the given {@code name} and {@code value} to this request.
         * 
         * @param name
         * @param value
         * @return this builder
         */
        public HttpRequestBuilder header(final String name, final String value) {
            getHeaders().add(new BasicHeader(name, value));
            return this;
        }

        /**
         * Adds the given {@linkplain Header header} to this request.
         * 
         * @param header
         * @return this builder
         */
        public HttpRequestBuilder header(final Header header) {
            getHeaders().add(header);
            return this;
        }

        /**
         * Sets the encoding for this request.
         * 
         * @param charset
         * @return this builder
         */
        public HttpRequestBuilder charset(final String charset) {
            this.charset = charset;
            return this;
        }

        /**
         * Sets the behavior of redirection following for this request. The behavior effects for this request only.
         * 
         * @param follow
         *            {@code true}, if redirects should be followed, otherwise {@code false}
         * @return this builder
         */
        public HttpRequestBuilder followRedirects(final boolean follow) {
            followRedirects = follow;
            return this;
        }

        /**
         * Executes this request and returns the result as a {@linkplain HttpResponse} object.
         * 
         * @return the response of this request
         * @throws IllegalStateException
         *             if no {@link HttpClient} was specified
         * @throws IOException
         *             if an error occurs while execution
         */
        public HttpResponse asResponse() throws IOException {
            if (client == null) {
                throw new IllegalStateException("Please specify a HttpClient instance to use for this request.");
            }

            request = createFinalRequest();

            final HttpResponse response = client.execute(request);

            return response;
        }

        /**
         * Executes this request and returns the content body of the result as a String. If no response body exists,
         * this returns {@code null}.
         * 
         * @return the response body as a String or {@code null} if no response body exists.
         * @throws IOException
         *             if an error occurs while execution
         */
        public String asString() throws IOException {
            return Http.asString(asResponse(), charset);
        }

        /**
         * Executes this request and saves the response stream to a file. The stream is going to be copied if and only
         * if the response was successful ({@code 2xx}) and a response body exists. If the response code was
         * {@code >= 300}, a {@link FileNotFoundException} is thrown. If no body exists, this returns {@code false} and
         * no exception is thrown.
         * 
         * @param target
         *            the file in which the stream should be copied.
         * @return {@code true} if the stream was copied successful to the file, otherwise {@code false}.
         * @throws IOException
         *             if an error occurs while execution
         * @throws FileNotFoundException
         *             if the response code was {@code >= 300}.
         */
        public boolean asFile(final File target) throws IOException {
            final HttpResponse response = asResponse();
            return new FileResponseHandler(target, url).handleResponse(response);
        }

        /**
         * Executes this request and processes the response using the given response handler.
         * 
         * @param handler
         *            the response handler
         * @return the response object generated by the response handler.
         * @throws IOException
         *             if an error occurs while execution
         */
        public <T> T as(final ResponseHandler<? extends T> handler) throws IOException {
            if (handler == null) {
                throw new NullPointerException("ResponseHandler must not be null.");
            }

            final HttpResponse response = asResponse();
            return handler.handleResponse(response);
        }

        /**
         * Executes this request and aborts immediately after execution using the {@linkplain HttpUriRequest#abort()
         * abort} method of this request.
         * 
         * @throws IOException
         *             if an error occurs while execution
         */
        public void throwAwayResponse() throws IOException {
            asResponse();
            request.abort();
        }

        /**
         * Executes this request and consumes any available content on the response stream.
         * 
         * @throws IOException
         *             if an error occurs while execution
         */
        public void consumeResponse() throws IOException {
            final HttpResponse response = asResponse();
            final HttpEntity entity = response.getEntity();
            if (entity == null) {
                return;
            }

            if (entity.isStreaming()) {
                InputStream instream = entity.getContent();
                if (instream != null) {
                    instream.close();
                }
            }

        }

        abstract protected HttpUriRequest createRequest() throws IOException;

        protected String getUrl() {
            return url;
        }

        protected List<Header> getHeaders() {
            if (headers == null) {
                headers = new ArrayList<Header>();
            }
            return headers;
        }

        protected List<RequestCustomizer> getCustomizers() {
            if (customizers == null) {
                customizers = new ArrayList<RequestCustomizer>();
            }
            return customizers;
        }

        protected HttpClient getClient() {
            return client;
        }

        private HttpUriRequest createFinalRequest() throws IOException {
            final HttpUriRequest request = createRequest();

            applyHeaders(request);
            if (followRedirects != null) {
                HttpClientParams.setRedirecting(request.getParams(), followRedirects);
            }
            applyCustomizers(request);

            return request;
        }

        private void applyHeaders(final HttpRequest request) {
            if (headers != null) {
                for (final Header h : headers) {
                    request.setHeader(h);
                }
            }
        }

        private void applyCustomizers(final HttpUriRequest request) {
            if (customizers != null) {
                for (final RequestCustomizer modifier : customizers) {
                    modifier.customize(request);
                }
            }
        }

    }

    // Response handlers

    /**
     * Saves the stream to a file and returns <code>true</code>, if no error occurred while saving.
     * 
     * @see {@link HttpRequestBuilder#asFile(File)}
     */
    private static class FileResponseHandler implements ResponseHandler<Boolean> {

        protected final File file;
        protected final String url;

        public FileResponseHandler(final File file, final String url) {
            this.file = file;
            this.url = url;
        }

        @Override
        public Boolean handleResponse(final HttpResponse response) throws IOException {
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode >= 300) {
                throw new FileNotFoundException("Source not found at " + url + ", response code " + statusCode);
            }

            final HttpEntity entity = response.getEntity();
            if (entity == null) {
                return false;
            }

            return copyStreamToFile(entity.getContent(), file);
        }

        protected boolean copyStreamToFile(final InputStream source, final File target) throws IOException {
            final byte[] buffer = new byte[1024 * 4];
            final OutputStream out = new FileOutputStream(target);
            int read = 0;
            while ((read = source.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            try {
                out.close();
            } catch (final IOException ignore) {
            }
            return true;
        }

    }

    // Request builders

    /**
     * GET-request builder.
     * 
     * @see {@link Http#get(String)}
     */
    private static class HttpGetRequestBuilder extends HttpRequestBuilder {

        protected HttpGetRequestBuilder(final String url) {
            super(url);
        }

        @Override
        protected HttpUriRequest createRequest() throws IOException {
            return new HttpGet(url);
        }

    }

}