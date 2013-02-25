package agile.co.uk.jsontester.http;

import java.io.IOException;

import lombok.Data;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.map.ObjectMapper;

import agile.co.uk.jsontester.episerver.model.NavigationResponse;
import agile.co.uk.jsontester.http.client.Request;

@Data
public class NavigationMenuLoader {

    private String uri;

    private Executor<String> httpExecutor;

    public NavigationResponse connect() {
        try {
            NavigationResponse response = Request.Get("http://localhost:8000").execute()
                    .handleResponse(new ResponseHandler<NavigationResponse>() {

                        @Override
                        public NavigationResponse handleResponse(HttpResponse arg0) throws ClientProtocolException,
                                IOException {
                            ObjectMapper mapper = new ObjectMapper();
                            return mapper.readValue(arg0.getEntity().getContent(), NavigationResponse.class);

                        }

                    });
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
