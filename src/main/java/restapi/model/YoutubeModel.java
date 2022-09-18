package restapi.model;

import filter.StringFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class YoutubeModel {
    public String link;
    public Filter[] filter;

    @ToString
    public static class Filter {
        public String cls;
        public String type;
        public String pattern;
    }
}
