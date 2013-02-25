package agile.co.uk.jsontester.episerver.model;

import java.util.List;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
@Data
public class MenuItem {

    private String name;

    public Boolean getIsFrontMenuItem() {
        return isFrontMenuItem;
    }

    public void setIsFrontMenuItem(Boolean isFrontMenuItem) {
        this.isFrontMenuItem = isFrontMenuItem;
    }

    private String link;

    private Boolean isFrontMenuItem;

    private List<MenuItem> submenu;
}
