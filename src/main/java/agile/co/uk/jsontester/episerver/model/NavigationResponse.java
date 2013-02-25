package agile.co.uk.jsontester.episerver.model;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
public class NavigationResponse {

    @JsonSerialize
    private MainMenu mainMenu;

}
