package agile.co.uk.jsontester.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import agile.co.uk.jsontester.episerver.model.NavigationResponse;

@RunWith(MockitoJUnitRunner.class)
public class NavigationMenuLoaderTest {

    private NavigationMenuLoader menuLoder;

    @Before
    public void setup() {
        menuLoder = new NavigationMenuLoader();
    }

    @Test
    public void testJsonParsing() throws IOException {
        // URL url = Resources.getResource("test-response.json");
        // String text = Resources.toString(url, Charsets.UTF_8);
        // when(executor.execute(isA(Request.class))).thenReturn(text);

        NavigationResponse response = menuLoder.connect();
        assertThat(response.getMainMenu().getMenuItems(), is(not(empty())));

        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response);

        System.out.println(new ObjectMapper().writeValueAsString(response));
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}
