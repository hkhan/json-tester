package agile.co.uk.jsontester.episerver.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
public class MainMenu {

    @JsonSerialize
    private List<MenuItem> menuItems;

}
